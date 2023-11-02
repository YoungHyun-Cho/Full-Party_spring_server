package com.full_party.search.dto;

import com.full_party.party.dto.PartyResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDto {
    private List<PartyResponseDto> result;
}
