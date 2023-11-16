package com.full_party.search.dto;

import com.full_party.dto.CommonInformationDto;
import com.full_party.party.dto.PartyResponseDto;
import com.full_party.values.Coordinates;
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
    private Coordinates coordinates;

    public SearchResponseDto(Boolean notificationBadge, List<PartyResponseDto> result, Coordinates coordinates) {
        super(notificationBadge);
        this.result = result;
        this.coordinates = coordinates;
    }
}
