package com.full_party.user.dto;

import com.full_party.values.Coordinates;
import com.full_party.values.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPostDto {
    private String email;
    private String userName;
    private String password;
    private String profileImage;
    private Date birth;
    private Gender gender;
    private String address;
    private String mobile;
    private Coordinates coordinates;
}
