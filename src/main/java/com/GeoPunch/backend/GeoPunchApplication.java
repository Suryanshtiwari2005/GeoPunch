package com.GeoPunch.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.GeoPunch.backend")
public class GeoPunchApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoPunchApplication.class, args);
		System.out.println("Hello");
	}
}
