package com.full_party.auth.handler;

import com.full_party.auth.jwt.JwtTokenizer;
import com.full_party.exception.BusinessLogicException;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import com.full_party.values.SignUpType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;

public class OAuth2UserSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;
    private static SignUpType signUpType;

    public OAuth2UserSuccessHandler(UserService userService, JwtTokenizer jwtTokenizer) {
        this.userService = userService;
        this.jwtTokenizer = jwtTokenizer;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes= oAuth2User.getAttributes();
//
//         test

        Set<Map.Entry<String, Object>> attributesSet = oAuth2User.getAttributes().entrySet();

        for (Map.Entry<String, Object> entry : attributesSet) {
            System.out.println("🔴" + entry.getKey() + " : " + entry.getValue());
        }

        // test

        User user;

        if (attributes.containsKey("kakao_account")) {

            LinkedHashMap kakaoAccount = ((LinkedHashMap) attributes.get("kakao_account"));
            LinkedHashMap properties = ((LinkedHashMap) attributes.get("properties"));

            signUpType = SignUpType.KAKAO;

            user = saveUser(
                    String.valueOf(kakaoAccount.get("email")),
                    String.valueOf(properties.get("nickname")),
                    String.valueOf(properties.get("profile_image"))
            );

            redirect(request, response, user);
        }

        else {

            signUpType = SignUpType.GOOGLE;

            user = saveUser(
                    String.valueOf(attributes.get("email")),
                    String.valueOf(attributes.get("name")),
                    String.valueOf(attributes.get("picture"))
            );

            redirect(request, response, user);
        }
    }

    private User saveUser(String email, String userName, String profileImage) {
        User user = new User(email, userName, profileImage, signUpType);

        try {
            return userService.createUser(user);
        }
        catch (BusinessLogicException e) {
            return userService.findUser(email);
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {

        String accessToken = jwtTokenizer.delegateAccessToken(user);
        String refreshToken = jwtTokenizer.delegateRefreshToken(user);

        String uri = createURI(accessToken, refreshToken, user).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private URI createURI(String accessToken, String refreshToken, User user) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);
        queryParams.add("sign_up_by", signUpType.getType());
        queryParams.add("user_id", user.getId() + "");

        return UriComponentsBuilder
                .newInstance()
                .scheme("https")
//                .host("localhost")
//                .port(3000)
                .host("fullpartyspring.com")
//                .port("443") // 시도해보고 수정 필요
                .path("/auth")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
