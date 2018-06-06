package com.intellif;

import com.intellif.annotation.EnableLoggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@EnableAutoConfiguration
@SpringBootApplication
@EnableLoggerConfiguration
public class CoreInitApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreInitApplication.class, args);
	}
}
