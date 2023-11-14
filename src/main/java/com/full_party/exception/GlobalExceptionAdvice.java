package com.full_party.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity handleBusinessLogicException(BusinessLogicException e) {
        System.out.println("❌ Exception : " + e.getExceptionCode().getStatus() + " " + e.getExceptionCode().getMessage());
        return new ResponseEntity(HttpStatus.valueOf(e.getExceptionCode().getStatus()));
    }


}
