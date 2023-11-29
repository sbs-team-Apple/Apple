//package com.sbs.apple.base.niceAPI;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//@Service
//public class NiceApiTokenService {
//
//    private final NiceApiConfig niceApiConfig;
//    private final RestTemplate restTemplate;
//
//    @Autowired
//    public NiceApiTokenService(NiceApiConfig niceApiConfig, RestTemplate restTemplate) {
//        this.niceApiConfig = niceApiConfig;
//        this.restTemplate = restTemplate;
//    }
//
//    public String getAccessToken() {
//        String tokenUrl = "https://svc.niceapi.co.kr:22001/digital/niceid/oauth/oauth/token";
//
//        HttpHeaders headers = createAuthorizationHeader();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        // POST 데이터 생성
//        String postData = "grant_type=client_credentials&scope=default";
//
//        // HTTP 요청 엔터티 생성
//        HttpEntity<String> requestEntity = new HttpEntity<>(postData, headers);
//
//        // 토큰 요청
//        ResponseEntity<String> responseEntity = restTemplate.exchange(
//                tokenUrl,
//                HttpMethod.POST,
//                requestEntity,
//                String.class
//        );
//
//        if (responseEntity.getStatusCode().is2xxSuccessful()) {
//            return responseEntity.getBody();
//        } else {
//            throw new RuntimeException("토큰 발급 실패: " + responseEntity.getStatusCode().toString());
//        }
//    }
//
//    private HttpHeaders createAuthorizationHeader() {
//        String clientId = niceApiConfig.getClientId();
//        String clientSecret = niceApiConfig.getClientSecret();
//
//        String credentials = clientId + ":" + clientSecret;
//        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Basic " + base64Credentials);
//
//        return headers;
//    }
//
//    public HttpHeaders getAuthorizationHeader() {
//        String accessToken = getAccessToken(); // 토큰을 얻는 메서드를 호출하여 액세스 토큰을 얻음
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken); // 액세스 토큰을 헤더에 추가
//        headers.add(HttpHeaders.CONTENT_TYPE, "application/json"); // 필요에 따라 Content-Type 설정
//
//        return headers;
//    }
//}