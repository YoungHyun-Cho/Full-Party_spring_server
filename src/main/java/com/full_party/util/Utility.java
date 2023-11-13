package com.full_party.util;

import com.full_party.auth.userdetails.UserDetail;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

public class Utility {

    public static Long getUserId(UserDetails userDetails) {
        return ((UserDetail) userDetails).getId();
    }

    public static <T> ResponseEntity<T> sendRequest(HttpMethod httpMethod, String url, HttpHeaders headers, Class<T> classType) {

        System.out.println("🟥 sendRequest");
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
//        return restTemplate.getForObject(url, classType);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // 요청 보내기
        ResponseEntity<T> responseEntity = restTemplate.exchange(
                url,
                httpMethod,
                requestEntity,
                classType
        );

        // 응답 처리
//        T responseBody = responseEntity.getBody();
        System.out.println(responseEntity.getBody());

        return responseEntity;
    }
}
