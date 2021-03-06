package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.GroupSqlParameterSource;
import io.zhudy.spring.jdbc.RowBounds;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.sql.Types;

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
public class OracleDialect extends AbstractDialect {

    private static final String START_ROW_PARAM_NAME = "__start_row__";
    private static final String END_ROW_PARAM_NAME = "__end_row__";

    public OracleDialect(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        super(namedParameterJdbcOperations);
    }

    @Override
    protected String convertToCountSql(String sql) throws JSQLParserException {
        return null;
    }

    @Override
    protected String convertToPageSql(String sql) throws JSQLParserException {
        StringBuilder sb = new StringBuilder(sql.length() + 100);
        sb.append("select * from (select tmp_page.*, ROWNUM r_id from (")
                .append(sql)
                .append(") tmp_page where ROWNUM<=")
                .append(":").append(END_ROW_PARAM_NAME)
                .append(")").append(" where r_id>")
                .append(":").append(START_ROW_PARAM_NAME);
        return sb.toString();
    }

    @Override
    protected void preparePageParams(GroupSqlParameterSource gsps, RowBounds rowBounds) {
        gsps.addValue(START_ROW_PARAM_NAME, rowBounds.getStartRow(), Types.NUMERIC);
        gsps.addValue(END_ROW_PARAM_NAME, rowBounds.getEndRow(), Types.NUMERIC);
    }

}
