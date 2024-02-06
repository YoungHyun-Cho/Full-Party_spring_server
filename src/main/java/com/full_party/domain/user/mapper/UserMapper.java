package com.full_party.domain.user.mapper;

import com.full_party.domain.user.dto.UserBasicResponseDto;
import com.full_party.domain.user.dto.UserDetailResponseDto;
import com.full_party.domain.user.dto.UserPatchDto;
import com.full_party.domain.user.dto.UserPostDto;
import com.full_party.domain.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User userPostDtoToUser(UserPostDto userPostDto);
    User userPatchDtoToUser(UserPatchDto userPatchDto);
    UserBasicResponseDto userToUserBasicResponseDto(User user);
    UserDetailResponseDto userToUserDetailResponseDto(User user);
}
