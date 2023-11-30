package com.full_party.util;

import com.full_party.auth.userdetails.UserDetail;
import com.full_party.config.EnvConfiguration;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;

public class Utility {

//    private static final String DOMAIN = "localhost";
//    private static final String DOMAIN = ".fullpartyspring.com";

    public static Long getUserId(UserDetails userDetails) {
        return ((UserDetail) userDetails).getId();
    }

    public static ResponseCookie createCookie(String name, String value, Integer minutes) {

        return ResponseCookie.from(name, value)
//                .domain(DOMAIN)
                .domain(EnvConfiguration.getValues().getDomain())
                .path("/")
                .sameSite("Lax")
                .maxAge(minutes * 60)
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
