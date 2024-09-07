package com.task.verveservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VerveServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VerveServiceApplication.class, args);
	}

}
