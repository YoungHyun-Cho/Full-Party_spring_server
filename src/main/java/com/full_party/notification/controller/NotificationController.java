package com.full_party.notification.controller;

import com.full_party.notification.dto.NotificationListDto;
import com.full_party.notification.entity.Notification;
import com.full_party.notification.mapper.NotificationMapper;
import com.full_party.notification.service.NotificationService;
import com.full_party.util.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @GetMapping
    public ResponseEntity getNotifications(@AuthenticationPrincipal UserDetails userDetails) {

        List<Notification> notifications = notificationService.findAll(Utility.getUserId(userDetails));
        NotificationListDto notificationListDto = notificationMapper.mapToNotificationDtoList(notifications);
        notificationService.changeIsRead(notifications);

        return new ResponseEntity(notificationListDto, HttpStatus.OK);
    }
}
