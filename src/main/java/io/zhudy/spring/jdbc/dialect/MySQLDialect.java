package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.Dialect;
import io.zhudy.spring.jdbc.PageResult;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public class MySQLDialect implements Dialect {

    private JdbcTemplate jdbcTemplate;

    @Override
    public PageResult query(String sql, PreparedStatementSetter pss) {
        return null;
    }
}
