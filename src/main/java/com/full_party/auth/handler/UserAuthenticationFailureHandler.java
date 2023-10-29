package com.full_party.auth.handler;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@Component
public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.error("# Authentication failed : {}", exception.getMessage());

        System.out.println("❤️" + request.getRequestURI());

        System.out.println("🟥" + exception.getMessage());
        System.out.println("🟥" + exception.getCause());
        System.out.println("🟥" + exception.getClass());

        String errorMessage;

        if (exception instanceof BadCredentialsException ||
            exception instanceof InternalAuthenticationServiceException) errorMessage = "User Not Found";
        else errorMessage = "Internal Server Error"; // 예외 처리 구현 시 반영 필요

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8"); /* 한글 인코딩 깨짐 문제 방지 */
        setDefaultFailureUrl("/v1/auth/error?errMsg=" + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
