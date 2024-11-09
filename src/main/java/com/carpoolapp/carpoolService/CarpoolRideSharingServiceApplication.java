package com.carpoolapp.carpoolService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CarpoolRideSharingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarpoolRideSharingServiceApplication.class, args);
	}

}
