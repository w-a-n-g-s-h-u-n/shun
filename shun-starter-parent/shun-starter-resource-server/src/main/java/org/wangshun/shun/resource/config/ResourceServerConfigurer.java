package org.wangshun.shun.resource.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
public class ResourceServerConfigurer extends WebSecurityConfigurerAdapter {

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
            .disable() // 关闭csrf攻击拦截 ，jwt不需要 。非jwt不要关闭，除非你知道自己在做什么
        .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 不创建、不使用session。前后端分离，分布式系统要做到无状态，即无session。
            .and()
        .httpBasic()
            .disable()
        .headers()
            .cacheControl()
                .disable()
            .frameOptions()
                .disable()
            .and()
        .oauth2ResourceServer()
            .jwt()
        ;
        // @formatter:on
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        final String[] staticPath = {"/static/**", "/resources/**", "/public/**", "/page/**", "/**/*.html", "/**/*.js", "/**/*.css", "/**/*.png", "/**/*.gif", "/**/*.jpg", "/**/*.svg", "/**/*.woff", "/**/*.ttf", "/**/*.pdf", "/**/*.ico"};
        web.ignoring().mvcMatchers(staticPath);
    }

}
