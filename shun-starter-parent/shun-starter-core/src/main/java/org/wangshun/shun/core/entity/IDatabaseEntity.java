package org.wangshun.shun.core.entity;

import java.io.Serializable;

public interface IDatabaseEntity<T> extends Serializable, Cloneable {

    Long getId();

    T setId(Long id);

}
