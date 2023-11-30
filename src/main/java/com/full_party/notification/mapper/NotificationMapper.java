package com.full_party.notification.mapper;

import com.full_party.notification.dto.NotificationDto;
import com.full_party.notification.dto.NotificationListDto;
import com.full_party.notification.entity.Notification;
import com.full_party.party.service.PartyService;
import com.full_party.user.service.UserService;
import com.full_party.values.NotificationInfo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    default NotificationDto mapToNotificationDto(Notification notification) {

        String subject;
        String content;
        NotificationInfo notificationInfo = notification.getNotificationInfo();

        if (notificationInfo.getType() == NotificationInfo.Type.USER) {
            subject = "Lv. " + notification.getUser().getLevel();
            content = notificationInfo.getContent();
        }
        else if (notificationInfo.getType() == NotificationInfo.Type.PARTY) {
            subject = notification.getParty().getName();
            content = notificationInfo.getContent();
        }
        else {
            subject = notification.getParty().getName();
            content = notification.getSubject().getUserName() + notificationInfo.getContent();
        }

        return new NotificationDto(
                subject, content, notificationInfo.getType().toString(), notificationInfo.getLabel(),
                notification.getParty().getId(), notification.getIsRead(), notification.getCreatedAt()
        );
    }

    default NotificationListDto mapToNotificationDtoList(List<Notification> notifications) {

        return new NotificationListDto(
                notifications.stream()
                        .map(notification -> mapToNotificationDto(notification))
                        .collect(Collectors.toList())
        );
    }
}
