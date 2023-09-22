package com.full_party.user.entity;

import com.full_party.audit.Auditable;
import com.full_party.comment.entity.Comment;
import com.full_party.comment.entity.Reply;
import com.full_party.heart.entity.Heart;
import com.full_party.notification.entity.Notification;
import com.full_party.party.entity.UserParty;
import com.full_party.party.entity.Waiter;
import com.full_party.quest.entity.Quest;
import com.full_party.values.Gender;
import com.full_party.values.SignUpType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToMany(mappedBy = "user")
    private List<Quest> quests = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserParty> userParties = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList();

    @OneToMany(mappedBy = "user")
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Waiter> waiters = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false)
    private Date birth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false, unique = true)
    private String mobile;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(nullable = false)
    private Integer exp;

    @Column(nullable = false)
    private Integer level;

    @Enumerated(EnumType.STRING)
    @Column
    private SignUpType signUpType;
}
