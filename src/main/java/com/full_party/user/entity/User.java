package com.full_party.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.full_party.audit.Auditable;
import com.full_party.comment.entity.Comment;
import com.full_party.comment.entity.Reply;
import com.full_party.heart.entity.Heart;
import com.full_party.notification.entity.Notification;
import com.full_party.party.entity.Party;
import com.full_party.party.entity.UserParty;
import com.full_party.party.entity.Waiter;
import com.full_party.values.Coordinates;
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
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Party> parties = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserParty> userParties = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.REMOVE)
    private List<Notification> notificationsAsSubject = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
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

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer exp;

    @Column(nullable = false)
    private Integer level;

    @Embedded
    @Column(nullable = false)
    private Coordinates coordinates;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignUpType signUpType;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Transient
    private Integer levelUpExp;

    public User(User previousUser, User modifiedUser) {
        this.id = previousUser.id;
        this.email = previousUser.email;
        this.exp = previousUser.exp;
        this.level = previousUser.level;
        this.signUpType = previousUser.signUpType;
        this.userName = modifiedUser.userName == null ? previousUser.userName : modifiedUser.userName;
        this.password = modifiedUser.password == null ? previousUser.password : modifiedUser.password;
        this.profileImage = modifiedUser.profileImage == null ? previousUser.profileImage : modifiedUser.profileImage;
        this.birth = modifiedUser.birth == null ? previousUser.birth : modifiedUser.birth;
        this.gender = modifiedUser.gender == null ? previousUser.gender : modifiedUser.gender;
        this.mobile = modifiedUser.mobile == null ? previousUser.mobile : modifiedUser.mobile;
        this.address = modifiedUser.address == null ? previousUser.address : modifiedUser.address;
        this.coordinates = modifiedUser.coordinates == null ? previousUser.coordinates : modifiedUser.coordinates;
    }

    public User(User newUser) {
        this.email = newUser.email;
        this.signUpType = newUser.getSignUpType() == null ? SignUpType.NORMAL : newUser.getSignUpType();
        this.userName = newUser.userName;
        this.password = newUser.password;
        this.profileImage = newUser.profileImage;
        this.birth = newUser.birth;
        this.gender = newUser.gender;
        this.mobile = newUser.mobile;
        this.address = newUser.address;
        this.coordinates = newUser.coordinates;
        this.exp = 0;
        this.level = 1;
        this.roles = newUser.roles;
    }

    public User(Long id) {
        this.id = id;
    }

    public User(String email, String userName, String profileImage, SignUpType signUpType) {
        this.email = email;
        this.userName = userName;
        this.profileImage = profileImage;
        this.password = signUpType.getType() + userName;
        this.address = signUpType.getType();
        this.birth = new Date();
        this.exp = 0;
        this.level = 1;
        this.coordinates = new Coordinates(37.496562, 127.024761);
        this.gender = Gender.NOT_SELECTED;
        this.mobile = signUpType.getType();
        this.signUpType = signUpType;
    }

    public String getProfileImage() {
        if (signUpType == SignUpType.GUEST || signUpType == SignUpType.NORMAL) return "https://fullpartyspringimageserver.s3.ap-northeast-2.amazonaws.com/" + profileImage;
        else return profileImage;
    }
}
