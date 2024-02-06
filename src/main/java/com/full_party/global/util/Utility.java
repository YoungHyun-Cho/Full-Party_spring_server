package com.full_party.global.util;

import com.full_party.domain.auth.userdetails.UserDetail;
import com.full_party.global.config.EnvConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class Utility {

    private static Integer accessTokenExpirationMinutes;
    private static Integer refreshTokenExpirationMinutes;

    @Value("${jwt.access-token-expiration-minutes}")
    public static void setAccessTokenExpirationMinutes(Integer accessTokenExpirationMinutes) {
        Utility.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
    }

    @Value("${jwt.access-token-expiration-minutes}")
    public static void setRefreshTokenExpirationMinutes(Integer refreshTokenExpirationMinutes) {
        Utility.refreshTokenExpirationMinutes = refreshTokenExpirationMinutes;
    }

    public static Long getUserId(UserDetails userDetails) {
        return ((UserDetail) userDetails).getId();
    }

    public static ResponseCookie createCookie(String name, String value, Integer minutes) {

        return ResponseCookie.from(name, value)
                .domain(EnvConfiguration.getDomain())
                .path("/")
                .sameSite("Lax")
                .maxAge(minutes * 60)
                .build();
    }

    public static HttpHeaders setCookie(String accessToken, String refreshToken) {

        HttpHeaders headers = new HttpHeaders();
        ResponseCookie accessTokenCookie = createCookie("token", accessToken, accessTokenExpirationMinutes);
        ResponseCookie refreshTokenCookie = createCookie("refresh", refreshToken, refreshTokenExpirationMinutes);
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return headers;
    }
}
