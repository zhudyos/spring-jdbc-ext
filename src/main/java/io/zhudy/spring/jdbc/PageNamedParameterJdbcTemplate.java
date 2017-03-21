package io.zhudy.spring.jdbc;

import io.zhudy.spring.jdbc.dialect.MySQLDialect;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.Assert;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public class PageNamedParameterJdbcTemplate implements NamedParameterJdbcOperations {

    private NamedParameterJdbcOperations namedParameterJdbcOperations;
    private Dialect dialect;

    /**
     * @param namedParameterJdbcOperations
     */
    public PageNamedParameterJdbcTemplate(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        Assert.notNull(namedParameterJdbcOperations);
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;

        // 获取数据库类型
        Dialect.Type dbType = this.namedParameterJdbcOperations.getJdbcOperations().execute(
                (ConnectionCallback<Dialect.Type>) con -> {
                    DatabaseMetaData metaData = con.getMetaData();
                    String s = metaData.getDatabaseProductName();
                    return Dialect.Type.from(s);
                });

        switch (dbType) {
            case MySQL:
            case MarriaDB:
                dialect = new MySQLDialect(namedParameterJdbcOperations);
                break;
        }
    }

    @Override
    public JdbcOperations getJdbcOperations() {
        return namedParameterJdbcOperations.getJdbcOperations();
    }

    @Override
    public <T> T execute(String sql, SqlParameterSource paramSource, PreparedStatementCallback<T> action) throws DataAccessException {
        return namedParameterJdbcOperations.execute(sql, paramSource, action);
    }

    @Override
    public <T> T execute(String sql, Map<String, ?> paramMap, PreparedStatementCallback<T> action) throws DataAccessException {
        return namedParameterJdbcOperations.execute(sql, paramMap, action);
    }

    @Override
    public <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException {
        return namedParameterJdbcOperations.execute(sql, action);
    }

    @Override
    public <T> T query(String sql, SqlParameterSource paramSource, ResultSetExtractor<T> rse) throws DataAccessException {
        RowBounds rowBounds = PageHelper.get();
        if (rowBounds != null) {
            // 分页查询
            try {
                return dialect.query(sql, paramSource, rse, rowBounds);
            } finally {
                PageHelper.clear();
            }
        }
        return namedParameterJdbcOperations.query(sql, paramSource, rse);
    }

    @Override
    public <T> T query(String sql, Map<String, ?> paramMap, ResultSetExtractor<T> rse) throws DataAccessException {
        return query(sql, new MapSqlParameterSource(paramMap), rse);
    }

    @Override
    public <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
        return query(sql, EmptySqlParameterSource.INSTANCE, rse);
    }

    @Override
    public void query(String sql, SqlParameterSource paramSource, RowCallbackHandler rch) throws DataAccessException {
        namedParameterJdbcOperations.query(sql, paramSource, rch);
    }

    @Override
    public void query(String sql, Map<String, ?> paramMap, RowCallbackHandler rch) throws DataAccessException {
        namedParameterJdbcOperations.query(sql, paramMap, rch);
    }

    @Override
    public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
        namedParameterJdbcOperations.query(sql, rch);
    }

    @Override
    public <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) throws DataAccessException {
        return query(sql, paramSource, new RowMapperResultSetExtractor<>(rowMapper));
    }

    @Override
    public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException {
        return query(sql, new MapSqlParameterSource(paramMap), rowMapper);
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        return query(sql, EmptySqlParameterSource.INSTANCE, rowMapper);
    }

    @Override
    public <T> T queryForObject(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) throws DataAccessException {
        return namedParameterJdbcOperations.query(sql, paramSource, rs -> {
            T r = null;
            if (rs.next()) {
                r = rowMapper.mapRow(rs, 0);
            }
            return r;
        });
    }

    @Override
    public <T> T queryForObject(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException {
        return queryForObject(sql, new MapSqlParameterSource(paramMap), rowMapper);
    }

    @Override
    public <T> T queryForObject(String sql, SqlParameterSource paramSource, Class<T> requiredType) throws DataAccessException {
        return namedParameterJdbcOperations.queryForObject(sql, paramSource, requiredType);
    }

    @Override
    public <T> T queryForObject(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws DataAccessException {
        return namedParameterJdbcOperations.queryForObject(sql, paramMap, requiredType);
    }

    @Override
    public Map<String, Object> queryForMap(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return namedParameterJdbcOperations.queryForMap(sql, paramSource);
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return namedParameterJdbcOperations.queryForMap(sql, paramMap);
    }

    @Override
    public long queryForLong(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return namedParameterJdbcOperations.queryForLong(sql, paramSource);
    }

    @Override
    public long queryForLong(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return namedParameterJdbcOperations.queryForLong(sql, paramMap);
    }

    @Override
    public int queryForInt(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return namedParameterJdbcOperations.queryForInt(sql, paramSource);
    }

    @Override
    public int queryForInt(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return namedParameterJdbcOperations.queryForInt(sql, paramMap);
    }

    @Override
    public <T> List<T> queryForList(String sql, SqlParameterSource paramSource, Class<T> elementType) throws DataAccessException {
        return query(sql, paramSource, new SingleColumnRowMapper<>(elementType));
    }

    @Override
    public <T> List<T> queryForList(String sql, Map<String, ?> paramMap, Class<T> elementType) throws DataAccessException {
        return queryForList(sql, new MapSqlParameterSource(paramMap), elementType);
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return query(sql, paramSource, new ColumnMapRowMapper());
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return queryForList(sql, paramMap);
    }

    @Override
    public SqlRowSet queryForRowSet(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return namedParameterJdbcOperations.queryForRowSet(sql, paramSource);
    }

    @Override
    public SqlRowSet queryForRowSet(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return namedParameterJdbcOperations.queryForRowSet(sql, paramMap);
    }

    @Override
    public int update(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return namedParameterJdbcOperations.update(sql, paramSource);
    }

    @Override
    public int update(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return namedParameterJdbcOperations.update(sql, paramMap);
    }

    @Override
    public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder) throws DataAccessException {
        return namedParameterJdbcOperations.update(sql, paramSource, generatedKeyHolder);
    }

    @Override
    public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder, String[] keyColumnNames) throws DataAccessException {
        return namedParameterJdbcOperations.update(sql, paramSource, generatedKeyHolder, keyColumnNames);
    }

    @Override
    public int[] batchUpdate(String sql, Map<String, ?>[] batchValues) {
        return namedParameterJdbcOperations.batchUpdate(sql, batchValues);
    }

    @Override
    public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) {
        return namedParameterJdbcOperations.batchUpdate(sql, batchArgs);
    }
}
