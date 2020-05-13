package org.wangshun.shun.web.config.web;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.wangshun.shun.core.prop.CorsEndpointProperties;
import org.wangshun.shun.core.rest.RestTemplateAutoConfiguration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@ConditionalOnClass(WebMvcAutoConfiguration.class)
@AutoConfigureBefore(RestTemplateAutoConfiguration.class)
@AllArgsConstructor
public class MvcAutoConfiguration implements WebMvcConfigurer {

    /**
     * 设置 跨域请求参数，处理跨域请求
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.web", name = "cors", matchIfMissing = true)
    public CorsFilter corsFilter(CorsEndpointProperties corsProperties) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsProperties.toCorsConfiguration());
        return new CorsFilter(source);
    }

    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(source)), ZoneId.systemDefault())
                    .toLocalDate();
            }
        };
    }

    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(source)), ZoneId.systemDefault());
            }
        };
    }

    @Bean
    public Converter<String, Instant> instantConverter() {
        return new Converter<String, Instant>() {
            @Override
            public Instant convert(String source) {
                return Instant.ofEpochMilli(Long.valueOf(source));
            }
        };
    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter(FastJsonConfig fastJsonConfig) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter() {
            @Override
            public boolean supports(Class<?> clazz) {
                if (clazz == null) {
                    return false;
                }
                if (Map.class.equals(clazz)) {
                    return false;
                }
                if (hasJsonSerialize(clazz)) {
                    return false;
                }
                return super.supports(clazz);
            }

            private boolean hasJsonSerialize(Class<?> clazz) {
                Class<?>[] interfaces = clazz.getInterfaces();
                if (interfaces != null) {
                    for (Class<?> inte : interfaces) {
                        if (hasJsonSerialize(inte)) {
                            return true;
                        }
                    }
                }
                return clazz.getAnnotation(JsonSerialize.class) != null;
            }
        };
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.parseMediaType("text/json"));
        mediaTypes.add(MediaType.parseMediaType("text/json;charset=UTF-8"));
        converter.setSupportedMediaTypes(mediaTypes);

        converter.setFastJsonConfig(fastJsonConfig);
        return converter;
    }
}
