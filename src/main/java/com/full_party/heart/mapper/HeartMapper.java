package com.full_party.heart.mapper;

import com.full_party.heart.dto.HeartResponseDto;
import com.full_party.heart.entity.Heart;
import com.full_party.party.dto.PartyResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HeartMapper {

    default HeartResponseDto partyListToHeartResponseDto(Boolean notificationBadge, List<PartyResponseDto> partyList) {
        return new HeartResponseDto(notificationBadge, partyList);
    }
}
