package com.full_party.domain.auth.jwt;

import com.full_party.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenizer {

    @Getter
    @Value("${jwt.key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    public Jws<Claims> getClaims(String jws) {

        String key = encodeBase64SecretKey();

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);

        return claims;
    }

    private static Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return key;
    }

    public String delegateAccessToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getEmail());

        String subject = user.getEmail();
        Date expiration = getAccessTokenExpiration();
        String base64EncodedSecretKey = encodeBase64SecretKey();

        return generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
    }

    public String delegateRefreshToken(User user) {

        String subject = user.getEmail();
        Date expiration = getRefreshTokenExpiration();
        String base64EncodedSecretKey = encodeBase64SecretKey();

        return generateRefreshToken(subject, expiration, base64EncodedSecretKey);
    }

    private static String generateAccessToken(Map<String, Object> claims, String subject,
                                              Date expiration, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    private static String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {

        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    private String encodeBase64SecretKey() {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Date getAccessTokenExpiration() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, accessTokenExpirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }

    private Date getRefreshTokenExpiration() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, refreshTokenExpirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }
}
