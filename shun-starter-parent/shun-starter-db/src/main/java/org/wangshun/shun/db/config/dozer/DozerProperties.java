/*
 * Copyright 2005-2018 Dozer Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.wangshun.shun.db.config.dozer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Configuration properties for Dozer.
 */
@ConfigurationProperties(prefix = "dozer")
public class DozerProperties {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    /**
     * Mapping files configuration. For example "classpath:*.dozer.xml".
     */
    private String[] mappingFiles = new String[] {};

    /**
     * Mapping files configuration.
     *
     * @return mapping files
     */
    public String[] getMappingFiles() {
        return Arrays.copyOf(mappingFiles, mappingFiles.length);
    }

    /**
     * Set mapping files configuration. For example <code>classpath:*.dozer.xml</code>.
     *
     * @param mappingFiles dozer mapping files
     * @return dozer properties
     */
    public DozerProperties setMappingFiles(String[] mappingFiles) {
        this.mappingFiles = Arrays.copyOf(mappingFiles, mappingFiles.length);
        return this;
    }

    public Resource[] resolveMapperLocations() {
        return Stream.of(Optional.ofNullable(this.mappingFiles).orElse(new String[0])).flatMap(location -> Stream.of(getResources(location))).toArray(Resource[]::new);
    }

    private Resource[] getResources(String location) {
        try {
            return resourceResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }
}
