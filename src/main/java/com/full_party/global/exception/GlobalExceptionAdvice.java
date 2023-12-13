package com.full_party.global.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity handleBusinessLogicException(BusinessLogicException e) {
        return new ResponseEntity(HttpStatus.valueOf(e.getExceptionCode().getStatus()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        ErrorResponse response = new ErrorResponse(400, e.getFieldError().getDefaultMessage());
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleJwtException(JwtException e) {

        ErrorResponse response = new ErrorResponse(401, e.getMessage());
        return response;
    }
}
