package com.full_party.user.mapper;

import com.full_party.user.dto.*;
import com.full_party.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User userPostDtoToUser(UserPostDto userPostDto);
    User userPatchDtoToUser(UserPatchDto userPatchDto);
    UserBasicResponseDto userToUserBasicResponseDto(User user);
    UserDetailResponseDto userToUserDetailResponseDto(User user);
}
