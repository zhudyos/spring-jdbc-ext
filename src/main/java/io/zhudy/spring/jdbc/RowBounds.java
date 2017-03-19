package io.zhudy.spring.jdbc;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
class RowBounds {

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
