package org.wangshun.shun.core.entity.impl;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

public abstract class BaseOAuthCurdEntity<T extends BaseOAuthCurdEntity<T>> extends BaseSecurityCurdEntity<T> {
    /** serialVersionUID */
    protected static final long serialVersionUID = 1L;

    /** 创建应用 */
    @TableField(fill = FieldFill.INSERT)
    protected String createClient;

    /** 更新应用 */
    @TableField(fill = FieldFill.UPDATE)
    protected String updateClient;

    public String getCreateClient() {
        return createClient;
    }

    @SuppressWarnings("unchecked")
    public T setCreateClient(String createClient) {
        this.createClient = createClient;
        return (T)this;
    }



    public String getUpdateClient() {
        return updateClient;
    }

    @SuppressWarnings("unchecked")
    public T setUpdateClient(String updateClient) {
        this.updateClient = updateClient;
        return (T)this;
    }


}
