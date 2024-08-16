package com.videohash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class VideoHashBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoHashBackendApplication.class, args);
	}

}
