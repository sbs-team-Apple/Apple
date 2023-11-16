package com.sbs.apple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AccessTokenRequest {

    public String requestToken() throws IOException {
        String clientId = "445795d2-e19e-4da7-af8a-7634a6e68e67";
        String clientSecret = "3be8754bbb31422e1a5f7b8c97f7f7cc";
        String credentials = clientId + ":" + clientSecret;

        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        String apiUrl = "http://svc.niceapi.co.kr:22001/digital/niceid/oauth/oauth/token";
        String postData = "grant_type=client_credentials&scope=default";

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        connection.setRequestProperty("Authorization", "Basic " + base64Credentials);
        connection.setDoOutput(true);

        connection.getOutputStream().write(postData.getBytes(StandardCharsets.UTF_8));

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();
        } else {
            return "Token Request Failed. Response Code: " + responseCode;
        }
    }
}



