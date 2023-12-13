package com.full_party.global.exception;

import lombok.Getter;

import java.util.Arrays;

public enum ExceptionCode {

    USER_NOT_FOUND(404, "User Not Found"),
    USER_EXISTS(409, "User Exists"),
    PARTY_NOT_FOUND(404, "Party Not Found"),
    WAITER_NOT_FOUND(404, "Waiter Not Found"),
    USER_PARTY_NOT_FOUND(404, "User Party Not Found"),
    PARTY_STATE_NOT_FOUND(404, "Party State Not Found");

    @Getter
    private Integer status;

    @Getter
    private String message;

    ExceptionCode(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ExceptionCode getExceptionCode(String errMsg) {
        return Arrays.stream(values())
                .filter(value -> value.message.equals(errMsg))
                .findAny()
                .orElse(null);
    }
}
