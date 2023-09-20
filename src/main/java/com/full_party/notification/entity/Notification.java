package com.full_party.notification.entity;

import com.full_party.audit.Auditable;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Notification extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isRead;
}
