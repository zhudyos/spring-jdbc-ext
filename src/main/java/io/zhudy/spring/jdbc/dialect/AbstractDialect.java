package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.Dialect;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public abstract class AbstractDialect implements Dialect {

    private static final Logger log = LoggerFactory.getLogger(AbstractDialect.class);

    private static final boolean CACHE_ENABLED = Boolean.valueOf(System.getProperty("io.zhudy.spring.jdbc.countSql.cacheEnabled", "true"));
    private static final Map<Integer, String> CACHE_COUNT_SQL = new HashMap<>();

    /**
     *
     */
    protected static final List<SelectItem> COUNT_SELECT_ITEMS = new ArrayList<SelectItem>(1) {
        {
            add(new SelectExpressionItem(new Column("count(1)")));
        }
    };
    /**
     *
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
    }

    @Override
    public <T> T query(String sql, SqlParameterSource sps, ResultSetExtractor<T> rse, RowBounds rowBounds) {
        log.debug("origin sql: {}", sql);

        long total;
        try {
            String csql = getCountSql(sql);
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
            String psql = convertToPageSql(sql, rowBounds);
            log.debug("page sql: {}", psql);

            T rows = namedParameterJdbcOperations.query(psql, sps, rse);
            return (T) new PageArrayList<>(rows, total);
        } catch (JSQLParserException e) {
            log.error("convert to page sql error [{}]", sql);
            throw new PageQueryException(sql, e);
        }
    }

    private String getCountSql(String sql) throws JSQLParserException {
        int k = sql.hashCode();
        String csql;
        if (CACHE_ENABLED && (csql = CACHE_COUNT_SQL.get(k)) != null) {
            return csql;
        }

        csql = convertToCountSql(sql);
        if (csql != null) {
            if (CACHE_ENABLED) {
                CACHE_COUNT_SQL.put(k, csql);
            }
            return csql;
        }

        Statement stmt = CCJSqlParserUtil.parse(sql);
        SelectBody body = ((Select) stmt).getSelectBody();
        cleanSelect(body);

        if (body instanceof PlainSelect && isSimpleCount((PlainSelect) body)) {
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

        CACHE_COUNT_SQL.put(k, csql);
        return csql;
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
     * @param sql
     * @return
     */
    protected abstract String convertToCountSql(String sql) throws JSQLParserException;

    /**
     * @param sql
     * @param rowBounds
     * @return
     */
    protected abstract String convertToPageSql(String sql, RowBounds rowBounds) throws JSQLParserException;

    protected boolean isSimpleCount(PlainSelect select) {
        //包含group by的时候不可以
        if (select.getGroupByColumnReferences() != null) {
            return false;
        }

        //包含distinct的时候不可以
        if (select.getDistinct() != null) {
            return false;
        }

        for (SelectItem item : select.getSelectItems()) {
            // select列中包含参数的时候不可以，否则会引起参数个数错误
            if (item.toString().contains("?")) {
                return false;
            }
            //如果查询列中包含函数，也不可以，函数可能会聚合列
            if (item instanceof SelectExpressionItem) {
                if (((SelectExpressionItem) item).getExpression() instanceof Function) {
                    return false;
                }
            }
        }
        return true;
    }
}
