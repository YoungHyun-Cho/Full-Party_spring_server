package com.full_party.domain.notification.dto;

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