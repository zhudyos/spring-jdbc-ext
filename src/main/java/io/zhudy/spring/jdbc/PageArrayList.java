package io.zhudy.spring.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
class PageArrayList<E> extends ArrayList<E> implements PageList<E> {

    private long total;
    private long offset;
    private int limit;

    PageArrayList(long total, long offset, int limit) {
        this.total = total;
        this.offset = offset;
        this.limit = limit;
    }

    PageArrayList(Collection<? extends E> c, long total, long offset, int limit) {
        super(c);
        this.total = total;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public List<E> getRows() {
        return this;
    }
}
