package org.wangshun.shun.core.entity.impl;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

public abstract class BaseSecurityCurdEntity<T extends BaseSecurityCurdEntity<T>> extends BaseCurdEntity<T> {
    /** serialVersionUID */
    protected static final long serialVersionUID = 1L;

    /** 创建人名称 */
    @TableField(fill = FieldFill.INSERT)
    protected String createUser;

    /** 更新人名称 */
    @TableField(fill = FieldFill.UPDATE)
    protected String updateUser;

    public String getCreateUser() {
        return createUser;
    }

    @SuppressWarnings("unchecked")
    public T setCreateUser(String createUser) {
        this.createUser = createUser;
        return (T)this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    @SuppressWarnings("unchecked")
    public T setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return (T)this;
    }

}
