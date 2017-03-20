package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
class PageArrayList<E> extends ArrayList<E> implements Page<E> {

    private long total;

    PageArrayList(long total) {
        this.total = total;
    }

    PageArrayList(Collection<? extends E> c, long total) {
        super(c);
        this.total = total;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public List<E> getRows() {
        return this;
    }
}
