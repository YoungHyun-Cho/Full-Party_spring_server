package com.full_party.auth.controller;

import com.full_party.auth.service.AuthService;
import com.full_party.user.entity.User;
import com.full_party.user.mapper.UserMapper;
import com.full_party.user.service.UserService;
import com.full_party.util.Utility;
import com.full_party.values.SignUpType;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    public AuthController(AuthService authService, UserService userService, UserMapper userMapper) {
        this.authService = authService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/signin")
    public ResponseEntity signIn(@AuthenticationPrincipal UserDetails userDetails) {

//        @RequestParam(value = "error", required = false) boolean isError,
//        @RequestParam(value = "errMsg", required = false) String errMsg

//        if (isError) throw new BusinessLogicException(errMsg);

//        User user = userService.findUser(userDetails.getUsername());
//
//        return new ResponseEntity(userMapper.userToUserBasicResponseDto(user), HttpStatus.OK);

        User user = userService.findUser(userDetails.getUsername());

        return new ResponseEntity(userMapper.userToUserBasicResponseDto(user), HttpStatus.OK);

        /*
        * 지렸다...
        * AuthenticationFilter에서 super 추가, doFilter 추가
        * Context에 AuthResult 주입하는 코드 제거 -> 아마 super를 통해서 이루어지는 듯 하다.
        *
        * 이후 실행했으나, 아래 예외 발생
        * java.lang.IllegalStateException: getReader() has already been called for this request org.springframework.http.converter.HttpMessageNotReadableException: Could not read JSON: Stream closed; nested exception is java.io.IOException: Stream closed
        *
        * 이유 : Request Body는 단 한 번만 읽을 수 있도록 설계됨.
        * 즉, 필터 단계에서 인증을 처리하는 과정 중 이미 Request Body의 email과 password를 읽음.
        * 따라서, Controller의 핸들러 메서드 파라미터에 @Request Body를 사용하면, 두 번 째로 Request Body를 읽는 것이 되기 때문에
        * 예외가 발생함.
        * 이를 해결하기 위해 @RequestBody를 삭제. 굳이 없어도 됨.
        *
        *
        * 정리
        * - 기존에는 로그인 요청을 보낸 다음에 유저 정보를 다시 재요청하는 식으로 인증과 초기 화면 렌더링을 진행했음.
        *   - 두 번 요청을 보낼 필요가 꼭 있을까? 의문이 듦.
        * - 인증은 필터 체인에서 처리하고, 요청을 Controller에서 처리하게 하면 한 번의 요청 만으로 인증과 사용자 기본 정보 제공을 할 수 있을 것이라 생각함.
        * - 그를 위해서 AuthController 작성, 로그인 요청에 대한 핸들러 메서드 만듦.
        * - 문제 발생1 : Controller의 핸들러메서드로 요청이 넘어오지 않음.
        *   - successfulAuthentication에 doFilter가 부재했기 때문인 것으로 이해됨.
        * - 문제 발생2 : SecurityContextHolder를 통해 Authentication을 수동으로 저장할 필요가 없음. -> 수동 저장 시 핸들러로 안넘어갈 수 있음.
        *   - 해당 코드 제거
        *   - super..에 해당 코드가 이미 있는 것으로 추정
        *   - https://m.blog.naver.com/moonv11/221220018974
        * - 문제 발생3 : HttpMessage뭐시기 Exception -> RequestBody를 두 번 이상 읽을 수 없음.
        *   - AuthController의 signIn에는 굳이 Request Body를 받아올 필요가 없음.
        *   - SecurityContext에서 꺼내 쓰면 되기 때문.
        *   - @Request Body 제거하여 문제 해결.
        *
        * 굳!!
        * */
    }

    @GetMapping("/refresh")
    public ResponseEntity refresh(@RequestHeader("Refresh") String refreshToken) {

        /*
        * 액세스 토큰 재발급해주고 200 응답해야 함.
        *
        * */

        System.out.println("🟥🟥🟥 AuthController refresh");
        System.out.println("🟥🟥🟥 Refresh : " + refreshToken);

        // 액세스토큰 재발급
//        String accessToken = authService.reIssueToken(refreshToken);

        try {
            Map<String, String> tokenMap = authService.reIssueToken(refreshToken);

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            ResponseCookie accessTokenCookie = authService.createCookie("token", tokenMap.get("accessToken"), 10);
            ResponseCookie refreshTokenCookie = authService.createCookie("refresh", tokenMap.get("refreshToken"), 60);
            headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            return new ResponseEntity(headers, HttpStatus.OK);
        }
        catch (ExpiredJwtException e) {
            System.out.println("❌ " + e.getCause());
            System.out.println("❌ " + e.getMessage());
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signout")
    public ResponseEntity signOut() {

        HttpHeaders headers = new HttpHeaders();
        ResponseCookie accessTokenCookie = authService.createCookie("token", "temp");
        ResponseCookie refreshTokenCookie = authService.createCookie("refresh", "temp");
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return new ResponseEntity(HttpStatus.OK);
    }

    // 마이페이지 재인증
//    @PostMapping("/verification")
//    public ResponseEntity verifyUser() {
//
//        return new ResponseEntity(HttpStatus.OK);
//    }
//
//    @GetMapping("/error")
//    public ResponseEntity failedAuthentication(@RequestParam("errMsg") String errMsg) {
//
//        System.out.println("❌");
//
//        throw new BusinessLogicException(errMsg);
//    }

    /*
    * 문제 :
    * - /verification으로 요청 -> 인증 실패 -> error, errMsg 파라미터 붙여서 redirect -> 다시 authenticationFilter 진입
    * -
    *
    * */
}
