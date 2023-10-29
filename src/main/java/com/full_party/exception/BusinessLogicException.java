package com.full_party.exception;

import lombok.Getter;

public class BusinessLogicException extends RuntimeException {

    @Getter
    private ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public BusinessLogicException(String errMsg) {
        super(errMsg);
        this.exceptionCode = ExceptionCode.getExceptionCode(errMsg);
    }
}
