package com.full_party.config;

import com.full_party.auth.filter.JwtAuthenticationFilter;
import com.full_party.auth.filter.JwtVerificationFilter;
import com.full_party.auth.handler.OAuth2UserSuccessHandler;
import com.full_party.auth.handler.UserAuthenticationFailureHandler;
import com.full_party.auth.handler.UserAuthenticationSuccessHandler;
import com.full_party.auth.jwt.JwtTokenizer;
import com.full_party.auth.userdetails.UserDetailService;
import com.full_party.auth.utils.CustomAuthorityUtils;
import com.full_party.user.controller.UserController;
import com.full_party.user.mapper.UserMapper;
import com.full_party.user.mapper.UserMapperImpl;
import com.full_party.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfiguration {
    private final UserAuthenticationSuccessHandler userAuthenticationSuccessHandler;
    private final UserAuthenticationFailureHandler userAuthenticationFailureHandler;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final UserService userService;
    private final UserDetailService userDetailService;
    private final JwtTokenizer jwtTokenizer;

    public SecurityConfiguration(UserAuthenticationSuccessHandler userAuthenticationSuccessHandler, UserAuthenticationFailureHandler userAuthenticationFailureHandler, CustomAuthorityUtils customAuthorityUtils, @Lazy UserService userService, @Lazy UserDetailService userDetailService, JwtTokenizer jwtTokenizer) {
        this.userAuthenticationSuccessHandler = userAuthenticationSuccessHandler;
        this.userAuthenticationFailureHandler = userAuthenticationFailureHandler;
        this.customAuthorityUtils = customAuthorityUtils;
        this.userService = userService;
        this.userDetailService = userDetailService;
        this.jwtTokenizer = jwtTokenizer;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2.successHandler(new OAuth2UserSuccessHandler(userService, jwtTokenizer))
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOriginPattern("https://localhost:3000"); // 로컬 프론트에서 접근
        corsConfiguration.addAllowedOriginPattern("https://fullpartyspring.com"); // 배포 프론트에서 접근
        corsConfiguration.addAllowedOriginPattern("https://accounts.google.com");

        corsConfiguration.setAllowCredentials(true);

        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addExposedHeader("Refresh");
        corsConfiguration.addExposedHeader("Set-Cookie");
        corsConfiguration.addExposedHeader("Location");

        corsConfiguration.addAllowedHeader("*");

        corsConfiguration.addAllowedMethod("OPTIONS");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("PATCH");
        corsConfiguration.addAllowedMethod("DELETE");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {

            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);

            jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);

            jwtAuthenticationFilter.setRequiresAuthenticationRequestMatcher(authenticationFilterPath());

            jwtAuthenticationFilter.setAuthenticationSuccessHandler(userAuthenticationSuccessHandler);
            jwtAuthenticationFilter.setAuthenticationFailureHandler(userAuthenticationFailureHandler);

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(userDetailService, jwtTokenizer);


            builder.addFilter(corsFilter())
                   .addFilter(jwtAuthenticationFilter)
                   .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class)
                   .addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);

        }
    }

    private RequestMatcher authenticationFilterPath() {
        String[] allowedPaths = {"/auth/signin", "/auth/verification"};
        List<RequestMatcher> processingMatchers = new ArrayList<>();

        for (String path : allowedPaths) {
            processingMatchers.add(new AntPathRequestMatcher(path));
        }

        return new OrRequestMatcher(processingMatchers);
    }
}