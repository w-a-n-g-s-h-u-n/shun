package org.wangshun.shun.sso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;
import org.wangshun.shun.core.rest.RestTemplateAutoConfiguration;
import org.wangshun.shun.sso.jwt.OAuth2ClientJwtDecoders;

@Configuration
@AutoConfigureAfter(RestTemplateAutoConfiguration.class)
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
        return createJwtDecoder(properties.getJwt().getJwkSetUri());
    }

    @Bean
    public OAuth2ClientJwtDecoders jwtDecoders(OAuth2ClientProperties properties) {
        OAuth2ClientJwtDecoders jwtDecoders = new OAuth2ClientJwtDecoders();
        properties.getProvider().forEach((key, provider) -> {
            String jwkSetUri = provider.getJwkSetUri();
            jwtDecoders.put(jwkSetUri, createJwtDecoder(jwkSetUri));
        });
        return jwtDecoders;
    }

    private NimbusJwtDecoder createJwtDecoder(String jwkSetUri) {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
            .restOperations(loadBalanced ? lbRestTemplate : builder.build()).build();
    }
}
