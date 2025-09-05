package com.cleansolution.illkam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IllkamApplication {

	public static void main(String[] args) {
		SpringApplication.run(IllkamApplication.class, args);
	}
}
