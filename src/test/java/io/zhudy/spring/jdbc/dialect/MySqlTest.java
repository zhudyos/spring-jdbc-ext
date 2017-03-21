package io.zhudy.spring.jdbc.dialect;

import io.zhudy.spring.jdbc.Page;
import io.zhudy.spring.jdbc.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@ContextConfiguration(locations = "classpath*:test-context.xml")
public class MySqlTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Test
    public void testQuery() {
//        String sql = "select * from bs_book";
        String sql = "SELECT a.id,a.name FROM bs_book a UNION SELECT b.id,b.nickname NAME FROM bs_user b";
        PageHelper.set(0, 30);
        Page<List<Map<String, Object>>> p = (Page) namedParameterJdbcOperations.queryForList(sql, EmptySqlParameterSource.INSTANCE);
        System.out.println(p);
    }

}
