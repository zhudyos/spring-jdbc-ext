package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.RowBounds;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public class MySQLDialect extends AbstractDialect {

    @Override
    protected String convertToPageSql(String sql, RowBounds rowBounds) {
        return sql + " limit " + rowBounds.getOffset() + ", " + rowBounds.getLimit();
    }
}
