package io.zhudy.spring.jdbc;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public interface Page<T> {

    /**
     * @return
     */
    long getTotal();

    /**
     * @return
     */
    T getRows();
}
