package com.full_party.user.controller;

import com.full_party.auth.dto.AuthDto;
import com.full_party.auth.userdetails.UserDetail;
import com.full_party.auth.userdetails.UserDetailService;
import com.full_party.notification.service.NotificationService;
import com.full_party.party.dto.PartyResponseDto;
import com.full_party.party.mapper.PartyMapper;
import com.full_party.party.service.PartyService;
import com.full_party.user.dto.UserBasicResponseDto;
import com.full_party.user.dto.UserPatchDto;
import com.full_party.user.dto.UserPostDto;
import com.full_party.user.entity.User;
import com.full_party.user.mapper.UserMapper;
import com.full_party.user.service.UserService;
import com.full_party.util.Utility;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PartyService partyService;
    private final NotificationService notificationService;
    private final UserMapper userMapper;
    private final PartyMapper partyMapper;

    public UserController(UserService userService, PartyService partyService, NotificationService notificationService, UserMapper userMapper, PartyMapper partyMapper) {
        this.userService = userService;
        this.partyService = partyService;
        this.notificationService = notificationService;
        this.userMapper = userMapper;
        this.partyMapper = partyMapper;
    }

    @PostMapping
    public ResponseEntity postUser(@Valid @RequestBody UserPostDto userPostDto) {

        User user = userService.createUser(userMapper.userPostDtoToUser(userPostDto));

        URI uri =
                UriComponentsBuilder
                        .newInstance()
                        .path("/users/{user-id}")
                        .buildAndExpand(user.getId())
                        .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/name")
    public ResponseEntity checkUserName(@RequestParam("user_name") String userName) {

        userService.verifyExistsUserName(userName);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{user-id}")
    public ResponseEntity getUserInfo(@PathVariable("user-id") Long userId) {

        UserBasicResponseDto userBasicResponseDto = userMapper.userToUserBasicResponseDto(userService.findUser(userId));

        Map<String, List<PartyResponseDto>> relatedParties = partyMapper.mapRelatedPartyMap(
                partyService.findProgressingLeadingParty(userId),
                partyService.findProgressingParticipatingParty(userId),
                partyService.findCompletedMyParty(userId)
        );

        userBasicResponseDto.setRelatedParties(relatedParties);
        userBasicResponseDto.setNotificationBadge(notificationService.checkNotificationBadge(userId));

        return new ResponseEntity(userBasicResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{user-id}/details")
    public ResponseEntity getUserDetailInfo(@PathVariable("user-id") Long userId) {

        User user = userService.findUser(userId);

        return new ResponseEntity(userMapper.userToUserDetailResponseDto(user), HttpStatus.OK);
    }

    @PatchMapping("/{user-id}")
    public ResponseEntity patchUser(@PathVariable("user-id") Long userId,
                                    @RequestBody UserPatchDto userPatchDto) {

        userPatchDto.setId(userId);

        User user = userService.updateUser(userMapper.userPatchDtoToUser(userPatchDto));

        return new ResponseEntity(userMapper.userToUserDetailResponseDto(user), HttpStatus.OK);
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity deleteUser(@PathVariable("user-id") Long userId) {

        HttpHeaders headers = Utility.setCookie("temp", "temp");

        userService.deleteUser(userId);

        return ResponseEntity.noContent()
                .headers(headers)
                .build();
    }
}
