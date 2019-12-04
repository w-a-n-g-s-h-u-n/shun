package org.wangshun.shun.core.http;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    @ConditionalOnBean(HttpMessageConverters.class)
    public RestTemplate restTemplate(RestTemplateBuilder builder, HttpMessageConverters httpMessageConverters) {
        return builder.additionalMessageConverters(httpMessageConverters.getConverters()).build();
    }

    @Bean
    public RestTemplate simpleRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
