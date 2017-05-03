package io.zhudy.spring.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@ContextConfiguration(locations = "classpath*:test-context.xml")
public class PageNamedParameterJdbcTemplateTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SingleConnectionDataSource dataSource;
    @Autowired
    private NamedParameterJdbcOperations namedTemplate;

    private List<Person> persons = new ArrayList<>();
    private int total = 100;

    @BeforeClass
    public void setUp() throws Exception {
        ScriptUtils.executeSqlScript(dataSource.getConnection(),
                applicationContext.getResource("classpath:test.sql"));

        SqlParameterSource[] batchArgs = new SqlParameterSource[total];
        for (int i = 0; i < total; i++) {
            Person p = new Person();
            p.setId(i + 1);
            p.setName("Hello" + i);
            p.setAddress("Shanghai" + i);
            p.setCountry("China" + i);
            persons.add(p);
            batchArgs[i] = new BeanPropertySqlParameterSource(p);
        }

        int[] rs = namedTemplate.batchUpdate("INSERT INTO person VALUES(:id, :name, :address, :country)", batchArgs);
        System.out.println(rs.length);
    }

    @Test
    public void testQuery() throws Exception {
        int offset = 10, limit = 5;
        PageHelper.set(offset, limit);

        String sql = "select * from person";
        PageResult<Person> pageResult = (PageResult) namedTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Person.class));

        assertTrue(pageResult.getTotal() == total);
        assertTrue(pageResult.getRows().size() == limit);
        for (int i = 0; i < limit; i++) {
            Person p = pageResult.getRows().get(i);
            assertTrue(persons.indexOf(p) == (offset + i));
        }
    }

    @Test
    public void testQuery1() throws Exception {
        int offset = 10, limit = 5;
        PageHelper.set(offset, limit);

        String sql = "SELECT * FROM person";
        PageResult<Person> pageResult = (PageResult) namedTemplate.query(sql, Collections.emptyMap(),
                BeanPropertyRowMapper.newInstance(Person.class));

        assertTrue(pageResult.getTotal() == total);
        assertTrue(pageResult.getRows().size() == limit);
        for (int i = 0; i < limit; i++) {
            Person p = pageResult.getRows().get(i);
            assertTrue(persons.indexOf(p) == (offset + i));
        }
    }

    @Test
    public void testQuery2() throws Exception {
        int offset = 10, limit = 5;
        PageHelper.set(offset, limit);

        String sql = "SELECT * FROM person";
        PageResult<Person> pageResult = (PageResult) namedTemplate.query(sql,
                new RowMapperResultSetExtractor(BeanPropertyRowMapper.newInstance(Person.class)));

        assertTrue(pageResult.getTotal() == total);
        assertTrue(pageResult.getRows().size() == limit);
        for (int i = 0; i < limit; i++) {
            Person p = pageResult.getRows().get(i);
            assertTrue(persons.indexOf(p) == (offset + i));
        }
    }

    @Test
    public void testQuery3() throws Exception {
        int offset = 10, limit = 5;
        PageHelper.set(offset, limit);

        String sql = "SELECT * FROM person";
        PageResult<Person> pageResult = (PageResult) namedTemplate.query(sql,
                EmptySqlParameterSource.INSTANCE, BeanPropertyRowMapper.newInstance(Person.class));

        assertTrue(pageResult.getTotal() == total);
        assertTrue(pageResult.getRows().size() == limit);
        for (int i = 0; i < limit; i++) {
            Person p = pageResult.getRows().get(i);
            assertTrue(persons.indexOf(p) == (offset + i));
        }
    }

    @Test
    public void testQuery4() throws Exception {
        int offset = 10, limit = 5;
        PageHelper.set(offset, limit);

        String sql = "SELECT * FROM person";
        PageResult<Person> pageResult = (PageResult) namedTemplate.query(sql, Collections.EMPTY_MAP,
                new RowMapperResultSetExtractor<>(BeanPropertyRowMapper.newInstance(Person.class)));

        assertTrue(pageResult.getTotal() == total);
        assertTrue(pageResult.getRows().size() == limit);
        for (int i = 0; i < limit; i++) {
            Person p = pageResult.getRows().get(i);
            assertTrue(persons.indexOf(p) == (offset + i));
        }
    }

    @Test
    public void testQuery5() throws Exception {
        int offset = 10, limit = 5;
        PageHelper.set(offset, limit);

        String sql = "SELECT * FROM person";
        PageResult<Person> pageResult = (PageResult) namedTemplate.query(sql,
                EmptySqlParameterSource.INSTANCE,
                new RowMapperResultSetExtractor<>(BeanPropertyRowMapper.newInstance(Person.class)));

        assertTrue(pageResult.getTotal() == total);
        assertTrue(pageResult.getRows().size() == limit);
        for (int i = 0; i < limit; i++) {
            Person p = pageResult.getRows().get(i);
            assertTrue(persons.indexOf(p) == (offset + i));
        }
    }

    @Test
    public void testQueryForList() throws Exception {
        int offset = 10, limit = 5;
        PageHelper.set(offset, limit);

        String sql = "SELECT * FROM person";
        PageResult<Object> pageResult = (PageResult) namedTemplate.queryForList(sql, Collections.EMPTY_MAP);

        assertTrue(pageResult.getTotal() == total);
        assertTrue(pageResult.getRows().size() == limit);
    }

    @Test
    public void testQueryForList1() throws Exception {
        int offset = 10, limit = 5;
        PageHelper.set(offset, limit);

        String sql = "SELECT * FROM person";
        PageResult<Object> pageResult = (PageResult) namedTemplate.queryForList(sql, EmptySqlParameterSource.INSTANCE);

        assertTrue(pageResult.getTotal() == total);
        assertTrue(pageResult.getRows().size() == limit);
    }

}