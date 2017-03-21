package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.RowBounds;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
public class PostgreSQLDialect extends AbstractDialect {

    public PostgreSQLDialect(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        super(namedParameterJdbcOperations);
    }

    @Override
    protected String convertToCountSql(String sql) throws JSQLParserException {
        return null;
    }

    @Override
    protected String convertToPageSql(String sql, RowBounds rowBounds) throws JSQLParserException {
        StringBuilder sb = new StringBuilder(sql.length() + 20);
        sb.append(sql).append(" limit ").append(rowBounds.getLimit()).append(" offset ").append(rowBounds.getOffset());
        return sb.toString();
    }
}
