package com.full_party.auth.service;

import com.full_party.auth.jwt.JwtTokenizer;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;

    public AuthService(UserService userService, JwtTokenizer jwtTokenizer) {
        this.userService = userService;
        this.jwtTokenizer = jwtTokenizer;
    }

    public Map<String, String> reIssueToken(String refreshToken) throws ExpiredJwtException, MalformedJwtException {

        Map<String, String> tokenMap = new HashMap<>();

        // refresh token 확인하고,
        Map<String, Object> claims = jwtTokenizer.getClaims(refreshToken).getBody();

        Set<Map.Entry<String, Object>> entrySet = claims.entrySet();
        entrySet.stream().forEach(entry -> System.out.println("🟦 " + entry.getKey() + " : " + entry.getValue()));

//        Jws<Claims> claims = jwtTokenizer.getClaims(refreshToken, base64EncodedSecretKey);

        // 맞으면 token 재발급
        User user = userService.findUser((String) claims.get("sub"));
        tokenMap.put("accessToken", jwtTokenizer.delegateAccessToken(user));
        tokenMap.put("refreshToken", jwtTokenizer.delegateRefreshToken(user));

        return tokenMap;
    }
}
