package org.wangshun.shun.db.mp.objecthandler;

import java.time.Instant;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.wangshun.shun.security.util.SecurityUtils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import lombok.SneakyThrows;

public class OAuthMetaObjectHandler implements MetaObjectHandler {

    private final String createUser = "createUser";
    private final String createClient = "createClient";
    private final String updateUser = "updateUser";
    private final String updateClient = "updateClient";
    private final String createTime = "createTime";
    private final String updateTime = "updateTime";
    private final String disabled = "disabled";

    @Override
    @SneakyThrows
    public void insertFill(MetaObject metaObject) {
        Instant now = Instant.now();
        this.strictInsertFill(metaObject, createTime, Instant.class, now);
        this.strictInsertFill(metaObject, updateTime, Instant.class, now);
        JwtAuthenticationToken authentication = SecurityUtils.getJwtToken();
        if (null != authentication) {
            this.strictInsertFill(metaObject, createUser, String.class, SecurityUtils.getUsername(authentication));
            this.strictInsertFill(metaObject, createClient, String.class, SecurityUtils.getClientId(authentication));
        } else {
            this.strictInsertFill(metaObject, createUser, String.class, "");
            this.strictInsertFill(metaObject, createClient, String.class, "");
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, updateTime, Instant.class, Instant.now());

        JwtAuthenticationToken authentication = SecurityUtils.getJwtToken();
        if (null != authentication) {
            this.strictUpdateFill(metaObject, updateUser, String.class, SecurityUtils.getUsername(authentication));
            this.strictUpdateFill(metaObject, updateClient, String.class, SecurityUtils.getClientId(authentication));
        } else {
            this.strictUpdateFill(metaObject, updateUser, String.class, "");
            this.strictUpdateFill(metaObject, updateClient, String.class, "");
        }
    }
}
