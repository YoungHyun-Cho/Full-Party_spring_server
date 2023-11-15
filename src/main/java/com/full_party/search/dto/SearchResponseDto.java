package com.full_party.search.dto;

import com.full_party.dto.CommonInformationDto;
import com.full_party.party.dto.PartyResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SearchResponseDto extends CommonInformationDto {
    private List<PartyResponseDto> result;

    public SearchResponseDto(Boolean notificationBadge, List<PartyResponseDto> result) {
        super(notificationBadge);
        this.result = result;
    }
}
