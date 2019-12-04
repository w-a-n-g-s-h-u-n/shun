package org.wangshun.shun.cloud.feign.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wangshun.shun.cloud.feign.decoder.RResponseEntityDecoder;

import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;

@Configuration
public class FeignClientsConfiguration {
    @Bean
    public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new OptionalDecoder(new RResponseEntityDecoder(new SpringDecoder(messageConverters)));
    }
}
