package org.wangshun.shun.core.entity.impl;

import java.time.Instant;

import org.wangshun.shun.core.entity.ICurdEntity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

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

}
