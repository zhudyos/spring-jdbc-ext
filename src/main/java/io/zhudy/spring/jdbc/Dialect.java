package io.zhudy.spring.jdbc;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * 数据库分页查询接口.
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
public interface Dialect {

    /**
     * 执行分页查询.
     *
     * @param <T>       返回类型
     * @param sql       SQL
     * @param sps       {@link SqlParameterSource}
     * @param rse       {@link ResultSetExtractor}
     * @param rowBounds 分页参数
     * @return 查询结果集
     */
    <T> T query(String sql, SqlParameterSource sps, ResultSetExtractor<T> rse, RowBounds rowBounds);

    /**
     * 数据库类型枚举.
     */
    enum Type {
        MySQL, MarriaDB, Oracle, PostgreSQL, Informix, SQLServer, SQLite;

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
