package com.full_party.util;

import com.full_party.auth.userdetails.UserDetail;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

public class Utility {

//    private static final String DOMAIN = "localhost";
    private static final String DOMAIN = "fullpartyspring.com";

    public static Long getUserId(UserDetails userDetails) {
        return ((UserDetail) userDetails).getId();
    }

    public static ResponseCookie createCookie(String name, String value, Integer maxAge) {

        return ResponseCookie.from(name, value)
                .domain(DOMAIN)
                .path("/")
                .sameSite("None")
                .maxAge(Duration.ofMinutes(maxAge).getSeconds())
                .secure(true)
                .build();
    }

    public static ResponseCookie createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .domain(DOMAIN)
                .path("/")
                .sameSite("None")
                .secure(true)
                .build();
    }
}
