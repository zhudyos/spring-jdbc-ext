package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.GroupSqlParameterSource;
import io.zhudy.spring.jdbc.RowBounds;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.sql.Types;

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
public class InformixDialect extends AbstractDialect {

    private static final String SKIP_PARAM_NAME = "__skip__";
    private static final String FIRST_PARAM_NAME = "__first__";

    public InformixDialect(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        super(namedParameterJdbcOperations);
    }

    @Override
    protected String convertToCountSql(String sql) throws JSQLParserException {
        return null;
    }

    @Override
    protected String convertToPageSql(String sql) throws JSQLParserException {
        StringBuilder sb = new StringBuilder(sql.length() + 50);
        sb.append("select skip ")
                .append(":").append(SKIP_PARAM_NAME)
                .append(" first ").append(":").append(FIRST_PARAM_NAME)
                .append(" * from (")
                .append(sql)
                .append(") tmp_page");
        return sb.toString();
    }

    @Override
    protected void preparePageParams(GroupSqlParameterSource gsps, RowBounds rowBounds) {
        // 添加参数
        gsps.addValue(SKIP_PARAM_NAME, rowBounds.getOffset(), Types.NUMERIC);
        gsps.addValue(FIRST_PARAM_NAME, rowBounds.getLimit(), Types.NUMERIC);
    }
}
