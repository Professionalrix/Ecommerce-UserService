package com.ecommerce;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import com.ecommerce.service.UserService;

@SpringBootApplication
@EnableAutoConfiguration
public class EcommerceUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceUserServiceApplication.class, args);
		
	}
	
	
	
	@Bean
	CommandLineRunner run(UserService userService) {
		return args ->{
			
			userService.saveUser(new User(null, "Kapil Sharma","kapil", "kapil123", new ArrayList<>()));
			userService.saveUser(new User(null, "Vivek Kumar","vivek", "vivek123", new ArrayList<>()));
			userService.saveUser(new User(null, "Mohit Kumar","mohit", "mohit123", new ArrayList<>()));
			userService.saveUser(new User(null, "Chetan","chetan", "chetan123", new ArrayList<>()));
			
			userService.saveRole(new Role(null,"ROLE_USER"));
			userService.saveRole(new Role(null,"ROLE_MANAGER"));
			userService.saveRole(new Role(null,"ROLE_ADMIN"));
			userService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));
			
			
			
			userService.addRoleToUser("kapil", "ROLE_USER");
			userService.addRoleToUser("kapil", "ROLE_MANAGER");
			userService.addRoleToUser("vivek", "ROLE_MANAGER");
			userService.addRoleToUser("mohit", "ROLE_ADMIN");
			userService.addRoleToUser("chetan", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("chetan", "ROLE_MANAGER");
			userService.addRoleToUser("chetan", "ROLE_USER");
		};
	}

}
