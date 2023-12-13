package com.full_party.global.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PartyState {
    RECRUITING("모집 중"),
    FULL_PARTY("모집 완료"),
    COMPLETED("퀘스트 완료"),
    DISMISSED("파티 해산");

    @Getter
    @JsonValue
    private String state;

    PartyState(String state) {
        this.state = state;
    }

    @JsonCreator
    public static PartyState fromString(String value) {
        for (PartyState partyState : PartyState.values()) {
            if (partyState.state.equalsIgnoreCase(value)) return partyState;
        }
        throw new IllegalArgumentException("Invalid PartyState: " + value); // 추후 예외 처리 필요
    }
}
