package org.wangshun.shun.db.base.entity;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.wangshun.shun.core.entity.IDatabaseEntity;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;

public abstract class BaseDatabaseEntity<T extends BaseDatabaseEntity<T>> implements IDatabaseEntity<T> {
    protected static final long serialVersionUID = 1L;// serialVersionUID

    protected Long id;// ID

    @Override
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setId(Long id) {
        this.id = id;
        return (T)this;
    }

    public abstract static class BaseDatabaseEntityTypeHandler<T extends BaseDatabaseEntity<T>>
        extends BaseTypeHandler<T> {

        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
            throws SQLException {
            Long id = parameter.getId();
            if (null == id) {
                ps.setNull(i, JdbcType.BIGINT.TYPE_CODE);
            } else {
                ps.setLong(i, id);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
            return ReflectUtil.newInstance((Class<T>)TypeUtil.getClass(getRawType()), rs.getLong(columnName));
        }

        @SuppressWarnings("unchecked")
        @Override
        public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            return ReflectUtil.newInstance((Class<T>)TypeUtil.getClass(getRawType()), rs.getLong(columnIndex));
        }

        @SuppressWarnings("unchecked")
        @Override
        public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            return ReflectUtil.newInstance((Class<T>)TypeUtil.getClass(getRawType()), cs.getLong(columnIndex));
        }
    }
}
