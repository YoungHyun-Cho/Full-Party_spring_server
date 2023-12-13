package com.full_party.domain.party.dto;

import com.full_party.global.values.SignUpType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyMemberDto {

    private Long id;
    private String userName;
    private String address;
    private String profileImage;
    private String message;
    private Integer exp;
    private Integer level;
    private SignUpType signUpType;
    private LocalDateTime joinDate;

}
