package com.full_party.notification.mapper;

import com.full_party.notification.dto.NotificationDto;
import com.full_party.notification.entity.Notification;
import com.full_party.party.service.PartyService;
import com.full_party.user.service.UserService;
import com.full_party.values.NotificationInfo;

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
        NotificationInfo.Type type = notification.getNotificationInfo().getType();

        if (type == NotificationInfo.Type.USER) {
            subject = "Lv. " + notification.getUser().getLevel();
            content = notification.getNotificationInfo().getContent();
        }
        else if (type == NotificationInfo.Type.PARTY) {
            subject = partyService.findParty(notification.getParty().getId()).getName(); // 아래 설명 참고
            content = notification.getNotificationInfo().getContent();
        }
        else {
            subject = partyService.findParty(notification.getParty().getId()).getName(); // 아래 설명 참고
            content = userService.findUser(notification.getSubjectId()).getUserName() + notification.getNotificationInfo().getContent(); // 아래 설명 참고
        }

        return new NotificationDto(subject, content, notification.getCreatedAt());
    }

    /*
    * - 알림 생성 시각과 알림 제공 시각 간에 차이 존재
    * - 알림 생성 이후, 파티 이름, 유저 이름 등이 변경될 것을 감안하여, 알림 제공 시점에서 최신의 정보를 다시 가져와서 제공
    *
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
