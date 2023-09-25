package com.full_party.user.mapper;

import com.full_party.user.dto.*;
import com.full_party.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User userPostDtoToUser(UserPostDto userPostDto);
    User userPatchDtoToUser(UserPatchDto userPatchDto);
    UserBasicResponseDto userToUserBasicResponseDto(User user);
    UserDetailResponseDto userToUserDetailResponseDto(User user);
    UserPatchResponseDto userToUserPatchResponseDto(User user);
}
