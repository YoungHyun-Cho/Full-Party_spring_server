package com.full_party.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.full_party.auth.userdetails.UserDetail;
import com.full_party.user.controller.UserController;
import com.full_party.user.dto.UserBasicResponseDto;
import com.full_party.user.entity.User;
import com.full_party.user.mapper.UserMapper;
import com.full_party.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

@Slf4j
@Component
//public class UserAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("# Authenticated Successfully.");

//        setDefaultTargetUrl("/auth/signin?error=false&errMsg=null");
//        super.onAuthenticationSuccess(request, response, authentication);
    }
}
