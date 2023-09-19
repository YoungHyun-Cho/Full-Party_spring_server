package com.full_party.user.dto;

import com.full_party.values.Gender;
import lombok.Getter;

import java.util.Date;

@Getter
public class UserInfo {
    private String email;
    private String userName;
    private String password;
    private String profileImage;
    private Date birth;
    private Gender gender;
    private String address;
    private String mobile;
}
