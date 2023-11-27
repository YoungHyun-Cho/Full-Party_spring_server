package com.full_party.user.dto;

import com.full_party.values.Coordinates;
import com.full_party.values.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPostDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String userName;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])((?=.*\\d)(?=.*\\W).{8,16}$)")
    private String password;

    private String address;
    private Coordinates coordinates;
    private String profileImage;

    private Date birth; // Deprecate 예정
    private Gender gender; // Deprecate 예정
    private String mobile; // Deprecate 예정
}
