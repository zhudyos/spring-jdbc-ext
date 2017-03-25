package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.PageResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
class PageArrayList<T> extends ArrayList implements PageResult<T> {

    private long total;

    PageArrayList(long total) {
        this.total = total;
    }

    PageArrayList(T c, long total) {
        super((Collection) c);
        this.total = total;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public List<T> getRows() {
        return this;
    }

    @Override
    public String toString() {
        return "{total=" + total +
                ", rows=" + super.toString() +
                '}';
    }
}
