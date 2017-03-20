package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.Page;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
class PageArrayList<T> extends ArrayList implements Page<T> {

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
    public T getRows() {
        return (T) this;
    }
}
