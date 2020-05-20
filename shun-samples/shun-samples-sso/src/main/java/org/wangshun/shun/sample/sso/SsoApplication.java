package org.wangshun.shun.sample.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Provider;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Registration;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wangshun.shun.core.http.R;
import org.wangshun.shun.core.util.SpringUtils;
import org.wangshun.shun.sso.entity.LoginProvider;

import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class SsoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoApplication.class, args);
    }

    @GetMapping(value = "/test")
    public String test() {
        return "ok";
    }

    @GetMapping(value = "/userinfo")
    public Authentication userinfo(Authentication authentication) {
        return authentication;
    }

    @GetMapping(value = "/bean")
    public Object bean(String beanId) {
        Object bean = SpringUtils.getBean(beanId);
        return bean;
    }

    @GetMapping(value = "/beanByClassName")
    public Object beanClass(String beanClass) throws ClassNotFoundException {
        Object bean = SpringUtils.getBean(Class.forName(beanClass));
        return bean;
    }

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @GetMapping(value = "/loginLinks")
    private R loginLinks() {
        Map<String, Provider> providers = oAuth2ClientProperties.getProvider();
        return R.success(oAuth2ClientProperties.getRegistration().entrySet().stream().map(e -> {
            String name = e.getKey();
            Provider provider = providers.get(name);
            Registration registration = e.getValue();
            StringBuilder sb = new StringBuilder(provider.getAuthorizationUri());
            sb.append("?response_type=code");
            sb.append("&client_id=").append(registration.getClientId());
            sb.append("&scope=").append(registration.getScope().iterator().next());
            sb.append("&redirect_uri=").append(registration.getRedirectUri());
            return new LoginProvider(name, registration.getClientName(), sb.toString());
        }).collect(Collectors.toList()));
    }
}
