package org.wangshun.shun.core.entity.impl;

import java.time.Instant;

import org.wangshun.shun.core.entity.IDatabaseEntity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

public abstract class BaseCurdEntity<T extends BaseCurdEntity<T>> implements IDatabaseEntity<T> {
    /** serialVersionUID */
    protected static final long serialVersionUID = 1L;

    /** ID */
    protected Long id;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    protected Instant createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Instant updateTime;

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

    public Instant getCreateTime() {
        return createTime;
    }

    @SuppressWarnings("unchecked")
    public T setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return (T)this;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    @SuppressWarnings("unchecked")
    public T setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return (T)this;
    }

}
