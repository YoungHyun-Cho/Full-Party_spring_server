package com.full_party.user.dto;

import com.full_party.party.dto.PartyResponseDto;
import com.full_party.values.SignUpType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicResponseDto {

    private Long id;
    private String userName;
    private String address;
    private String profileImage;
    private Integer exp;
    private Integer level;
    private SignUpType signUpType;
    private Map<String, List<PartyResponseDto>> relatedParties;
}

