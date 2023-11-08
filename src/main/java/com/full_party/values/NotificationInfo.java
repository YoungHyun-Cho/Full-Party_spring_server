package com.full_party.values;

import lombok.Getter;

public enum NotificationInfo {

//    "apply": "님이 퀘스트에 지원했습니다.",
//            "accept": "파티 가입이 승인됐습니다.",
//            "deny": "파티 가입이 거절됐습니다.",
//            "expel": "파티에서 추방당했습니다.",
//            "quit": "님이 파티에서 탈퇴했습니다.",
//            "favorite": "님이 퀘스트에 관심을 보입니다.",
//            "complete": "퀘스트를 클리어했습니다!",
//            "fullparty": "파티원 모집이 완료됐습니다. 퀘스트를 진행해보세요!",
//            "reparty": "파티원을 재모집중입니다.",
//            "dismiss": "파티가 해산됐습니다.",
//            "question": "님의 퀘스트 문의가 도착했습니다.",
//            "answer": "퀘스트 문의에 대한 답변이 도착했습니다.",
//            "reply": "님의 답변에 대한 재문의가 도착했습니다.",
//            "levelup": "로 레벨이 올랐습니다!",
//            "leveldown": "로 레벨이 떨어졌습니다."

    APPLY(Type.PARTY_REQUIRE_NAME, "님이 퀘스트에 지원했습니다."), // 파티 가입 신청 -> 파티장에게
    ACCEPT(Type.PARTY, "파티 가입이 승인되었습니다."), // 파티 가입 승인 -> 파티원 1명에게
    DENY(Type.PARTY, "파티 가입이 거절되었습니다."), // 파티 가입 거절 -> 파티원 1명에게
    CANCEL(Type.PARTY_REQUIRE_NAME, "님이 파티 가입 신청을 취소했습니다."), // 가입 신청 취소 -> 파티장에게
    EXPEL(Type.PARTY, "파티에서 추방당했습니다."), // 파티 추방 -> 파티원 1명에게
    QUIT(Type.PARTY_REQUIRE_NAME, "님이 파티에서 탈퇴했습니다."), // 파티 탈퇴 -> 파티장에게
    HEART(Type.PARTY_REQUIRE_NAME, "님이 퀘스트에 관심을 보입니다."), // 좋아요 알림 -> 파티장에게
    COMPLETE(Type.PARTY, "퀘스트를 클리어했습니다!"), // 모두에게
    REVIEW(Type.PARTY, "리뷰를 작성해주세요."), // 파티원에게
    FULL_PARTY(Type.PARTY, "파티원 모집이 완료됐습니다. 퀘스트를 진행해 보세요!"), // 모두에게
    RE_PARTY(Type.PARTY, "파티원을 재모집 중입니다."), // 모두에게
    DISMISS(Type.PARTY, "파티가 해산되었습니다."), // 모두에게
    COMMENT(Type.PARTY_REQUIRE_NAME, "님이 댓글을 달았습니다."), // 모두에게
    REPLY(Type.PARTY_REQUIRE_NAME, "님이 대댓글을 달았습니다."), // Comment 작성자에게
    LEVEL_UP(Type.USER, "레벨이 올랐습니다!"), // 개인에게
    LEVEL_DOWN(Type.USER, "레벨이 떨어졌습니다."); // 개인에게

    @Getter
    private Type type;

    @Getter
//    @JsonValue
    private String content;

    NotificationInfo(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public enum Type {
        USER,
        PARTY,
        PARTY_REQUIRE_NAME
    }

//    @JsonCreator
//    public static NotificationType fromString(String value) {
//        for (NotificationType notificationType : NotificationType.values()) {
//            if (notificationType.content.equalsIgnoreCase(value)) return notificationType;
//        }
//        throw new IllegalArgumentException("Invalid NotificationType: " + value); // 추후 예외 처리 필요
//    }
}