package org.wangshun.shun.db.config.mp.handler.object;

import java.time.Instant;

import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import lombok.SneakyThrows;

public class DefaultMetaObjectHandler implements MetaObjectHandler {
    private final String createTime = "createTime";
    private final String updateTime = "updateTime";

    @Override
    @SneakyThrows
    public void insertFill(MetaObject metaObject) {
        Instant now = Instant.now();
        this.strictInsertFill(metaObject, createTime, Instant.class, now);
        this.strictInsertFill(metaObject, updateTime, Instant.class, now);
    }

    @Override
    @SneakyThrows
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, updateTime, Instant.class, Instant.now());
    }

}
