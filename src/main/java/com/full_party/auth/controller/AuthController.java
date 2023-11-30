package com.full_party.auth.controller;

import com.full_party.auth.service.AuthService;
import com.full_party.user.entity.User;
import com.full_party.user.mapper.UserMapper;
import com.full_party.user.service.UserService;
import com.full_party.util.Utility;
import com.full_party.values.SignUpType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/signin")
    public ResponseEntity signIn(@AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findUser(userDetails.getUsername());

        return new ResponseEntity(userMapper.userToUserBasicResponseDto(user), HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity refresh(@RequestHeader("Refresh") String refreshToken) {

        Map<String, String> tokenMap = authService.reIssueTokens(refreshToken);

        HttpHeaders headers = Utility.setCookie(
                tokenMap.get("accessToken"),
                tokenMap.get("refreshToken")
        );

        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/signout")
    public ResponseEntity signOut(@AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Utility.getUserId(userDetails);
        User foundUser = userService.findUser(userId);

        if (foundUser.getSignUpType() == SignUpType.GUEST) {
            userService.deleteUser(foundUser);
        }

        HttpHeaders headers = Utility.setCookie("temp", "temp");

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

    @PostMapping("/verification")
    public ResponseEntity verifyUser() {

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/guest")
    public ResponseEntity guestSignIn() {

        User createdGuestUser = authService.createGuestUser();

        HttpHeaders headers = Utility.setCookie(
                authService.issueAccessToken(createdGuestUser),
                authService.issueRefreshToken(createdGuestUser)
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(userMapper.userToUserBasicResponseDto(createdGuestUser));
    }
}
