package com.full_party.domain.search.mapper;

import com.full_party.domain.party.dto.PartyResponseDto;
import com.full_party.domain.search.dto.SearchResponseDto;
import com.full_party.global.values.Coordinates;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SearchMapper {

    default SearchResponseDto mapToSearchResponseDto(Boolean notificationBadge, List<PartyResponseDto> partyResponseDtos, Coordinates coordinates) {

        return new SearchResponseDto(notificationBadge, partyResponseDtos, coordinates);
    }
}
