package com.full_party.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum NotificationInfo {

    APPLY("APPLY", Type.PARTY_REQUIRE_NAME, "님이 퀘스트에 지원했습니다."), // 파티 가입 신청 -> 파티장에게
    ACCEPT("ACCEPT", Type.PARTY, "파티 가입이 승인되었습니다."), // 파티 가입 승인 -> 파티원 1명에게
    DENY("DENY", Type.PARTY, "파티 가입이 거절되었습니다."), // 파티 가입 거절 -> 파티원 1명에게
    CANCEL("CANCEL", Type.PARTY_REQUIRE_NAME, "님이 파티 가입 신청을 취소했습니다."), // 가입 신청 취소 -> 파티장에게
    EXPEL("EXPEL", Type.PARTY, "파티에서 추방당했습니다."), // 파티 추방 -> 파티원 1명에게
    QUIT("QUIT", Type.PARTY_REQUIRE_NAME, "님이 파티에서 탈퇴했습니다."), // 파티 탈퇴 -> 파티장에게
    HEART("HEART", Type.PARTY_REQUIRE_NAME, "님이 퀘스트에 관심을 보입니다."), // 좋아요 알림 -> 파티장에게
    COMPLETE("COMPLETE", Type.PARTY, "퀘스트를 클리어했습니다!"), // 모두에게
    REVIEW("REVIEW", Type.PARTY, "리뷰를 작성해주세요."), // 파티원에게
    FULL_PARTY("FULL_PARTY", Type.PARTY, "파티원 모집이 완료됐습니다. 퀘스트를 진행해 보세요!"), // 모두에게
    RE_PARTY("RE_PARTY", Type.PARTY, "파티원을 재모집 중입니다."), // 모두에게
    DISMISS("DISMISS", Type.PARTY, "파티가 해산되었습니다."), // 모두에게
    COMMENT("COMMENT", Type.PARTY_REQUIRE_NAME, "님이 댓글을 달았습니다."), // 모두에게
    REPLY("REPLY", Type.PARTY_REQUIRE_NAME, "님이 대댓글을 달았습니다."), // Comment 작성자에게
    LEVEL_UP("LEVEL_UP", Type.USER, "레벨이 올랐습니다!"), // 개인에게
    LEVEL_DOWN("LEVEL_DOWN", Type.USER, "레벨이 떨어졌습니다."); // 개인에게

    @Getter
    private String label;

    @Getter
    private Type type;

    @Getter
    private String content;

    NotificationInfo(String label, Type type, String content) {
        this.label = label;
        this.type = type;
        this.content = content;
    }

    public enum Type {
        USER("user"),
        PARTY("party"),
        PARTY_REQUIRE_NAME("party_require_name"),
        DO_NOT_LINK("do_not_link");

        private String description;

        Type(String description) {
            this.description = description;
        }

        public String toString() {
            return description;
        }
    }

//    @JsonCreator
//    public static NotificationInfo fromString(String value) {
//        for (NotificationInfo notificationInfo : NotificationInfo.values()) {
//            if (notificationInfo.type.equalsIgnoreCase(value)) return notificationInfo;
//        }
//        throw new IllegalArgumentException("Invalid NotificationType: " + value); // 추후 예외 처리 필요
//    }
}