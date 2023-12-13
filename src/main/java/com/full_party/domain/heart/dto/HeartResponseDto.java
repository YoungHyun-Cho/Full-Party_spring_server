package com.full_party.domain.heart.dto;

import com.full_party.global.dto.CommonInformationDto;
import com.full_party.domain.party.dto.PartyResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HeartResponseDto extends CommonInformationDto {

    private List<PartyResponseDto> parties;

    public HeartResponseDto(Boolean notificationBadge, List<PartyResponseDto> parties) {
        super(notificationBadge);
        this.parties = parties;
    }
}
