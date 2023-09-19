package com.full_party.user.dto;

import com.full_party.values.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserPatchDto {

    private Long userId;
    private UserInfo userInfo;
}
