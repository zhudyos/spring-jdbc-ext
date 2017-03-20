package io.zhudy.spring.jdbc;

import java.util.List;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public interface Page<E> {

    /**
     * @return
     */
    long getTotal();

    /**
     * @return
     */
    List<E> getRows();
}
