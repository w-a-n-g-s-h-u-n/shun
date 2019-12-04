package org.wangshun.shun.db.config.mp.handler.object;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import lombok.SneakyThrows;

public class DefaultMetaObjectHandler implements MetaObjectHandler {
    private final String createTime = "createTime";
    private final String updateTime = "updateTime";
    private final String disabled = "disabled";

    @Override
    @SneakyThrows
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        this.setInsertFieldValByName(createTime, date, metaObject);
        this.setInsertFieldValByName(updateTime, date, metaObject);
        if (null == metaObject.getValue(disabled)) {
            this.setInsertFieldValByName(disabled, 0, metaObject);
        }
    }

    @Override
    @SneakyThrows
    public void updateFill(MetaObject metaObject) {
        this.setUpdateFieldValByName(updateTime, new Date(), metaObject);
    }

}
