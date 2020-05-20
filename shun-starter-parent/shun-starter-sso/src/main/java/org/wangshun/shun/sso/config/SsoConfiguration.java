package org.wangshun.shun.sso.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsUtils;
import org.wangshun.shun.sso.jwt.JwtOAuth2UserService;
import org.wangshun.shun.sso.jwt.OAuth2ClientJwtDecoders;

@Configuration
@EnableWebSecurity
@ConditionalOnMissingBean({SecurityWebFilterChain.class, WebSecurityConfigurerAdapter.class})
@AllArgsConstructor
public class SsoConfiguration extends WebSecurityConfigurerAdapter {
    private final OAuth2ClientJwtDecoders jwtDecoders;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
        .authorizeRequests()
            .mvcMatchers("/error","/actuator/**")
                .permitAll()
            .requestMatchers(CorsUtils::isPreFlightRequest)
                .permitAll()
            .anyRequest()
                .authenticated()
            .and()
        .cors()
            .and()
        .csrf()
            .and()
        .headers()
            .cacheControl()
                .disable()
            .frameOptions()
                .disable()
            .and()
        .oauth2Login()
            .userInfoEndpoint()
                .userService(new JwtOAuth2UserService(jwtDecoders))
        ;
        // @formatter:on
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        final String[] staticPath = {"/static/**", "/resources/**", "/public/**", "/page/**", "/**/*.html", "/**/*.js", "/**/*.css", "/**/*.png", "/**/*.gif", "/**/*.jpg", "/**/*.svg", "/**/*.woff", "/**/*.ttf", "/**/*.pdf", "/**/*.ico"};
        web.ignoring().mvcMatchers(staticPath);
    }
}
