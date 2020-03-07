package org.wangshun.shun.core.entity;

public interface ISecurityCurdEntity<T extends ISecurityCurdEntity<T>> extends ICurdEntity<T> {

    String getCreateUser();

    T setCreateUser(String createUser);

    String getUpdateUser();

    T setUpdateUser(String updateUser);

}
