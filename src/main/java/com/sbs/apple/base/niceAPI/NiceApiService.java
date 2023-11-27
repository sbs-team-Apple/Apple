//package com.sbs.apple.base.niceAPI;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class NiceApiService {
//
//    private final NiceApiTokenService niceApiTokenService;
//    private final RestTemplate restTemplate;
//
//    @Autowired
//    public NiceApiService(NiceApiTokenService niceApiTokenService, RestTemplate restTemplate) {
//        this.niceApiTokenService = niceApiTokenService;
//        this.restTemplate = restTemplate;
//    }
//
//    public String performIdentityVerification() {
//        // NiceAPI 본인확인 API 엔드포인트 URL
//        String apiEndpoint = "https://svc.niceapi.co.kr/identity/verify";
//
//        // 액세스 토큰을 헤더에 추가
//        HttpHeaders headers = niceApiTokenService.getAuthorizationHeader();
//
//        // API 요청 시 필요한 데이터 (예시)
//        String requestData = "user_id=12345";
//
//        // HTTP 요청 엔터티 생성
//        HttpEntity<String> requestEntity = new HttpEntity<>(requestData, headers);
//
//        // NiceAPI의 본인확인 API 호출
//        ResponseEntity<String> responseEntity = restTemplate.exchange(
//                apiEndpoint,
//                HttpMethod.POST,
//                requestEntity,
//                String.class
//        );
//
//        // API 응답 처리 (예시)
//        if (responseEntity.getStatusCode().is2xxSuccessful()) {
//            String responseBody = responseEntity.getBody();
//            // 본인확인 성공 시의 처리
//            return "본인확인 성공: " + responseBody;
//        } else {
//            // 본인확인 실패 시의 처리
//            return "본인확인 실패: " + responseEntity.getStatusCode().toString();
//        }
//    }
//}
//
