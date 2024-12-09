package com.movietheater.movietheater_mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MovietheaterMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovietheaterMvcApplication.class, args);
	}

}
