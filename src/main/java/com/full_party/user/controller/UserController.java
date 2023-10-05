package com.full_party.user.controller;

import com.full_party.user.dto.UserPatchDto;
import com.full_party.user.dto.UserPostDto;
import com.full_party.user.entity.User;
import com.full_party.user.mapper.UserMapper;
import com.full_party.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final static String USER_DEFAULT_URL = "/v1/users";
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity postUser(@RequestBody UserPostDto userPostDto) {

        User user = userService.createUser(userMapper.userPostDtoToUser(userPostDto));

        URI uri =
                UriComponentsBuilder
                        .newInstance()
                        .path(USER_DEFAULT_URL + "/{user-id}")
                        .buildAndExpand(user.getId())
                        .toUri();

        return ResponseEntity.created(uri).build();
    }

    // 마이페이지 정보 제공
    @GetMapping("/{user-id}")
    public ResponseEntity getUser(@PathVariable("user-id") Long userId) {

        User user = userService.findUser(userId);

        return new ResponseEntity(userMapper.userToUserBasicResponseDto(user), HttpStatus.OK);
    }

    // 유저 상세 정보 조회
    @GetMapping("/{user-id}/details")
    public ResponseEntity getUserDetails(@PathVariable("user-id") Long userId) {

        User user = userService.findUser(userId);

        return new ResponseEntity(userMapper.userToUserDetailResponseDto(user), HttpStatus.OK);
    }

    @PatchMapping("/{user-id}")
    public ResponseEntity patchUser(@PathVariable("user-id") Long userId, @RequestBody UserPatchDto userPatchDto) {

        User user = userService.updateUser(userMapper.userPatchDtoToUser(userPatchDto));

        return new ResponseEntity(userPatchDto, HttpStatus.OK);
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity deleteUser(@PathVariable("user-id") Long userId) {

        userService.deleteUser(userId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}