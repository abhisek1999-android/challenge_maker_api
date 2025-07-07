package com.abhisek.challenge_maker.challenge_maker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class ChallengeMakerApplication {
	public static void main(String[] args) {
		var context = SpringApplication.run(ChallengeMakerApplication.class, args);
	}

}
