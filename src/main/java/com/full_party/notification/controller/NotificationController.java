package com.full_party.notification.controller;

import com.full_party.notification.dto.NotificationListDto;
import com.full_party.notification.entity.Notification;
import com.full_party.notification.mapper.NotificationMapper;
import com.full_party.notification.service.NotificationService;
import com.full_party.util.Utility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    @GetMapping
    public ResponseEntity getNotifications(@AuthenticationPrincipal UserDetails userDetails) {

        // DB에서 가져오고,
        List<Notification> notifications = notificationService.findAll(Utility.getUserId(userDetails));
        
        // 일단 DTO로 매핑한 다음,
        NotificationListDto notificationListDto = notificationMapper.mapToNotificationDtoList(notifications);

        // 엔티티의 isRead를 true로 바꿔준 다음 save -> notificationListDto의 isRead는 영향 X
        notificationService.changeIsRead(notifications);

        return new ResponseEntity(notificationListDto, HttpStatus.OK);
    }
}
