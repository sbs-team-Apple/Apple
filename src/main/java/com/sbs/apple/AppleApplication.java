package com.sbs.apple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class AppleApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppleApplication.class, args);

		AccessTokenRequest tokenRequest = new AccessTokenRequest();

		try {
			String tokenResponse = tokenRequest.requestToken();
			System.out.println("Token Response: " + tokenResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
