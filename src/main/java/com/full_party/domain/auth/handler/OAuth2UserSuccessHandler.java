package com.full_party.domain.auth.handler;

import com.full_party.domain.auth.jwt.JwtTokenizer;
import com.full_party.global.config.EnvConfiguration;
import com.full_party.global.exception.BusinessLogicException;
import com.full_party.domain.user.entity.User;
import com.full_party.domain.user.service.UserService;
import com.full_party.global.values.SignUpType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

@RequiredArgsConstructor
public class OAuth2UserSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;
    private static SignUpType signUpType;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes= oAuth2User.getAttributes();

        User user;

        if (attributes.containsKey("kakao_account")) {

            LinkedHashMap kakaoAccount = ((LinkedHashMap) attributes.get("kakao_account"));
            LinkedHashMap properties = ((LinkedHashMap) attributes.get("properties"));

            signUpType = SignUpType.KAKAO;

            user = saveOrFindUser(
                    String.valueOf(kakaoAccount.get("email")),
                    String.valueOf(properties.get("nickname")),
                    String.valueOf(properties.get("profile_image"))
            );

            redirect(request, response, user);
        }

        else {

            signUpType = SignUpType.GOOGLE;

            user = saveOrFindUser(
                    String.valueOf(attributes.get("email")),
                    String.valueOf(attributes.get("name")),
                    String.valueOf(attributes.get("picture"))
            );

            redirect(request, response, user);
        }
    }

    private User saveOrFindUser(String email, String userName, String profileImage) {

        User user = new User(email, userName, profileImage, signUpType);

        try {
            return userService.createUser(user);
        }
        catch (BusinessLogicException | DataIntegrityViolationException e) {
            return userService.findUser(email);
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {

        String accessToken = jwtTokenizer.delegateAccessToken(user);
        String refreshToken = jwtTokenizer.delegateRefreshToken(user);

        response.setHeader("token", accessToken);
        response.setHeader("refresh", refreshToken);

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
                .host(EnvConfiguration.getHost())
                .port(EnvConfiguration.getPort())
                .path("/auth")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
