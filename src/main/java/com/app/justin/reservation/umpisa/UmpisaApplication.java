package com.app.justin.reservation.umpisa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UmpisaApplication {

	public static void main(String[] args) {
		SpringApplication.run(UmpisaApplication.class, args);
	}

}
