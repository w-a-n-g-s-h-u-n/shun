/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package org.wangshun.shun.sso.jwt;

import java.util.List;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;

import reactor.core.publisher.Mono;

/**
 * A reactive version of {@link com.nimbusds.jose.jwk.source.JWKSource}
 * 
 * @author Rob Winch
 * @since 5.1
 */
interface ReactiveJWKSource {
    Mono<List<JWK>> get(JWKSelector jwkSelector);
}
