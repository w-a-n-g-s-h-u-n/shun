package org.wangshun.shun.db.mp.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wangshun.shun.db.mp.objecthandler.OAuthMetaObjectHandler;

@Configuration
public class OAuthMetaObjectHandlerAutoConfiguration {
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new OAuthMetaObjectHandler();
    }
}
