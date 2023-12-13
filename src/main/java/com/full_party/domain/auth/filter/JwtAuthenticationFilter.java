package com.full_party.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.full_party.domain.auth.dto.AuthDto;
import com.full_party.domain.auth.jwt.JwtTokenizer;
import com.full_party.domain.user.entity.User;
import com.full_party.global.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

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

        String accessToken = jwtTokenizer.delegateAccessToken(user);
        String refreshToken = jwtTokenizer.delegateRefreshToken(user);

        response.addHeader("Set-Cookie", Utility.createCookie("token", accessToken, 10).toString());
        response.addHeader("Set-Cookie", Utility.createCookie("refresh", refreshToken, 60).toString());

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);

        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
