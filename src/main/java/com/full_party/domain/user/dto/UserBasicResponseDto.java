package com.full_party.domain.user.dto;

import com.full_party.global.dto.CommonInformationDto;
import com.full_party.domain.party.dto.PartyResponseDto;
import com.full_party.global.values.SignUpType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserBasicResponseDto extends CommonInformationDto {

    private Long id;
    private String email;
    private String userName;
    private String address;
    private String profileImage;
    private Integer exp;
    private Integer level;
    private Integer levelUpExp;
    private SignUpType signUpType;
    private Map<String, List<PartyResponseDto>> relatedParties;
}

