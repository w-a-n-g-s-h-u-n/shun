package org.wangshun.shun.sso.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class WebClientConfiguration {


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty("security.oauth2.resource.loadBalanced")
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @ConditionalOnProperty("security.oauth2.resource.loadBalanced")
    public WebClient lbWebClient() {
        return loadBalancedWebClientBuilder().build();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {
        return webClientBuilder().build();
    }
}
