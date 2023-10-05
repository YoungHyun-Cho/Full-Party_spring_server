package com.full_party.heart.mapper;

import com.full_party.heart.dto.HeartResponseDto;
import com.full_party.heart.entity.Heart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HeartMapper {

//    @Mapping(target = "heartList", qualifiedByName = "heartListToDto")
//    HeartResponseDto heartListToHeartResponseDto(List<Heart> heartList);

//    @Named("heartListToDto")
    default HeartResponseDto heartListToHeartResponseDto(List<Heart> heartList) {
        return new HeartResponseDto(heartList);
    }
}
