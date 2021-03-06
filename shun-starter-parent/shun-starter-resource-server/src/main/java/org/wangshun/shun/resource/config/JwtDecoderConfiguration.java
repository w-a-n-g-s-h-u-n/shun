package org.wangshun.shun.resource.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;
import org.wangshun.shun.cloud.rest.LbRestTemplateAutoConfiguration;

@Configuration
@AutoConfigureAfter({LbRestTemplateAutoConfiguration.class})
public class JwtDecoderConfiguration {
    @Autowired
    private RestTemplate lbRestTemplate;
    @Autowired
    private RestTemplateBuilder builder;
    @Value("${security.oauth2.resource.loadBalanced:false}")
    private Boolean loadBalanced;

    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
    public JwtDecoder jwtDecoderByJwkKeySetUri(OAuth2ResourceServerProperties properties) {
        return NimbusJwtDecoder.withJwkSetUri(properties.getJwt().getJwkSetUri())
                .restOperations(loadBalanced ? lbRestTemplate : builder.build()).build();
    }

}
