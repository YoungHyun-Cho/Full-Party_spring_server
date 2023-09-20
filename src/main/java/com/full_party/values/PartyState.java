package com.full_party.values;

import lombok.Getter;

public enum PartyState {
    RECRUITING("모집중"),
    FULL_PARTY("모집 완료"),
    COMPLETED("퀘스트 완료");

    @Getter
    private String state;

    PartyState(String state) {
        this.state = state;
    }
}
