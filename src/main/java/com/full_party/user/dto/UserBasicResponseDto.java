package com.full_party.user.dto;

import com.full_party.values.SignUpType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserBasicResponseDto {

    private String userName;
    private String address;
    private String profileImage;
    private Integer exp;
    private Integer level;
    private SignUpType signUpType;
}

