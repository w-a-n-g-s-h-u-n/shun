package org.wangshun.shun.db.base.entity;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.wangshun.shun.core.entity.ICurdEntity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;

public abstract class BaseCurdEntity<T extends BaseCurdEntity<T>> extends BaseDatabaseEntity<T>
    implements ICurdEntity<T> {
    protected static final long serialVersionUID = 1L;// serialVersionUID

    @TableField(fill = FieldFill.INSERT)
    protected Instant createTime;// 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Instant updateTime;// 更新时间

    @Override
    public Instant getCreateTime() {
        return createTime;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return (T)this;
    }

    @Override
    public Instant getUpdateTime() {
        return updateTime;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return (T)this;
    }

    public abstract static class BaseCurdEntityTypeHandler<T extends BaseCurdEntity<T>> extends BaseTypeHandler<T> {

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
