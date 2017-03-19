package io.zhudy.spring.jdbc;

import org.springframework.core.NamedThreadLocal;
import org.springframework.util.Assert;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public final class PageHelper {

    private static final ThreadLocal<RowBounds> TL_ROW_BOUNDS = new NamedThreadLocal<>("spring-jdbc-ext-RowBounds");

    /**
     * @param offset
     * @param limit
     */
    public static void set(int offset, int limit) {
        Assert.isTrue(offset >= 0, "[offset] 必须大于等于0");
        Assert.isTrue(limit >= 1, "[limit] 必须大于等于1");

        TL_ROW_BOUNDS.set(new RowBounds(offset, limit));
    }

    /**
     * @return
     */
    static RowBounds get() {
        return TL_ROW_BOUNDS.get();
    }

    /**
     *
     */
    static void clear() {
        TL_ROW_BOUNDS.remove();
    }

}
