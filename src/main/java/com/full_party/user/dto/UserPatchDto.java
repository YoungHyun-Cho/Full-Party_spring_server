package com.full_party.user.dto;

import com.full_party.values.Coordinates;
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
    private String address;
    private Coordinates coordinates;

    private Date birth; // Deprecate 예정
    private Gender gender; // Deprecate 예정
    private String mobile; // Deprecate 예정
}
