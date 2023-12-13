package com.full_party.domain.user.dto;

import com.full_party.global.values.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class UserDetailResponseDto {

    private String userName;
    private String email;
    private String mobile;
    private String address;
    private Date birth;
    private Gender gender;
}
