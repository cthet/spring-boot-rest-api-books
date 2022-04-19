package com.books.springbootrestapibooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@SpringBootApplication
public class SpringBootRestApiBooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestApiBooksApplication.class, args);
	}

}
