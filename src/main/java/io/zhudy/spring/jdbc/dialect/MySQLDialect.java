package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.GroupSqlParameterSource;
import io.zhudy.spring.jdbc.RowBounds;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.sql.Types;

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
public class MySQLDialect extends AbstractDialect {

    private static final String OFFSET_PARAM_NAME = "__offset__";
    private static final String LIMIT_PARAM_NAME = "__limit__";

    public MySQLDialect(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        super(namedParameterJdbcOperations);
    }

    @Override
    protected String convertToCountSql(String sql) throws JSQLParserException {
        return null;
    }

    @Override
    protected String convertToPageSql(String sql) {
        StringBuilder sb = new StringBuilder(sql.length() + 50);
        sb.append(sql)
                .append(" limit ")
                .append(":").append(OFFSET_PARAM_NAME)
                .append(", ")
                .append(":").append(LIMIT_PARAM_NAME);

        return sb.toString();
    }

    @Override
    protected void preparePageParams(GroupSqlParameterSource gsps, RowBounds rowBounds) {
        // 添加参数
        gsps.addValue(OFFSET_PARAM_NAME, rowBounds.getOffset(), Types.NUMERIC);
        gsps.addValue(LIMIT_PARAM_NAME, rowBounds.getLimit(), Types.NUMERIC);
    }
}
