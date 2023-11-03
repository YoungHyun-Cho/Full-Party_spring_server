package com.full_party.party.dto;

import com.full_party.user.entity.User;
import com.full_party.values.SignUpType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyMemberDto {

    private Long id;
    private String userName;
    private String address;
    private String profileImage;
    private Integer exp;
    private Integer level;
    private SignUpType signUpType;
    private LocalDateTime joinDate;

//    public PartyMemberDto(User user, LocalDateTime joinDate) {
//        this.id = user.getId();
//        this.userName = user.getUserName();
//        this.address = user.getAddress();
//        this.profileImage = user.getProfileImage();
//        this.exp = user.getExp();
//        this.level = user.getLevel();
//        this.signUpType = user.getSignUpType();
//    }
}