package com.full_party.exception;

import lombok.Getter;

public enum ExceptionCode {

    USER_NOT_FOUND(404, "User Not Found"),
    USER_EXISTS(409, "User Exists"),
    QUEST_NOT_FOUND(404, "Quest Not Found"),
    TAGS_NOT_FOUND(404, "Tags Not Found"),
    PARTY_EXISTS(409, "Party Exists"),
    WAITER_NOT_FOUND(404, "Waiter Not Found"),
    USER_PARTY_NOT_FOUND(404, "User Party Not Found");

    @Getter
    private Integer status;

    @Getter
    private String message;

    ExceptionCode(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
