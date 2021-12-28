package com.ecommerce.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.MimeHeaders;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import com.ecommerce.service.UserService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@RestController
@RequestMapping("/api/v1")
public class UserResource {
	
	@Autowired
	private UserService userService;
	
	private final Logger logger =  LogManager.getLogger(UserResource.class);
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers(){
		logger.trace("home method accessed");
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
	
	@PostMapping("/role/addtousers")
	public ResponseEntity<?>saveRole(@RequestBody RoleToUserForm form){
		userService.addRoleToUser(form.getUsername(), form.getRoleName());
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException{
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
			try {
				String refresh_token = authorizationHeader.substring("Bearer ".length());
				Algorithm alogrith = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(alogrith).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String username = decodedJWT.getSubject();
				User user = userService.getUser(username);
				String access_token = JWT.create()
						.withSubject(user.getUserName())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
						.sign(alogrith);
				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);
				response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
				
			}catch (Exception exception) {
				response.setHeader("error", exception.getMessage());
				response.setStatus(FORBIDDEN.value());
				Map<String, String> error = new HashMap<>();	
				error.put("error_message", exception.getMessage());
				response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
			
			
		}else {
			throw new RuntimeException("Refresh token is missing");
		}
		
	}
	
	
}
	@Data
	class RoleToUserForm {
	private String username;
	private String roleName;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	
	}