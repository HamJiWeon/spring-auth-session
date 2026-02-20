package com.session.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AuthDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthDemoApplication.class, args);
	}

}
