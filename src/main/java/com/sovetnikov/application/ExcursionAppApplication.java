package com.sovetnikov.application;

import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExcursionAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExcursionAppApplication.class, args);
	}
}
