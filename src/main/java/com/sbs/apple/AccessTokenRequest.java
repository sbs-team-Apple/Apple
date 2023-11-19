package com.sbs.apple;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;


public class AccessTokenRequest {

    public String requestToken() throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();

        String clientID = "445795d2-e19e-4da7-af8a-7634a6e68e67";
        String clientSecret = "3be8754bbb31422e1a5f7b8c97f7f7cc";
        String credentials = clientID + ":" + clientSecret;
        String base64Credentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpPost request = new HttpPost("http://svc.niceapi.co.kr:22001/digital/niceid/oauth/oauth/token");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        request.addHeader("Authorization", "Basic " + base64Credentials);

        StringEntity params = new StringEntity("grant_type=client_credentials&scope=default");
        request.setEntity(params);

        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            return responseString;
        }

        return null;
    }
}
