package com.intellif;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@EnableAutoConfiguration
@SpringBootApplication
public class CoreInitApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreInitApplication.class, args);
	}
}
