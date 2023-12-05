package com.sbs.apple;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HttpClientExample {
    public void sendRequest() {
        try {
            // 클라이언트 ID와 시크릿 키
            String clientId = "445795d2-e19e-4da7-af8a-7634a6e68e67";
            String clientSecret = "3be8754bbb31422e1a5f7b8c97f7f7cc";

            // Base64로 인코딩된 인증 정보 생성
            String authString = clientId + ":" + clientSecret;
            String encodedAuth = Base64.getEncoder().encodeToString(authString.getBytes());

            // 요청을 보낼 URL
            String url = "https://localhost:8090/digital/niceid/oauth/oauth/token";

            // SSLContext 초기화
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();

            // HTTPS 연결 생성
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setSSLSocketFactory(socketFactory);

            // 요청 메서드 설정
            con.setRequestMethod("POST");

            // 헤더 설정
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization", "Basic " + encodedAuth);

            // POST 파라미터 설정
            String postData = "grant_type=client_credentials&scope=default";

            // POST 데이터 전송
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
                wr.write(postDataBytes);
            }

            // 응답 읽기
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}