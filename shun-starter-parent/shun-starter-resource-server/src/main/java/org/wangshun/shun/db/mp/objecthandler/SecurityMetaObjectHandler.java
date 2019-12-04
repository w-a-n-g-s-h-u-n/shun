package org.wangshun.shun.db.mp.objecthandler;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.wangshun.shun.security.util.SecurityUtils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

public class SecurityMetaObjectHandler implements MetaObjectHandler {

    private final String createUser = "createUser";
    private final String createClient = "createClient";
    private final String updateUser = "updateUser";
    private final String updateClient = "updateClient";
    private final String createTime = "createTime";
    private final String updateTime = "updateTime";
    private final String disabled = "disabled";

    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        this.setInsertFieldValByName(createTime, date, metaObject);
        this.setInsertFieldValByName(updateTime, date, metaObject);
        try {
            if (null == metaObject.getValue(disabled)) {
                this.setInsertFieldValByName(disabled, 0, metaObject);
            }
        } catch (ReflectionException e) {
            // Nothing
        }

        JwtAuthenticationToken authentication = SecurityUtils.getJwtToken();
        if (null != authentication) {
            this.setInsertFieldValByName(createUser, SecurityUtils.getUsername(authentication), metaObject);
            this.setInsertFieldValByName(createClient, SecurityUtils.getClientId(authentication), metaObject);
        } else {
            this.setInsertFieldValByName(createUser, "", metaObject);
            this.setInsertFieldValByName(createClient, "", metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setUpdateFieldValByName(updateTime, new Date(), metaObject);

        JwtAuthenticationToken authentication = SecurityUtils.getJwtToken();
        if (null != authentication) {
            this.setUpdateFieldValByName(updateUser, SecurityUtils.getUsername(authentication), metaObject);
            this.setUpdateFieldValByName(updateClient, SecurityUtils.getClientId(authentication), metaObject);
        } else {
            this.setUpdateFieldValByName(updateUser, "", metaObject);
            this.setUpdateFieldValByName(updateClient, "", metaObject);
        }

    }
}
