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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public User(User beforeUser, User afterUser) {
        this.id = beforeUser.id;
        this.email = beforeUser.email;
        this.exp = beforeUser.exp;
        this.level = beforeUser.level;
        this.signUpType = beforeUser.signUpType;
        this.userName = afterUser.userName;
        this.password = afterUser.password;
        this.profileImage = afterUser.profileImage;
        this.birth = afterUser.birth;
        this.gender = afterUser.gender;
        this.mobile = afterUser.mobile;
        this.address = afterUser.address;
    }

    public User(User newUser) {
        this.email = newUser.email;
        this.signUpType = SignUpType.NORMAL;
        this.userName = newUser.userName;
        this.password = newUser.password;
        this.profileImage = newUser.profileImage;
        this.birth = newUser.birth;
        this.gender = newUser.gender;
        this.mobile = newUser.mobile;
        this.address = newUser.address;
        this.exp = 0;
        this.level = 1;
    }
}
