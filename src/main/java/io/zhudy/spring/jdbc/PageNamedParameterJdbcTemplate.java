package io.zhudy.spring.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;
import java.util.Map;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public class PageNamedParameterJdbcTemplate implements NamedParameterJdbcOperations {

    private NamedParameterJdbcTemplate npjt;

    /**
     * @param npjt
     */
    public PageNamedParameterJdbcTemplate(NamedParameterJdbcTemplate npjt) {
        this.npjt = npjt;
    }

    @Override
    public JdbcOperations getJdbcOperations() {
        return npjt.getJdbcOperations();
    }

    @Override
    public <T> T execute(String sql, SqlParameterSource paramSource, PreparedStatementCallback<T> action) throws DataAccessException {
        return npjt.execute(sql, paramSource, action);
    }

    @Override
    public <T> T execute(String sql, Map<String, ?> paramMap, PreparedStatementCallback<T> action) throws DataAccessException {
        return null;
    }

    @Override
    public <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException {
        return null;
    }

    @Override
    public <T> T query(String sql, SqlParameterSource paramSource, ResultSetExtractor<T> rse) throws DataAccessException {
        return null;
    }

    @Override
    public <T> T query(String sql, Map<String, ?> paramMap, ResultSetExtractor<T> rse) throws DataAccessException {
        return null;
    }

    @Override
    public <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
        return null;
    }

    @Override
    public void query(String sql, SqlParameterSource paramSource, RowCallbackHandler rch) throws DataAccessException {

    }

    @Override
    public void query(String sql, Map<String, ?> paramMap, RowCallbackHandler rch) throws DataAccessException {

    }

    @Override
    public void query(String sql, RowCallbackHandler rch) throws DataAccessException {

    }

    @Override
    public <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) throws DataAccessException {
        return null;
    }

    @Override
    public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException {
        return null;
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        return null;
    }

    @Override
    public <T> T queryForObject(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) throws DataAccessException {
        return null;
    }

    @Override
    public <T> T queryForObject(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException {
        return null;
    }

    @Override
    public <T> T queryForObject(String sql, SqlParameterSource paramSource, Class<T> requiredType) throws DataAccessException {
        return null;
    }

    @Override
    public <T> T queryForObject(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws DataAccessException {
        return null;
    }

    @Override
    public Map<String, Object> queryForMap(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return null;
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return null;
    }

    @Override
    public long queryForLong(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return 0;
    }

    @Override
    public long queryForLong(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return 0;
    }

    @Override
    public int queryForInt(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return 0;
    }

    @Override
    public int queryForInt(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return 0;
    }

    @Override
    public <T> List<T> queryForList(String sql, SqlParameterSource paramSource, Class<T> elementType) throws DataAccessException {
        return null;
    }

    @Override
    public <T> List<T> queryForList(String sql, Map<String, ?> paramMap, Class<T> elementType) throws DataAccessException {
        return null;
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return null;
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return null;
    }

    @Override
    public SqlRowSet queryForRowSet(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return null;
    }

    @Override
    public SqlRowSet queryForRowSet(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return null;
    }

    @Override
    public int update(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return 0;
    }

    @Override
    public int update(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return 0;
    }

    @Override
    public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder) throws DataAccessException {
        return 0;
    }

    @Override
    public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder, String[] keyColumnNames) throws DataAccessException {
        return 0;
    }

    @Override
    public int[] batchUpdate(String sql, Map<String, ?>[] batchValues) {
        return new int[0];
    }

    @Override
    public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) {
        return new int[0];
    }
}
