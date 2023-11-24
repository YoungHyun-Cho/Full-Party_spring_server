package com.full_party.util;

import com.full_party.auth.userdetails.UserDetail;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

public class Utility {

    private static final String DOMAIN = "localhost";
//    private static final String DOMAIN = "fullpartyspring.com";

    public static Long getUserId(UserDetails userDetails) {
        return ((UserDetail) userDetails).getId();
    }

    public static ResponseCookie createCookie(String name, String value, Integer maxAge) {

        return ResponseCookie.from(name, value)
                .domain(DOMAIN)
                .path("/")
//                .sameSite("None")
                .sameSite("Lax")
                .maxAge(Duration.ofMinutes(maxAge).getSeconds())
                .secure(true)
                .build();
    }

    public static ResponseCookie createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .domain(DOMAIN)
                .path("/")
//                .sameSite("None")
                .sameSite("Lax")
                .secure(true)
                .build();
    }

    public static HttpHeaders setCookie(String accessToken, String refreshToken) {

        HttpHeaders headers = new HttpHeaders();
        ResponseCookie accessTokenCookie = Utility.createCookie("token", accessToken, 10);
        ResponseCookie refreshTokenCookie = Utility.createCookie("refresh", refreshToken, 60);
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return headers;
    }
}
