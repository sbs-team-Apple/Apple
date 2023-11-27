package com.sbs.apple.base.niceAPI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NiceApiConfig {

    @Value("${custom.security.oauth2.client.registration.niceapi.clientId}")
    private String clientId;

    @Value("${custom.security.oauth2.client.registration.niceapi.clientSecret}")
    private String clientSecret;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public HttpHeaders getAuthorizationHeader() {
        String credentials = getClientId() + ":" + getClientSecret();
        String base64Credentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + base64Credentials);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

        return headers;
    }
}