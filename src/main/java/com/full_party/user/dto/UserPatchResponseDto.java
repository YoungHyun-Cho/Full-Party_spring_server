package com.full_party.user.dto;

import com.full_party.values.Gender;
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

/*
 * password는 뺌. -> 굳이 응답으로 전송 필요 없음.
 * */