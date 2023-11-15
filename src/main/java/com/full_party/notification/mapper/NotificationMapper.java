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

    /*
    * - 알림 생성 시각과 알림 제공 시각 간에 차이 존재
    * - 알림 생성 이후, 파티 이름, 유저 이름 등이 변경될 것을 감안하여, 알림 제공 시점에서 최신의 정보를 다시 가져와서 제공
    *
    * 🟥 해야 할 것
    * - Mapper를 레퍼런스 삼아 PartyController와 NotificationController 코드 수정
    * */
//
//    default NotificationListDto mapToNotificationListDto(List<Notification> notifications) {
//
//        List<NotificationDto> notificationDtos = notifications.stream()
//                .map(notification -> notificationToNotificationDto(notification))
//                .collect(Collectors.toList());
//
//        return new NotificationListDto(notificationDtos);
//    }
}
