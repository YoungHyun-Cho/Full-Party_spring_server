package com.full_party.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum SignUpType {
    NORMAL("normal"),
    KAKAO("kakao"),
    GOOGLE("google"),
    NAVER("naver"),
    GUEST("guest");

    private String type;

    SignUpType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public static SignUpType fromString(String value) {
        for (SignUpType signUpType : SignUpType.values()) {
            if (signUpType.type.equalsIgnoreCase(value)) return signUpType;
        }
        throw new IllegalArgumentException("Invalid SignUpType: " + value); // 추후 예외 처리 필요
    }
}
