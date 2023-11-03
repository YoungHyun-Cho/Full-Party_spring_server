package com.full_party.auth.service;

import com.full_party.auth.jwt.JwtTokenizer;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;

    public AuthService(JwtTokenizer jwtTokenizer, UserService userService) {
        this.jwtTokenizer = jwtTokenizer;
        this.userService = userService;
    }

    public String reIssueAccessToken(String refreshToken) {

        // refresh token 확인하고,
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(refreshToken, base64EncodedSecretKey).getBody();

        // 맞으면 Accesstoken 재발급
        User user = userService.findUser((String) claims.get("sub"));
        return createAccessToken(user);

    }

    public ResponseCookie createCookie(String name, String value, Integer maxAge) {

        return ResponseCookie.from(name, value)
                .domain("localhost")
                .path("/")
                .sameSite("None")
                .maxAge(Duration.ofMinutes(maxAge).getSeconds())
                .secure(true)
                .build();
    }

    private String createAccessToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getEmail());

        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration();
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }
}
