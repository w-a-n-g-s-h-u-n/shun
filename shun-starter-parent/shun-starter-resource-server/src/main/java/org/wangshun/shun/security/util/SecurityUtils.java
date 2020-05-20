package org.wangshun.shun.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * 安全工具类
 */
public class SecurityUtils {
    static final String BEARER = "Bearer ";

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static JwtAuthenticationToken getJwtToken() {
        return getJwtToken(getAuthentication());
    }

    public static JwtAuthenticationToken getJwtToken(Authentication authentication) {
        if (null == authentication) {
            return null;
        }
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            return jwtAuthenticationToken;
        }
        return null;
    }

    /**
     * 获取ClientId
     */
    public static String getClientId() {
        return getClientId(getJwtToken());
    }

    /**
     * 获取ClientId
     */
    public static String getClientId(Authentication authentication) {
        return getClientId(getJwtToken(authentication));
    }

    /**
     * 获取ClientId
     */
    public static String getClientId(JwtAuthenticationToken jwtAuthenticationToken) {
        return (String) jwtAuthenticationToken.getTokenAttributes().getOrDefault("client_id", "");
    }

    /**
     * 获取username
     */
    public static String getUsername() {
        return getUsername(getAuthentication());
    }

    /**
     * 获取username
     */
    public static String getUsername(Authentication authentication) {
        return authentication.getName();
    }

    /**
     * 获取username
     */
    public static String getUsername(JwtAuthenticationToken jwtAuthenticationToken) {
        return jwtAuthenticationToken.getName();
    }

    /**
     * 获取jwt
     */
    public static Jwt getJwt() {
        return getJwt(getJwtToken());
    }

    /**
     * 获取jwt
     */
    public static Jwt getJwt(Authentication authentication) {
        return getJwt(getJwtToken(authentication));
    }

    /**
     * 获取jwt
     */
    public static Jwt getJwt(JwtAuthenticationToken jwtAuthenticationToken) {
        return jwtAuthenticationToken.getToken();
    }

    /**
     * 获取token
     */
    public static String getToken() {
        return getJwt().getTokenValue();
    }

    /**
     * 获取token
     */
    public static String getToken(Authentication authentication) {
        return getJwt(authentication).getTokenValue();
    }

    /**
     * 获取token
     */
    public static String getToken(JwtAuthenticationToken jwtAuthenticationToken) {
        return getJwt(jwtAuthenticationToken).getTokenValue();
    }

    /**
     * 获取Authorization header
     */
    public static String getHeader() {
        return BEARER + getToken();
    }

    /**
     * 获取Authorization header
     */
    public static String getHeader(Authentication authentication) {
        return BEARER + getToken(authentication);
    }

    /**
     * 获取Authorization header
     */
    public static String getHeader(JwtAuthenticationToken jwtAuthenticationToken) {
        return BEARER + getToken(jwtAuthenticationToken);
    }

}
