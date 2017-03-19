package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.Dialect;
import io.zhudy.spring.jdbc.PageResult;
import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public class AbstractDialect implements Dialect {

    

    @Override
    public PageResult query(String sql, PreparedStatementSetter pss) {
        return null;
    }
}
