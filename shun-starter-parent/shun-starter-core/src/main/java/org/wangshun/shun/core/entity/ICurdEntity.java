package org.wangshun.shun.core.entity;

import java.time.Instant;

public interface ICurdEntity<T extends ICurdEntity<T>> extends IDatabaseEntity<T> {

    Instant getCreateTime();

    T setCreateTime(Instant createTime);

    Instant getUpdateTime();

    T setUpdateTime(Instant updateTime);

}
