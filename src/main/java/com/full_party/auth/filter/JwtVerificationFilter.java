package com.full_party.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.full_party.auth.jwt.JwtTokenizer;
import com.full_party.auth.userdetails.UserDetail;
import com.full_party.auth.userdetails.UserDetailService;
import com.full_party.auth.utils.CustomAuthorityUtils;
import com.full_party.config.SecurityConfiguration;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final UserDetailService userDetailService;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer, CustomAuthorityUtils customAuthorityUtils, UserDetailService userDetailService) {
        this.jwtTokenizer = jwtTokenizer;
        this.customAuthorityUtils = customAuthorityUtils;
        this.userDetailService = userDetailService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String authorization = request.getHeader("Authorization");

//        System.out.println("🔴 should not filter : " + authorization == null || !authorization.startsWith("Bearer") || authorization.equals("Bearer undefined"));

        return authorization == null || !authorization.startsWith("Bearer") || authorization.equals("Bearer undefined") || authorization.equals("Bearer temp");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);

            filterChain.doFilter(request, response);
        }
        catch (MalformedJwtException e) {

            System.out.println("❌ MalformedJwtException");
            handleExpiredJwtException(response, e);
        }

    }

    private static void handleExpiredJwtException(HttpServletResponse response, MalformedJwtException e) {
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(e.getMessage());
            response.getWriter().write(json);
        }
        catch (Exception error) {
            System.out.println(error.getMessage());
        }
    }

    private Map<String, Object> verifyJws(HttpServletRequest request) {
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();

        return claims;
    }

    private void setAuthenticationToContext(Map<String, Object> claims) throws NoSuchElementException {

        String username = (String) claims.get("username");
//        List<GrantedAuthority> authorities = customAuthorityUtils.createAuthorities((List) claims.get("roles"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                (UserDetail) userDetailService.loadUserByUsername(username), // https://devjem.tistory.com/70
                null
//                authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
