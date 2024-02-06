package com.full_party.global.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


import lombok.Getter;

public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    NOT_SELECTED("선택 안함");

    @Getter
    private String korGender;

    Gender(String gender) {
        this.korGender = gender;
    }

    @JsonValue
    public String getKorGender() {
        return korGender;
    }

    @JsonCreator
    public static Gender fromString(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.korGender.equalsIgnoreCase(value)) return gender;
        }
        throw new IllegalArgumentException("Invalid Gender: " + value); // 추후 예외 처리 필요
    }
}
