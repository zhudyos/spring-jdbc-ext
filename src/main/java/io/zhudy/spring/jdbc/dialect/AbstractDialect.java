package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.Dialect;
import io.zhudy.spring.jdbc.GroupSqlParameterSource;
import io.zhudy.spring.jdbc.PageQueryException;
import io.zhudy.spring.jdbc.RowBounds;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public abstract class AbstractDialect implements Dialect {

    private static final Logger log = LoggerFactory.getLogger(AbstractDialect.class);

    private static final boolean CACHE_ENABLED = Boolean.valueOf(System.getProperty("io.zhudy.spring.jdbc.sql.cacheEnabled", "true"));
    private Map<Integer, String> cacheCountSql;
    private Map<Integer, String> cachePageSql;

    /**
     * COUNT 查询项.
     */
    protected static final List<SelectItem> COUNT_SELECT_ITEMS = new ArrayList<SelectItem>(1) {
        {
            add(new SelectExpressionItem(new Column("count(1)")));
        }
    };
    /**
     * 分页查询表别名.
     */
    protected static final Alias TABLE_ALIAS = new Alias("zd_pnt_") {
        {
            setUseAs(false);
        }
    };

    private final ResultSetExtractor<Long> COUNT_RSE = rs -> {
        if (rs.next()) {
            return rs.getLong(1);
        }
        throw new RuntimeException("获取 COUNT 错误");
    };


    protected NamedParameterJdbcOperations namedParameterJdbcOperations;

    protected AbstractDialect(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        if (CACHE_ENABLED) {
            cacheCountSql = new ConcurrentHashMap<>();
            cachePageSql = new ConcurrentHashMap<>();
        }
    }

    @Override
    public <T> T query(String sql, SqlParameterSource sps, ResultSetExtractor<T> rse, RowBounds rowBounds) {
        log.debug("origin sql: {}", sql);
        int k = sql.hashCode();

        long total;
        try {
            String csql = getCountSql(sql, k);
            log.debug("count sql: {}", csql);

            total = namedParameterJdbcOperations.query(csql, sps, COUNT_RSE);
            log.debug("total: {}", total);
            if (total <= 0) {
                return (T) new PageArrayList<>(total);
            }
        } catch (JSQLParserException e) {
            log.error("convert to count sql error [{}]", sql);
            throw new PageQueryException(sql, e);
        }

        try {
            String psql = getPageSql(sql, k);
            log.debug("page sql: {}", psql);

            GroupSqlParameterSource gsps = new GroupSqlParameterSource(sps);
            preparePageParams(gsps, rowBounds);
            T rows = namedParameterJdbcOperations.query(psql, gsps, rse);
            return (T) new PageArrayList<>(rows, total);
        } catch (JSQLParserException e) {
            log.error("convert to page sql error [{}]", sql);
            throw new PageQueryException(sql, e);
        }
    }

    private String getCountSql(String sql, int k) throws JSQLParserException {
        String csql;
        if (CACHE_ENABLED && (csql = cacheCountSql.get(k)) != null) {
            return csql;
        }

        csql = convertToCountSql(sql);
        if (csql != null) {
            if (CACHE_ENABLED) {
                cacheCountSql.put(k, csql);
            }
            return csql;
        }

        Statement stmt = CCJSqlParserUtil.parse(sql);
        SelectBody body = ((Select) stmt).getSelectBody();
        cleanSelect(body);

        if (body instanceof PlainSelect && isSimpleCountSql((PlainSelect) body)) {
            PlainSelect ps = (PlainSelect) body;
            ps.setSelectItems(COUNT_SELECT_ITEMS);
            csql = ps.toString();
        } else {
            PlainSelect ps = new PlainSelect();
            SubSelect ss = new SubSelect();
            ss.setSelectBody(body);

            ps.setFromItem(ss);
            ps.setSelectItems(COUNT_SELECT_ITEMS);
            ss.setAlias(TABLE_ALIAS);
            csql = ps.toString();
        }

        cacheCountSql.put(k, csql);
        return csql;
    }

    private String getPageSql(String sql, int k) throws JSQLParserException {
        String psql;
        if (CACHE_ENABLED && (psql = cachePageSql.get(k)) != null) {
            return psql;
        }

        psql = convertToPageSql(sql);
        if (CACHE_ENABLED) {
            cachePageSql.put(k, psql);
        }
        return psql;
    }

    protected void cleanSelect(SelectBody sb) {
        if (sb instanceof PlainSelect) {
            ((PlainSelect) sb).setOrderByElements(null);
        } else if (sb instanceof WithItem) {
            WithItem wi = (WithItem) sb;
            if (wi.getSelectBody() != null) {
                cleanSelect(wi.getSelectBody());
            }
        } else {
            SetOperationList sol = (SetOperationList) sb;
            if (sol.getSelects() != null) {
                sol.getSelects().forEach(this::cleanSelect);
            }
            sol.setOrderByElements(null);
        }
    }

    /**
     * 转换 COUNT SQL.
     *
     * @param sql 原 SQL
     * @return COUNT SQL
     */
    protected abstract String convertToCountSql(String sql) throws JSQLParserException;

    /**
     * 转换 Page SQL.
     *
     * @param sql 原 SQL
     * @return Page SQL
     */
    protected abstract String convertToPageSql(String sql) throws JSQLParserException;

    /**
     * 设置分页参数.
     *
     * @param gsps      JDBC 参数源
     * @param rowBounds 分页参数
     */
    protected abstract void preparePageParams(GroupSqlParameterSource gsps, RowBounds rowBounds);

    /**
     * 判断是否可以使用简单的 COUNT SQL.
     *
     * @param select SELECT
     * @return true/false
     */
    protected boolean isSimpleCountSql(PlainSelect select) {
        if (select.getGroupByColumnReferences() != null) {
            return false;
        }

        if (select.getDistinct() != null) {
            return false;
        }

        for (SelectItem item : select.getSelectItems()) {
            if (item.toString().contains("?")) {
                return false;
            }
            if (item instanceof SelectExpressionItem) {
                if (((SelectExpressionItem) item).getExpression() instanceof Function) {
                    return false;
                }
            }
        }
        return true;
    }
}
