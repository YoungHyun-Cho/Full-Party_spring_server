package com.full_party.auth.service;

import com.full_party.auth.jwt.JwtTokenizer;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;

    public AuthService(JwtTokenizer jwtTokenizer, UserService userService) {
        this.jwtTokenizer = jwtTokenizer;
        this.userService = userService;
    }

    public Map<String, String> reIssueToken(String refreshToken) throws ExpiredJwtException {

        Map<String, String> tokenMap = new HashMap<>();

        // refresh token 확인하고,
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(refreshToken, base64EncodedSecretKey).getBody();

        Set<Map.Entry<String, Object>> entrySet = claims.entrySet();
        entrySet.stream().forEach(entry -> System.out.println("🟦 " + entry.getKey() + " : " + entry.getValue()));

//        Jws<Claims> claims = jwtTokenizer.getClaims(refreshToken, base64EncodedSecretKey);

        // 맞으면 token 재발급
        User user = userService.findUser((String) claims.get("sub"));
        tokenMap.put("accessToken", delegateAccessToken(user));
        tokenMap.put("refreshToken", delegateRefreshToken(user));

        return tokenMap;
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

    public ResponseCookie createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .domain("localhost")
                .path("/")
                .sameSite("None")
                .secure(true)
                .build();
    }

    private String delegateAccessToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getEmail());

        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getAccessTokenExpiration();
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    private String delegateRefreshToken(User user) {

        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getRefreshTokenExpiration();
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }
}
