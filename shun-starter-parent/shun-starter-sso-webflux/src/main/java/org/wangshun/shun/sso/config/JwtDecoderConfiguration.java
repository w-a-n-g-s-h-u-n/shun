package org.wangshun.shun.sso.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.wangshun.shun.sso.jwt.NimbusReactiveJwtDecoder;
import org.wangshun.shun.sso.jwt.OAuth2ClientJwtDecoders;

@Configuration
@AutoConfigureAfter(WebClientConfiguration.class)
public class JwtDecoderConfiguration {

    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
    public ReactiveJwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties, WebClient lbWebClient) {
        return new NimbusReactiveJwtDecoder(properties.getJwt().getJwkSetUri(), lbWebClient);
    }

    @Bean
    public OAuth2ClientJwtDecoders jwtDecoders(OAuth2ClientProperties properties, WebClient lbWebClient) {
        OAuth2ClientJwtDecoders jwtDecoders = new OAuth2ClientJwtDecoders();
        properties.getProvider().forEach((key, provider) -> {
            jwtDecoders.put(provider.getJwkSetUri(), new NimbusReactiveJwtDecoder(provider.getJwkSetUri(), lbWebClient));
        });
        return jwtDecoders;
    }

}
