package com.full_party.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private String subject;
    private String content;
    private String type;
    private String label;
    private Long partyId;
    private Boolean isRead;
    private LocalDateTime createdAt;
}

/*
* - NotificationInfo의 타입에 따라서 적절하게 제목과 내용을 리턴
*   - 레벨업, 레벨다운 -> 제목에 레벨
*   - 파티 관련 -> 제목에 파티 이름
* - ~님이 로 시작하는 알림은 DTO에 매핑할 때 controller에서 제공
*
*
* */
