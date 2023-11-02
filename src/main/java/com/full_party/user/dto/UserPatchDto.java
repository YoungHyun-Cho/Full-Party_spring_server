package com.full_party.user.dto;

import com.full_party.values.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPatchDto {

    private Long id;
    private String userName;
    private String password;
    private String profileImage;
    private Date birth;
    private Gender gender;
    private String address;
    private String mobile;
}
