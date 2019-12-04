package org.wangshun.shun.cloud.rest;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LbRestTemplateAutoConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate lbRestTemplate(RestTemplateBuilder builder, HttpMessageConverters httpMessageConverters) {
        return builder.additionalMessageConverters(httpMessageConverters.getConverters()).build();
    }
}
