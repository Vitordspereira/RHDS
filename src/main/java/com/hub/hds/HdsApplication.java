package com.hub.hds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class HdsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HdsApplication.class, args);
	}

}
