package io.zhudy.spring.jdbc;

import org.springframework.core.NamedThreadLocal;
import org.springframework.util.Assert;

/**
 * 分页查询工具类.
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
public final class PageHelper {

    static final ThreadLocal<RowBounds> TL_ROW_BOUNDS = new NamedThreadLocal<>("spring-jdbc-ext-RowBounds");

    /**
     * 设置分页参数.
     *
     * @param offset 偏移量
     * @param limit  返回记录数
     */
    public static void set(int offset, int limit) {
        Assert.isTrue(offset >= 0, "[offset] 必须大于等于0");
        Assert.isTrue(limit >= 1, "[limit] 必须大于等于1");

        TL_ROW_BOUNDS.set(new RowBounds(offset, limit));
    }

    /**
     * 返回分页参数.
     *
     * @return 分页参数
     */
    static RowBounds get() {
        return TL_ROW_BOUNDS.get();
    }

    /**
     * 清理分页参数.
     */
    static void clear() {
        TL_ROW_BOUNDS.remove();
    }
}
