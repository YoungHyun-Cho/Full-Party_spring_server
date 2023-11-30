package com.full_party.notification.mapper;

import com.full_party.notification.dto.NotificationDto;
import com.full_party.notification.dto.NotificationListDto;
import com.full_party.notification.entity.Notification;
import com.full_party.party.service.PartyService;
import com.full_party.user.service.UserService;
import com.full_party.values.NotificationInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    private final UserService userService;
    private final PartyService partyService;

    public NotificationMapper(UserService userService, PartyService partyService) {
        this.userService = userService;
        this.partyService = partyService;
    }

    public NotificationDto mapToNotificationDto(Notification notification) {

        String subject;
        String content;
        NotificationInfo notificationInfo = notification.getNotificationInfo();

        if (notificationInfo.getType() == NotificationInfo.Type.USER) {
            subject = "Lv. " + notification.getUser().getLevel();
            content = notificationInfo.getContent();
        }
        else if (notificationInfo.getType() == NotificationInfo.Type.PARTY) {
            subject = partyService.findParty(notification.getParty().getId()).getName(); // 아래 설명 참고
            content = notificationInfo.getContent();
        }
        else {
            subject = partyService.findParty(notification.getParty().getId()).getName(); // 아래 설명 참고
            content = userService.findUser(notification.getSubjectId()).getUserName() + notificationInfo.getContent(); // 아래 설명 참고
        }

        System.out.println("subject = " + subject);
        System.out.println("content = " + content);

        return new NotificationDto(
                subject, content, notificationInfo.getType().toString(), notificationInfo.getLabel(),
                notification.getParty().getId(), notification.getIsRead(), notification.getCreatedAt()
        );
    }

    public NotificationListDto mapToNotificationDtoList(List<Notification> notifications) {

        return new NotificationListDto(
                notifications.stream()
                        .map(notification -> mapToNotificationDto(notification))
                        .collect(Collectors.toList())
        );
    }
}
