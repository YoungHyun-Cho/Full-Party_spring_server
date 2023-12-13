package com.full_party.domain.user.dto;

import com.full_party.global.values.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPatchResponseDto {

    private String userName;
    private String profileImage;
    private String address;
    private String mobile;
    private Date birth;
    private Gender gender;
}
