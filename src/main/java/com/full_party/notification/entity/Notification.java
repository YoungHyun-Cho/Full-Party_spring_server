package com.full_party.notification.entity;

import com.full_party.audit.Auditable;
import com.full_party.party.entity.Party;
import com.full_party.user.entity.User;
import com.full_party.values.NotificationInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Notification extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    @ManyToOne
    @JoinColumn(name = "SUBJECT_ID")
    private User subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationInfo notificationInfo;

    @Column(nullable = false)
    private Boolean isRead = false;

    public Notification(User user, Party party, NotificationInfo notificationType, User subject) {
        this.user = user;
        this.party = party;
        this.notificationInfo = notificationType;
        this.subject = subject;
    }
}
