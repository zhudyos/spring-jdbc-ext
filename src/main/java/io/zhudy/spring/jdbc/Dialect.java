package io.zhudy.spring.jdbc;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public interface Dialect {

//    MySQL, MarriaDB, Oracle, PostgreSQL, Sqlite

    /**
     * @param <T>
     * @param sql
     * @param sps
     * @param rse
     * @param rowBounds
     * @return
     */
    <T> T query(String sql, SqlParameterSource sps, ResultSetExtractor<T> rse, RowBounds rowBounds);

}
