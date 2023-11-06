package com.full_party.values;

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

    @JsonValue // 동작 확인 필요 : 여성, 남성이 응답으로 보내지는지 + @JsonValue를 필드 korGender에 붙여도 동일하게 동작하는지
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
