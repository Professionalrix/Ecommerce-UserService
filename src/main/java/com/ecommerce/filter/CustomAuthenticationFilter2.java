package com.ecommerce.filter;

import java.io.IOException;
import java.security.Signature;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFilter2  extends UsernamePasswordAuthenticationFilter{

	private static final String APPLICATION_JSON_VALUE = "application/json";
	private final AuthenticationManager authenticationManager;
	
	public CustomAuthenticationFilter2(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
	String username = request.getParameter("Userame");
	String password = request.getParameter("Password");

	//logger.info("Username is : {}",);
	//logger.info("Username is : {}",username);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("chetan","chetan123");
	return authenticationManager.authenticate(authenticationToken);	
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		
		User user = (User)authentication.getPrincipal();
			Algorithm alogrithm = Algorithm.HMAC256("secret".getBytes());
		String access_token =	JWT.create()
			.withSubject(user.getUsername())
			.withExpiresAt(new Date(System.currentTimeMillis()+ 10 * 60 * 1000))
			.withIssuer(request.getRequestURL().toString())
			.withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
			.sign(alogrithm);
			
		
		String refresh_token =	JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+ 30 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(alogrithm);
				
//		response.setHeader("access_token", access_token);
//		response.setHeader("refresh_token", refresh_token);
		Map<String,String> tokens = new HashMap<>();
		tokens.put("access_token",access_token);
		tokens.put("refresh_token", refresh_token);
		response.setContentType(APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(),tokens);
	}

	
	
}
