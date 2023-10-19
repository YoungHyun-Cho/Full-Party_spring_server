package com.full_party.config;

import com.full_party.auth.filter.JwtAuthenticationFilter;
import com.full_party.auth.filter.JwtVerificationFilter;
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class SecurityConfiguration {

    private final JwtTokenizer jwtTokenizer;
    private final UserAuthenticationSuccessHandler userAuthenticationSuccessHandler;
    private final UserAuthenticationFailureHandler userAuthenticationFailureHandler;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final UserDetailService userDetailService;

    public SecurityConfiguration(JwtTokenizer jwtTokenizer, UserAuthenticationSuccessHandler userAuthenticationSuccessHandler, UserAuthenticationFailureHandler userAuthenticationFailureHandler, CustomAuthorityUtils customAuthorityUtils, @Lazy UserDetailService userDetailService) {
        this.jwtTokenizer = jwtTokenizer;
        this.userAuthenticationSuccessHandler = userAuthenticationSuccessHandler;
        this.userAuthenticationFailureHandler = userAuthenticationFailureHandler;
        this.customAuthorityUtils = customAuthorityUtils;
        this.userDetailService = userDetailService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
//                .cors(Customizer.withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));

        corsConfiguration.addAllowedOriginPattern("http://localhost:3000"); // 로컬 프론트에서 접근

        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addExposedHeader("Refresh");

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
            jwtAuthenticationFilter.setFilterProcessesUrl("/v1/auth/signin");
//            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new UserAuthenticationSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(userAuthenticationSuccessHandler);
            jwtAuthenticationFilter.setAuthenticationFailureHandler(userAuthenticationFailureHandler);

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, customAuthorityUtils, userDetailService);

            builder.addFilter(corsFilter())
                   .addFilter(jwtAuthenticationFilter)
                   .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);

            /*
            * AuthenticationFilter 추가하면 Controller로 요청이 안넘어감
            * SuccessHandler까지는 잘 실행됨.
            *
            * */
        }
    }
}
