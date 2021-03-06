/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.wangshun.shun.sso.jwt;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * An implementation of an {@link OAuth2UserService} that supports standard OAuth 2.0 Provider's.
 * <p>
 * For standard OAuth 2.0 Provider's, the attribute name used to access the user's name from the UserInfo response is required and therefore must be available via {@link ClientRegistration.ProviderDetails.UserInfoEndpoint#getUserNameAttributeName() UserInfoEndpoint.getUserNameAttributeName()}.
 * <p>
 * <b>NOTE:</b> Attribute names are <b>not</b> standardized between providers and therefore will vary. Please consult the provider's API documentation for the set of supported user attribute names.
 *
 * @author Joe Grandja
 * @see OAuth2UserService
 * @see OAuth2UserRequest
 * @see OAuth2User
 * @see DefaultOAuth2User
 * @since 5.0
 */
@AllArgsConstructor
public class JwtOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";

    private final OAuth2ClientJwtDecoders jwtDecoders;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        if (!StringUtils.hasText(userNameAttributeName)) {
            OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE, "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        String accessToken = userRequest.getAccessToken().getTokenValue();
        Jwt jwt = jwtDecoders.get(userRequest.getClientRegistration().getProviderDetails().getJwkSetUri()).decode(accessToken);

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(ArrayUtil.toArray(jwt.getClaimAsStringList("authorities"), String.class));
        return new DefaultOAuth2User(authorities, jwt.getClaims(), userNameAttributeName);
    }
}
