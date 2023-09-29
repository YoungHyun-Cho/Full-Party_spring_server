package com.full_party.heart.mapper;

import com.full_party.heart.dto.HeartResponseDto;
import com.full_party.heart.entity.Heart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HeartMapper {

    HeartResponseDto heartListToHeartResponseDto(List<Heart> heartList);
}
