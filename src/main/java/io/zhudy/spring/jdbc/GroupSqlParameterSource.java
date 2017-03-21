package io.zhudy.spring.jdbc;

import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public class GroupSqlParameterSource extends AbstractSqlParameterSource {

    private final Map<String, Object> values = new LinkedHashMap<>();

    private SqlParameterSource sqlParameterSource;

    /**
     * @param sqlParameterSource
     */
    public GroupSqlParameterSource(SqlParameterSource sqlParameterSource) {
        Assert.notNull(sqlParameterSource);
        this.sqlParameterSource = sqlParameterSource;
    }

    /**
     * Add a parameter to this parameter source.
     *
     * @param paramName the name of the parameter
     * @param value     the value of the parameter
     * @return a reference to this parameter source,
     * so it's possible to chain several calls together
     */
    public GroupSqlParameterSource addValue(String paramName, Object value) {
        Assert.notNull(paramName, "Parameter name must not be null");
        this.values.put(paramName, value);
        if (value instanceof SqlParameterValue) {
            registerSqlType(paramName, ((SqlParameterValue) value).getSqlType());
        }
        return this;
    }

    /**
     * Add a parameter to this parameter source.
     *
     * @param paramName the name of the parameter
     * @param value     the value of the parameter
     * @param sqlType   the SQL type of the parameter
     * @return a reference to this parameter source,
     * so it's possible to chain several calls together
     */
    public GroupSqlParameterSource addValue(String paramName, Object value, int sqlType) {
        Assert.notNull(paramName, "Parameter name must not be null");
        this.values.put(paramName, value);
        registerSqlType(paramName, sqlType);
        return this;
    }

    /**
     * Add a parameter to this parameter source.
     *
     * @param paramName the name of the parameter
     * @param value     the value of the parameter
     * @param sqlType   the SQL type of the parameter
     * @param typeName  the type name of the parameter
     * @return a reference to this parameter source,
     * so it's possible to chain several calls together
     */
    public GroupSqlParameterSource addValue(String paramName, Object value, int sqlType, String typeName) {
        Assert.notNull(paramName, "Parameter name must not be null");
        this.values.put(paramName, value);
        registerSqlType(paramName, sqlType);
        registerTypeName(paramName, typeName);
        return this;
    }

    @Override
    public boolean hasValue(String paramName) {
        boolean r = values.containsKey(paramName);
        if (r) {
            return true;
        }
        return sqlParameterSource.hasValue(paramName);
    }

    @Override
    public Object getValue(String paramName) throws IllegalArgumentException {
        Object r = values.get(paramName);
        if (r != null) {
            return r;
        }
        return sqlParameterSource.getValue(paramName);
    }
}
