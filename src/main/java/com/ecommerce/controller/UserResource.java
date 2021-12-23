package com.ecommerce.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import com.ecommerce.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserResource {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/test")
	public String test() {
	
		return "test";
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers(){
		return ResponseEntity.ok().body(userService.getUsers()); 
	}
	
	
	@PostMapping("/users/save")
	public ResponseEntity<User> saveUser(@RequestBody User user) {
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users/save").toString());
		return ResponseEntity.created(uri).body(userService.saveUser(user));
	}
	
	
	@PostMapping("/roles/save")
	public ResponseEntity<Role> saveRole(@RequestBody Role role) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/roles/save").toString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
	}
	
	@PostMapping("/roles/addtouser")
	public ResponseEntity<?> saveRoleToUser(@RequestBody Role role) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/roles/save").toString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
	}
	
	
	@PostMapping("/users")
	public User createUser(@RequestBody User user) {
		return userService.createUser(user);
	}
	
	@GetMapping("/users/{userId}")
	public User getUsers(@PathVariable("userId")Long userId) {
	
		return userService.getUser(userId);
	}
	
	
	@PutMapping("/users/{userId}")
	public User updateUser(@RequestBody User user, @PathVariable("userId")Long userId) {
	return userService.updateUser(user,userId);	
	}

	

}
