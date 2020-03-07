package org.wangshun.shun.core.entity;

public interface IOAuthCurdEntity<T extends IOAuthCurdEntity<T>> extends ISecurityCurdEntity<T> {

    String getCreateClient();

    T setCreateClient(String createClient);

    String getUpdateClient();

    T setUpdateClient(String updateClient);

}
