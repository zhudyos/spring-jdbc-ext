package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.RowBounds;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public class MySQLDialect extends AbstractDialect {

    public MySQLDialect(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        super(namedParameterJdbcOperations);
    }

    @Override
    protected String convertToCountSql(String sql) throws JSQLParserException {
        return null;
    }

    @Override
    protected String convertToPageSql(String sql, RowBounds rowBounds) {
        StringBuilder sb = new StringBuilder(sql.length() + 20);
        sb.append(sql).append(" limit ").append(rowBounds.getOffset()).append(", ").append(rowBounds.getLimit());
        return sb.toString();
    }
}
