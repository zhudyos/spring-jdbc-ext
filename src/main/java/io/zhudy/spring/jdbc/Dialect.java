package io.zhudy.spring.jdbc;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public interface Dialect {

//    MySQL, MarriaDB, Oracle, PostgreSQL, Sqlite

    /**
     *
     * @param sql
     * @param pss
     * @param rse
     * @param rowBounds
     * @param <E>
     * @return
     */
    <E> Page<E> query(String sql, PreparedStatementSetter pss, ResultSetExtractor<List<E>> rse, RowBounds rowBounds);

}
