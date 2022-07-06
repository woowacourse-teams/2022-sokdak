package com.wooteco.sokdak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SokdakApplication {

	public static void main(String[] args) {
		SpringApplication.run(SokdakApplication.class, args);
	}

}
