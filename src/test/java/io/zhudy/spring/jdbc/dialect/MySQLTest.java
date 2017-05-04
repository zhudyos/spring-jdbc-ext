package io.zhudy.spring.jdbc.dialect;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.zhudy.spring.jdbc.PageHelper;
import io.zhudy.spring.jdbc.PageNamedParameterJdbcTemplate;
import io.zhudy.spring.jdbc.TestConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class MySQLTest extends AbstractTransactionalTestNGSpringContextTests {

    @Configuration
    static class ContextConfiguration {
        @Bean
        public DataSource dataSource() {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.mariadb.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://192.168.99.100:3306/test");
            config.setUsername("root");
            config.setPassword("root");
            return new HikariDataSource(config);
        }
    }

    @Test
    public void testQueryPage() {
        PageNamedParameterJdbcTemplate template = new PageNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(jdbcTemplate));

        PageHelper.set(TestConstants.PAGE_OFFSET, TestConstants.PAGE_LIMIT);
        List<Map<String, Object>> list = template.queryForList("select * from person", EmptySqlParameterSource.INSTANCE);
        System.out.println(list);
    }
}
