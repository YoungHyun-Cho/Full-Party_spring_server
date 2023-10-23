package com.full_party.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.full_party.auth.dto.AuthDto;
import com.full_party.auth.jwt.JwtTokenizer;
import com.full_party.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.HeaderUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();
        AuthDto authDto = objectMapper.readValue(request.getInputStream(), AuthDto.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();

        String accessToken = delegateAccessToken(user);
        String refreshToken = delegateRefreshToken(user);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);
//        response.addCookie(createCookie("token", accessToken, 60 * 60));
//        response.addCookie(createCookie("refresh", refreshToken, 60 * 60 * 3));

        response.addHeader("Set-Cookie", createCookie("token", accessToken, 60 * 60).toString());
        response.addHeader("Set-Cookie", createCookie("refresh", refreshToken, 60 * 60 * 3).toString());

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);

//        SecurityContextHolder.getContext().setAuthentication(authResult); // SecurityContextHolder에는 UserDetail이 잘 들어감.

        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private ResponseCookie createCookie(String name, String value, Integer maxAge) {

//        Cookie cookie = new Cookie(name, value);
//        cookie.setDomain("localhost");
//        cookie.setPath("/");
//        cookie.setMaxAge(maxAge);
//        cookie.setHttpOnly(true);

        return ResponseCookie.from(name, value)
                .domain("localhost")
                .path("/")
                .sameSite("None")
                .maxAge(maxAge)
                .secure(true)
                .build();

//        return cookie;

        // 🟥 HTTPS 적용 고려해보자... 어차피 해야 하니까
    }

    private String delegateAccessToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getEmail());
        claims.put("roles", user.getRoles());

        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    private String delegateRefreshToken(User user) {

        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }
}
