package com.cfsummit.hackathon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CfDataApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CfDataApiApplication.class, args);
	}
}
