package io.zhudy.spring.jdbc;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public interface Dialect {

    /**
     * @param <T>
     * @param sql
     * @param sps
     * @param rse
     * @param rowBounds
     * @return
     */
    <T> T query(String sql, SqlParameterSource sps, ResultSetExtractor<T> rse, RowBounds rowBounds);

    /**
     *
     */
    enum Type {
        MySQL, MarriaDB, Oracle, PostgreSQL, SQLite;

        public static Type from(String s) {
            if ("MySQL".equalsIgnoreCase(s)) {
                return MySQL;
            }
            if ("MarriaDb".equalsIgnoreCase(s)) {
                return MarriaDB;
            }
            if ("Oracle".equalsIgnoreCase(s)) {
                return Oracle;
            }
            if ("PostgreSQL".equalsIgnoreCase(s)) {
                return PostgreSQL;
            }
            if ("SQLite".equalsIgnoreCase(s)) {
                return SQLite;
            }
            throw new IllegalArgumentException("未找到[" + s + "]数据库类型");
        }
    }
}
