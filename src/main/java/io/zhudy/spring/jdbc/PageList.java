package io.zhudy.spring.jdbc;

import java.util.List;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public interface PageList<E> {

    /**
     * @return
     */
    long getTotal();

    /**
     * @return
     */
    long getOffset();

    /**
     * @return
     */
    int getLimit();

    /**
     * @return
     */
    List<E> getRows();
}
