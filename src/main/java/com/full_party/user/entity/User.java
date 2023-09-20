package com.full_party.user.entity;

import com.full_party.audit.Auditable;
import com.full_party.values.Gender;
import com.full_party.values.SignUpType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

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
