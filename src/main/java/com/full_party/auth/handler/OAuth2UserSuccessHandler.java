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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OAuth2UserSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;

    public OAuth2UserSuccessHandler(JwtTokenizer jwtTokenizer, UserService userService) {
        this.jwtTokenizer = jwtTokenizer;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes= oAuth2User.getAttributes();

//        // test
//
//        Set<Map.Entry<String, Object>> attributes = oAuth2User.getAttributes().entrySet();
//
//        for (Map.Entry<String, Object> entry : attributes) {
//            System.out.println("🔴" + entry.getKey() + " : " + entry.getValue());
//        }
//
//        // test

        Long userId = saveUser(
                String.valueOf(attributes.get("email")),
                String.valueOf(attributes.get("name")),
                String.valueOf(attributes.get("picture"))
        );
        redirect(request, response, String.valueOf(attributes.get("email")), userId);
    }

    private Long saveUser(String email, String userName, String profileImage) {
        User user = new User(email, userName, profileImage, SignUpType.GOOGLE);

        Long userId;

        try {
            userId = userService.createUser(user).getId();
        }
        catch (BusinessLogicException e) {
            userId = userService.findUser(email).getId();
        }

        return userId;
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String username, Long userId) throws IOException {

        String accessToken = delegateAccessToken(username);
        String refreshToken = delegateRefreshToken(username);

        String uri = createURI(accessToken, refreshToken, userId).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private String delegateAccessToken(String username) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        String subject = username;
        Date expiration = jwtTokenizer.getTokenExpiration();
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    private String delegateRefreshToken(String username) {
        String subject = username;
        Date expiration = jwtTokenizer.getTokenExpiration();
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }

    private URI createURI(String accessToken, String refreshToken, Long userId) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);
        queryParams.add("sign_up_by", "google");
        queryParams.add("user_id", userId + "");

        return UriComponentsBuilder
                .newInstance()
                .scheme("https")
                .host("localhost")
                .port(3000)
                .path("/auth")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
