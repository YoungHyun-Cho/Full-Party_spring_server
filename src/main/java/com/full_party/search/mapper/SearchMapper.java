package com.full_party.search.mapper;

import com.full_party.party.dto.PartyResponseDto;
import com.full_party.search.dto.SearchResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SearchMapper {

    default SearchResponseDto mapToSearchResponseDto(Boolean notificationBadge, List<PartyResponseDto> partyResponseDtos) {

        return new SearchResponseDto(notificationBadge, partyResponseDtos);
    }
}
