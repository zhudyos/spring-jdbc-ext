package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.GroupSqlParameterSource;
import io.zhudy.spring.jdbc.RowBounds;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Types;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public class OracleDialect extends AbstractDialect {

    private static final String START_ROW_PARAM_NAME = "__start_row__";
    private static final String END_ROW_PARAN_NAME = "__end_row__";

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
        sb.append("select * from (select __tmp_page.*, ROWNUM __rn from (")
                .append(sql)
                .append(") __tmp_page where __rn<=")
                .append(":").append(END_ROW_PARAN_NAME)
                .append(")").append(" where __rn>")
                .append(":").append(START_ROW_PARAM_NAME);
        return sb.toString();
    }

    @Override
    protected void preparePageParams(GroupSqlParameterSource gsps, RowBounds rowBounds) {
        gsps.addValue(START_ROW_PARAM_NAME, rowBounds.getStartRow(), Types.NUMERIC);
        gsps.addValue(END_ROW_PARAN_NAME, rowBounds.getEndRow(), Types.NUMERIC);
    }

    public static void main(String[] args) throws JSQLParserException {
        OracleDialect d = new OracleDialect(new NamedParameterJdbcTemplate(new JdbcTemplate()));
        String s = d.convertToPageSql("SELECT * FROM TABLE_NAME");
        System.out.println(s);
    }

}
