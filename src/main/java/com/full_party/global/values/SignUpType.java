package com.full_party.global.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SignUpType {
    NORMAL("normal"),
    KAKAO("kakao"),
    GOOGLE("google"),
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
