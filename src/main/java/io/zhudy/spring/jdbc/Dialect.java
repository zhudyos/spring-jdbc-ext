package io.zhudy.spring.jdbc;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public interface Dialect {

//    MySQL, MarriaDB, Oracle, PostgreSQL, Sqlite

    PageResult query(String sql, PreparedStatementSetter pss);

}
