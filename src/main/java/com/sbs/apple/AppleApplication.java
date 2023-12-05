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

		// HttpClientExample를 사용하여 sendRequest() 메소드 호출
		HttpClientExample httpClient = new HttpClientExample();
		httpClient.sendRequest();
	}
}
