package org.wangshun.shun.db.config.dozer;

import java.io.IOException;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.spring.DozerBeanMapperFactoryBean;

/**
 * Dozer spring auto configuration.
 */
@Configuration
@AutoConfigureBefore(com.github.dozermapper.springboot.autoconfigure.DozerAutoConfiguration.class)
@ConditionalOnClass({DozerBeanMapperFactoryBean.class, Mapper.class})
@ConditionalOnMissingBean(Mapper.class)
@EnableConfigurationProperties(DozerProperties.class)
public class DozerAutoConfiguration {

    private final DozerProperties properties;

    /**
     * Constructor for creating auto configuration.
     *
     * @param properties properties
     */
    public DozerAutoConfiguration(DozerProperties properties) {
        this.properties = properties;
    }

    /**
     * Creates default Dozer mapper
     *
     * @return Dozer mapper
     * @throws IOException if there is an exception during initialization.
     */
    @Bean
    public DozerBeanMapperFactoryBean dozerMapper() throws IOException {
        DozerBeanMapperFactoryBean factoryBean = new DozerBeanMapperFactoryBean();
        factoryBean.setMappingFiles(properties.resolveMapperLocations());
        return factoryBean;
    }

}
