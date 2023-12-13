package com.full_party.domain.notification.mapper;

import com.full_party.domain.notification.dto.NotificationDto;
import com.full_party.domain.notification.dto.NotificationListDto;
import com.full_party.domain.notification.entity.Notification;
import com.full_party.global.values.NotificationInfo;
import org.mapstruct.Mapper;

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
