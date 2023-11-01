package com.full_party.party.entity;

import com.full_party.user.entity.User;
import com.full_party.values.SignUpType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartyMember {

    private Long id;
    private String userName;
    private String address;
    private String profileImage;
    private Integer exp;
    private Integer level;
    private SignUpType signUpType;
    private LocalDateTime joinDate;

    public PartyMember(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.address = user.getAddress();
        this.profileImage = user.getProfileImage();
        this.exp = user.getExp();
        this.level = user.getLevel();
        this.signUpType = user.getSignUpType();
    }
}
