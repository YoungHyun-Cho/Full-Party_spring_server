package com.full_party.notification.service;

import com.full_party.notification.entity.Notification;
import com.full_party.notification.repository.NotificationRepository;
import com.full_party.party.entity.Party;
import com.full_party.user.entity.User;
import com.full_party.values.NotificationInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(User user, Party party, NotificationInfo notificationType, String subject) {
        return notificationRepository.save(new Notification(user, party, notificationType, subject));
    }

    public Boolean checkIsUnread(Long userId) {

        return findAll(userId).stream()
                .anyMatch(notification -> !notification.getIsRead());
    }

    public List<Notification> findAll(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
}
