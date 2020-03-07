package org.wangshun.shun.core.entity.impl;

import org.wangshun.shun.core.entity.ISecurityCurdEntity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

public abstract class BaseSecurityCurdEntity<T extends BaseSecurityCurdEntity<T>> extends BaseCurdEntity<T>
    implements ISecurityCurdEntity<T> {
    protected static final long serialVersionUID = 1L;// serialVersionUID

    @TableField(fill = FieldFill.INSERT)
    protected String createUser;// 创建人名称

    @TableField(fill = FieldFill.UPDATE)
    protected String updateUser;// 更新人名称

    @Override
    public String getCreateUser() {
        return createUser;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setCreateUser(String createUser) {
        this.createUser = createUser;
        return (T)this;
    }

    @Override
    public String getUpdateUser() {
        return updateUser;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return (T)this;
    }

}
