package com.full_party.heart.dto;

import com.full_party.dto.CommonInformationDto;
import com.full_party.heart.entity.Heart;
import com.full_party.party.dto.PartyResponseDto;
import lombok.AllArgsConstructor;
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
