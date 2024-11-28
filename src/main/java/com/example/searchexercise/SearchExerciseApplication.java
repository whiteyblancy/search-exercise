package com.example.searchexercise;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class SearchExerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchExerciseApplication.class, args);
	}

	@Value("${base.url}")
	private String baseUrl;

	@Bean
	public RestClient restClient(RestClient.Builder builder) {
		return builder.baseUrl(baseUrl).build();
	}

}
