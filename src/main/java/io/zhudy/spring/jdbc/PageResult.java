package io.zhudy.spring.jdbc;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public class PageResult {

    private String sql;
    private String countSql;
    private long total;

    public PageResult(String sql, String countSql, long total) {
        this.sql = sql;
        this.countSql = countSql;
        this.total = total;
    }

    public String getSql() {
        return sql;
    }

    public String getCountSql() {
        return countSql;
    }


    public long getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "sql='" + sql + '\'' +
                ", countSql='" + countSql + '\'' +
                ", total=" + total +
                '}';
    }
}
