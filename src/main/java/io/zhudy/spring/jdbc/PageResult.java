package io.zhudy.spring.jdbc;

import java.util.List;

/**
 * SQL 分页结果集.
 *
 * @author Kevin Zou <kevinz@weghst.com>
 */
public interface PageResult<T> {

    /**
     * SQL 查询总记录数.
     *
     * @return 总记录
     */
    long getTotal();

    /**
     * SQL 查询行记录.
     *
     * @return 行记录
     */
    List<T> getRows();
}
