package com.sovetnikov.application;

import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExcursionAppApplication implements ApplicationRunner {

	private final UserService userService;

	@Autowired
	public ExcursionAppApplication(UserService userService) {
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ExcursionAppApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		userService.create(new User("User", "user@yandex.ru", "9006005040", "password"));
		userService.create(new User("Admin", "admin@mail.ru", "9007008060", "password"));
	}
}
