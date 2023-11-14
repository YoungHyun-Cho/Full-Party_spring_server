package com.full_party.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.full_party.auth.dto.AuthDto;
import com.full_party.auth.jwt.JwtTokenizer;
import com.full_party.user.entity.User;
import com.full_party.util.Utility;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

        System.out.println("🟥 AttemptAuthentication");

        ObjectMapper objectMapper = new ObjectMapper();
        AuthDto authDto = objectMapper.readValue(request.getInputStream(), AuthDto.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();

        String accessToken = jwtTokenizer.delegateAccessToken(user);
        String refreshToken = jwtTokenizer.delegateRefreshToken(user);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);

        response.addHeader("Set-Cookie", Utility.createCookie("token", accessToken, 10).toString());
        response.addHeader("Set-Cookie", Utility.createCookie("refresh", refreshToken, 60).toString());

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);

        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
