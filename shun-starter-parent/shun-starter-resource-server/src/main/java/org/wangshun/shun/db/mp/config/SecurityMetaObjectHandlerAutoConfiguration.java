package org.wangshun.shun.db.mp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wangshun.shun.db.mp.objecthandler.SecurityMetaObjectHandler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

@Configuration
public class SecurityMetaObjectHandlerAutoConfiguration {
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new SecurityMetaObjectHandler();
    }
}
