package com.full_party.auth.handler;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.error("# Authentication failed : {}", exception.getMessage());

        sendErrorResponse(response);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {

        Gson gson = new Gson();

        // 예외 처리 구현 후 구현 필요
    }
}
