package org.wangshun.shun.sso.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginReactiveAuthenticationManager;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import org.wangshun.shun.sso.jwt.JwtReactiveOAuth2UserService;
import org.wangshun.shun.sso.jwt.OAuth2ClientJwtDecoders;
import org.wangshun.shun.sso.jwt.WebClientReactiveAuthorizationCodeTokenResponseClient;

import lombok.AllArgsConstructor;

@Configuration
@AutoConfigureAfter(JwtDecoderConfiguration.class)
@AllArgsConstructor
public class WebSecurityConfiguration {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, OAuth2ClientJwtDecoders jwtDecoders, ReactiveJwtDecoder jwtDecoder, WebClient webClient) {
        // @formatter:off
        return getSsoConfiguration(http, jwtDecoders, webClient)
        .oauth2ResourceServer()
            .jwt()
                .jwtDecoder(jwtDecoder)
                .and()
            .and()
        .build()
        ;
        // @formatter:on
    }

    @Bean
    SecurityWebFilterChain ssoSecurityFilterChain(ServerHttpSecurity http, OAuth2ClientJwtDecoders jwtDecoders, WebClient webClient) {
        return getSsoConfiguration(http, jwtDecoders, webClient).build();
    }

    private ServerHttpSecurity getSsoConfiguration(ServerHttpSecurity http, OAuth2ClientJwtDecoders jwtDecoders, WebClient webClient) {
        return
        // @formatter:off
        http.authorizeExchange()
            .pathMatchers("/error","/code","/auth/**","/login/**","/actuator/**")
                .permitAll()
            .pathMatchers(HttpMethod.OPTIONS,"/**")
                .permitAll()
            .anyExchange()
                .authenticated()
            .and()
        .cors()
            .and()
        .csrf()
            .disable()
        .headers()
            .cache()
                .disable()
            .frameOptions()
                .disable()
            .and()
        .oauth2Login()
            .authenticationManager(
                new OAuth2LoginReactiveAuthenticationManager(
                    new WebClientReactiveAuthorizationCodeTokenResponseClient(webClient),
                    new JwtReactiveOAuth2UserService(jwtDecoders)
                )
            )
            .and()
        ;
        // @formatter:on
    }
}