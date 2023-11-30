package com.full_party.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.full_party.auth.jwt.JwtTokenizer;
import com.full_party.auth.userdetails.UserDetail;
import com.full_party.auth.userdetails.UserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final UserDetailService userDetailService;
    private final JwtTokenizer jwtTokenizer;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");

        return authorization == null || !authorization.startsWith("Bearer") || authorization.equals("Bearer temp");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);

            filterChain.doFilter(request, response);
        }
        catch (MalformedJwtException | ExpiredJwtException e) {

            handleExpiredJwtException(response, e);
        }

    }

    private static void handleExpiredJwtException(HttpServletResponse response, JwtException e) {
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
        Map<String, Object> claims = jwtTokenizer.getClaims(jws).getBody();

        return claims;
    }

    private void setAuthenticationToContext(Map<String, Object> claims) throws NoSuchElementException {

        String username = (String) claims.get("username");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                (UserDetail) userDetailService.loadUserByUsername(username),
                null
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
