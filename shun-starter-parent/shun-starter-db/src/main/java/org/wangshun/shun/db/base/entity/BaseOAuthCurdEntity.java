package org.wangshun.shun.db.base.entity;

import org.wangshun.shun.core.entity.IOAuthCurdEntity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

public abstract class BaseOAuthCurdEntity<T extends BaseOAuthCurdEntity<T>> extends BaseSecurityCurdEntity<T>
    implements IOAuthCurdEntity<T> {
    protected static final long serialVersionUID = 1L;// serialVersionUID

    @TableField(fill = FieldFill.INSERT)
    protected String createClient;// 创建应用

    @TableField(fill = FieldFill.UPDATE)
    protected String updateClient;// 更新应用

    @Override
    public String getCreateClient() {
        return createClient;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setCreateClient(String createClient) {
        this.createClient = createClient;
        return (T)this;
    }



    @Override
    public String getUpdateClient() {
        return updateClient;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setUpdateClient(String updateClient) {
        this.updateClient = updateClient;
        return (T)this;
    }


}
