package com.full_party.domain.heart.mapper;

import com.full_party.domain.heart.dto.HeartResponseDto;
import com.full_party.domain.party.dto.PartyResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HeartMapper {

    default HeartResponseDto partyListToHeartResponseDto(Boolean notificationBadge, List<PartyResponseDto> partyList) {
        return new HeartResponseDto(notificationBadge, partyList);
    }
}
