package io.zhudy.spring.jdbc;

/**
 * 分页参数.
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
public class RowBounds {

    private long offset;
    private int limit;

    RowBounds(long offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public long getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public long getStartRow() {
        return offset;
    }

    public long getEndRow() {
        return offset + limit;
    }
}
