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

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTProcessor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import reactor.core.publisher.Mono;

/**
 * An implementation of a {@link ReactiveJwtDecoder} that &quot;decodes&quot; a JSON Web Token (JWT) and additionally verifies it's digital signature if the JWT is a JSON Web Signature (JWS). The public key used for verification is obtained from the JSON Web Key (JWK) Set {@code URL} supplied via the constructor.
 *
 * <p>
 * <b>NOTE:</b> This implementation uses the Nimbus JOSE + JWT SDK internally.
 *
 * @author Rob Winch
 * @since 5.1
 * @see ReactiveJwtDecoder
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc7519">JSON Web Token (JWT)</a>
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc7515">JSON Web Signature (JWS)</a>
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc7517">JSON Web Key (JWK)</a>
 * @see <a target="_blank" href="https://connect2id.com/products/nimbus-jose-jwt">Nimbus JOSE + JWT SDK</a>
 */
@AllArgsConstructor
@Builder
public final class NimbusReactiveJwtDecoder implements ReactiveJwtDecoder {
    private final JWTProcessor<JWKContext> jwtProcessor;

    private final ReactiveJWKSource reactiveJwkSource;

    private final JWKSelectorFactory jwkSelectorFactory;

    private OAuth2TokenValidator<Jwt> jwtValidator = JwtValidators.createDefault();

    public NimbusReactiveJwtDecoder(RSAPublicKey publicKey) {
        JWSAlgorithm algorithm = JWSAlgorithm.parse(JwsAlgorithms.RS256);

        RSAKey rsaKey = rsaKey(publicKey);
        JWKSet jwkSet = new JWKSet(rsaKey);
        JWKSource jwkSource = new ImmutableJWKSet<>(jwkSet);
        JWSKeySelector<JWKContext> jwsKeySelector = new JWSVerificationKeySelector<>(algorithm, jwkSource);
        DefaultJWTProcessor jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        jwtProcessor.setJWTClaimsSetVerifier((claims, context) -> {
        });

        this.jwtProcessor = jwtProcessor;
        this.reactiveJwkSource = new ReactiveJWKSourceAdapter(jwkSource);
        this.jwkSelectorFactory = new JWKSelectorFactory(algorithm);
    }

    /**
     * Constructs a {@code NimbusJwtDecoderJwkSupport} using the provided parameters.
     *
     * @param jwkSetUrl the JSON Web Key (JWK) Set {@code URL}
     */
    public NimbusReactiveJwtDecoder(String jwkSetUrl, WebClient webClient) {
        Assert.hasText(jwkSetUrl, "jwkSetUrl cannot be empty");
        String jwsAlgorithm = JwsAlgorithms.RS256;
        JWSAlgorithm algorithm = JWSAlgorithm.parse(jwsAlgorithm);
        JWKSource jwkSource = new JWKContextJWKSource();
        JWSKeySelector<JWKContext> jwsKeySelector = new JWSVerificationKeySelector<>(algorithm, jwkSource);

        DefaultJWTProcessor<JWKContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        jwtProcessor.setJWTClaimsSetVerifier((claims, context) -> {
        });
        this.jwtProcessor = jwtProcessor;

        this.reactiveJwkSource = new ReactiveRemoteJWKSource(jwkSetUrl, webClient);

        this.jwkSelectorFactory = new JWKSelectorFactory(algorithm);

    }

    /**
     * Use the provided {@link OAuth2TokenValidator} to validate incoming {@link Jwt}s.
     *
     * @param jwtValidator the {@link OAuth2TokenValidator} to use
     */
    public void setJwtValidator(OAuth2TokenValidator<Jwt> jwtValidator) {
        Assert.notNull(jwtValidator, "jwtValidator cannot be null");
        this.jwtValidator = jwtValidator;
    }

    @Override
    public Mono<Jwt> decode(String token) throws JwtException {
        JWT jwt = parse(token);
        if (jwt instanceof SignedJWT) {
            return this.decode((SignedJWT)jwt);
        }
        throw new JwtException("Unsupported algorithm of " + jwt.getHeader().getAlgorithm());
    }

    private JWT parse(String token) {
        try {
            return JWTParser.parse(token);
        } catch (Exception ex) {
            throw new JwtException("An error occurred while attempting to decode the Jwt: " + ex.getMessage(), ex);
        }
    }

    private Mono<Jwt> decode(SignedJWT parsedToken) {
        try {
            JWKSelector selector = this.jwkSelectorFactory.createSelector(parsedToken.getHeader());
            // @formatter:off
            return this.reactiveJwkSource.get(selector)
                .onErrorMap(e -> new IllegalStateException("Could not obtain the keys", e))
                .map(jwkList -> createClaimsSet(parsedToken, jwkList))
                .map(set -> createJwt(parsedToken, set))
                .map(this::validateJwt)
                .onErrorMap(e -> !(e instanceof IllegalStateException) && !(e instanceof JwtException), e -> new JwtException("An error occurred while attempting to decode the Jwt: ", e));
            // @formatter:on
        } catch (RuntimeException ex) {
            throw new JwtException("An error occurred while attempting to decode the Jwt: " + ex.getMessage(), ex);
        }
    }

    private JWTClaimsSet createClaimsSet(JWT parsedToken, List<JWK> jwkList) {
        try {
            return this.jwtProcessor.process(parsedToken, new JWKContext(jwkList));
        } catch (BadJOSEException | JOSEException e) {
            throw new JwtException("Failed to validate the token", e);
        }
    }

    private Jwt createJwt(JWT parsedJwt, JWTClaimsSet jwtClaimsSet) {
        Instant expiresAt = null;
        if (jwtClaimsSet.getExpirationTime() != null) {
            expiresAt = jwtClaimsSet.getExpirationTime().toInstant();
        }
        Instant issuedAt = null;
        if (jwtClaimsSet.getIssueTime() != null) {
            issuedAt = jwtClaimsSet.getIssueTime().toInstant();
        } else if (expiresAt != null) {
            // Default to expiresAt - 1 second
            issuedAt = Instant.from(expiresAt).minusSeconds(1);
        }

        Map<String, Object> headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());

        return new Jwt(parsedJwt.getParsedString(), issuedAt, expiresAt, headers, jwtClaimsSet.getClaims());
    }

    private Jwt validateJwt(Jwt jwt) {
        OAuth2TokenValidatorResult result = this.jwtValidator.validate(jwt);

        if (result.hasErrors()) {
            String message = result.getErrors().iterator().next().getDescription();
            throw new JwtValidationException(message, result.getErrors());
        }

        return jwt;
    }

    private static RSAKey rsaKey(RSAPublicKey publicKey) {
        return new RSAKey.Builder(publicKey).build();
    }

}
