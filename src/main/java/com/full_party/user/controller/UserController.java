package com.full_party.user.controller;

import com.full_party.auth.dto.AuthDto;
import com.full_party.auth.userdetails.UserDetail;
import com.full_party.auth.userdetails.UserDetailService;
import com.full_party.party.dto.PartyResponseDto;
import com.full_party.party.mapper.PartyMapper;
import com.full_party.party.service.PartyService;
import com.full_party.user.dto.UserBasicResponseDto;
import com.full_party.user.dto.UserPatchDto;
import com.full_party.user.dto.UserPostDto;
import com.full_party.user.entity.User;
import com.full_party.user.mapper.UserMapper;
import com.full_party.user.service.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final static String USER_DEFAULT_URL = "/v1/users";
    private final UserService userService;
    private final PartyService partyService;
    private final UserMapper userMapper;
    private final PartyMapper partyMapper;

    public UserController(UserService userService, PartyService partyService, UserMapper userMapper, PartyMapper partyMapper) {
        this.userService = userService;
        this.partyService = partyService;
        this.userMapper = userMapper;
        this.partyMapper = partyMapper;
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

//    @GetMapping
//    public ResponseEntity getInitialUserInfo(@AuthenticationPrincipal UserDetails userDetail) {
//
//        User user = userService.findUser(userDetail.getUsername());
//
//        return new ResponseEntity(userMapper.userToUserBasicResponseDto(user), HttpStatus.OK);
//    }

    // 마이페이지 정보 제공
    @GetMapping("/{user-id}")
    public ResponseEntity getUser(@PathVariable("user-id") Long userId) {

        UserBasicResponseDto userBasicResponseDto = userMapper.userToUserBasicResponseDto(userService.findUser(userId));

        Map<String, List<PartyResponseDto>> relatedParties = partyMapper.mapRelatedPartyMap(
                partyService.findLeadingParty(userId),
                partyService.findParticipatingParty(userId),
                partyService.findCompletedMyParty(userId)
        );

        userBasicResponseDto.setRelatedParties(relatedParties);

        return new ResponseEntity(userBasicResponseDto, HttpStatus.OK);
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
