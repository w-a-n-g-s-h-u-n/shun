package org.wangshun.shun.core.entity.impl;

import org.wangshun.shun.core.entity.IDatabaseEntity;

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

}
