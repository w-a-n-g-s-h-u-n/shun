package org.wangshun.shun.cloud.rest;

import java.io.IOException;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LbRestTemplateAutoConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate lbRestTemplate(RestTemplateBuilder builder, HttpMessageConverters httpMessageConverters) {
        return builder.errorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != HttpStatus.BAD_REQUEST.value()) {
                    super.handleError(response);
                }
            }
        }).additionalMessageConverters(httpMessageConverters.getConverters()).build();
    }
}
