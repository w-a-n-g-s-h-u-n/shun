package org.wangshun.shun.sample.jwt.config;

import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.cors.CorsUtils;
import org.wangshun.shun.core.http.R;
import org.wangshun.shun.core.prop.PermitUrlProperties;
import org.wangshun.shun.sample.jwt.util.JwtHelper;

import java.io.IOException;
import java.io.Writer;

@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
public class SecurityServerConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private PermitUrlProperties permitUrlProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
        .authorizeRequests()
            .mvcMatchers(permitUrlProperties.getIgnoreUrls())
                .permitAll()
            .requestMatchers(CorsUtils::isPreFlightRequest)
                .permitAll()
            .anyRequest()
                .authenticated()
            .and()
        .cors()
            .and()
        .csrf()
            .disable() 
        .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
        .formLogin()
            .permitAll()
            .successHandler((request, response, authentication) -> {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try (Writer writer= response.getWriter()){
                    writer.write(R.success(JwtHelper.sign(authentication.getName())).toString());
                    writer.flush();
                } catch (IOException | JOSEException e) {
                }
            })
            .failureHandler((request, response, exception) -> {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try (Writer writer= response.getWriter()){
                    writer.write(R.error(exception).toString());
                    writer.flush();
                } catch (IOException e) {
                }
            })
            .and()
        .logout()
            .disable()
        .rememberMe()
            .disable()
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
                .decoder(NimbusJwtDecoder.withPublicKey(JwtHelper.getPublickey()).build())
                .and()
            .and()
        ;
        // @formatter:on
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers(permitUrlProperties.getStaticResources());
    }
}
