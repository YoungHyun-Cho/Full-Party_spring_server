package com.full_party.exception;

import lombok.Getter;

import java.util.Arrays;

public enum ExceptionCode {

    USER_NOT_FOUND(404, "User Not Found"),
    USER_EXISTS(409, "User Exists"),
    QUEST_NOT_FOUND(404, "Quest Not Found"),
    TAGS_NOT_FOUND(404, "Tags Not Found"),
    PARTY_EXISTS(409, "Party Exists"),
    WAITER_NOT_FOUND(404, "Waiter Not Found"),
    USER_PARTY_NOT_FOUND(404, "User Party Not Found"),
    COMMENT_NOT_FOUND(404, "Comment Not Found"),
    REPLY_NOT_FOUND(404, "Reply Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

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
