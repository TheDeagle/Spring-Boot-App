package com.example.Backend;

import com.example.Backend.Modules.User;
import com.example.Backend.Services.UserServices;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

	@Autowired
	UserServices userServices;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		Faker _faker = new Faker();
		for (int i = 0; i < 10; i++){
			System.out.print(_faker.name().firstName());
			User _user = this.userServices.createUser(_faker.name().firstName(), "Password123");
			_user.setPfpPath(_faker.avatar().image());
			this.userServices.save(_user);
		}
		User _user  = this.userServices.createUser("test", "Password123");
		this.userServices.save(_user);
	}
}

