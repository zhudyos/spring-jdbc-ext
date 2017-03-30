package io.zhudy.spring.jdbc;

import io.zhudy.spring.jdbc.dialect.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;

/**
 * {@link NamedParameterJdbcOperations} 分页查询实现.
 * <p>
 * 支持的接口:
 * </p>
 * <ul>
 * <li>{@link #query(String, ResultSetExtractor)}</li>
 * <li>{@link #query(String, RowMapper)}</li>
 * <li>{@link #query(String, Map, ResultSetExtractor)}</li>
 * <li>{@link #query(String, Map, RowMapper)}</li>
 * <li>{@link #query(String, SqlParameterSource, ResultSetExtractor)}</li>
 * <li>{@link #query(String, SqlParameterSource, RowMapper)}</li>
 * <li>{@link #queryForList(String, Map)}</li>
 * <li>{@link #queryForList(String, SqlParameterSource)}</li>
 * </ul>
 *
 * @author Kevin Zou <kevinz@weghst.com>
 */
public class PageNamedParameterJdbcTemplate extends NamedParameterJdbcTemplate {

    private NamedParameterJdbcOperations delegate;
    private Dialect dialect;

    /**
     * @param namedParameterJdbcOperations
     */
    public PageNamedParameterJdbcTemplate(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        super(namedParameterJdbcOperations.getJdbcOperations());
        this.delegate = namedParameterJdbcOperations;

        // 获取数据库类型
        Dialect.Type dbType = this.getJdbcOperations().execute(
                (ConnectionCallback<Dialect.Type>) con -> {
                    DatabaseMetaData metaData = con.getMetaData();
                    String s = metaData.getDatabaseProductName();
                    return Dialect.Type.from(s);
                });

        switch (dbType) {
            case MySQL:
            case MarriaDB:
            case SQLite:
                dialect = new MySQLDialect(delegate);
                break;
            case Oracle:
                dialect = new OracleDialect(delegate);
                break;
            case PostgreSQL:
                dialect = new PostgreSQLDialect(delegate);
                break;
            case SQLServer:
                dialect = new SQLServerDialect(delegate);
                break;
            case Informix:
                dialect = new InformixDialect(delegate);
                break;
            default:
                throw new IllegalStateException("未发现的数据库类型 [" + dbType + "]");
        }
    }

    @Override
    public JdbcOperations getJdbcOperations() {
        return delegate.getJdbcOperations();
    }

    @Override
    public <T> T execute(String sql, SqlParameterSource paramSource, PreparedStatementCallback<T> action) throws DataAccessException {
        return delegate.execute(sql, paramSource, action);
    }

    @Override
    public <T> T execute(String sql, Map<String, ?> paramMap, PreparedStatementCallback<T> action) throws DataAccessException {
        return delegate.execute(sql, paramMap, action);
    }

    @Override
    public <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException {
        return delegate.execute(sql, action);
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
        return delegate.query(sql, paramSource, rse);
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
        delegate.query(sql, paramSource, rch);
    }

    @Override
    public void query(String sql, Map<String, ?> paramMap, RowCallbackHandler rch) throws DataAccessException {
        delegate.query(sql, paramMap, rch);
    }

    @Override
    public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
        delegate.query(sql, rch);
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
        return delegate.query(sql, paramSource, rs -> {
            T r = null;
            if (rs.next()) {
                r = rowMapper.mapRow(rs, 1);
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
        return delegate.queryForObject(sql, paramSource, requiredType);
    }

    @Override
    public <T> T queryForObject(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws DataAccessException {
        return delegate.queryForObject(sql, paramMap, requiredType);
    }

    @Override
    public Map<String, Object> queryForMap(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return delegate.queryForMap(sql, paramSource);
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return delegate.queryForMap(sql, paramMap);
    }

    @Override
    public long queryForLong(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return delegate.queryForLong(sql, paramSource);
    }

    @Override
    public long queryForLong(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return delegate.queryForLong(sql, paramMap);
    }

    @Override
    public int queryForInt(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return delegate.queryForInt(sql, paramSource);
    }

    @Override
    public int queryForInt(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return delegate.queryForInt(sql, paramMap);
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
        return queryForList(sql, new MapSqlParameterSource(paramMap));
    }

    @Override
    public SqlRowSet queryForRowSet(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return delegate.queryForRowSet(sql, paramSource);
    }

    @Override
    public SqlRowSet queryForRowSet(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return delegate.queryForRowSet(sql, paramMap);
    }

    @Override
    public int update(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return delegate.update(sql, paramSource);
    }

    @Override
    public int update(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return delegate.update(sql, paramMap);
    }

    @Override
    public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder) throws DataAccessException {
        return delegate.update(sql, paramSource, generatedKeyHolder);
    }

    @Override
    public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder, String[] keyColumnNames) throws DataAccessException {
        return delegate.update(sql, paramSource, generatedKeyHolder, keyColumnNames);
    }

    @Override
    public int[] batchUpdate(String sql, Map<String, ?>[] batchValues) {
        return delegate.batchUpdate(sql, batchValues);
    }

    @Override
    public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) {
        return delegate.batchUpdate(sql, batchArgs);
    }
}
