package com.intellif;

import com.intellif.aware.BeanRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@EnableAutoConfiguration
@SpringBootApplication
public class CoreInitApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(CoreInitApplication.class, args);
	}
}
