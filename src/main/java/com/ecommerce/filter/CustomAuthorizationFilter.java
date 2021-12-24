package com.ecommerce.filter;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION = "Authorization";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getServletPath().equals("api/v1/login")) {
				filterChain.doFilter(request, response);
			
		} else {
				String authorizationHeader = request.getHeader(AUTHORIZATION);
				if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
					try {
					
						String token = authorizationHeader.substring("Bearer".length());
						Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
						JWTVerifier verifier = JWT.require(algorithm).build();
						DecodedJWT decodedJWT = verifier.verify(token);
						String username = decodedJWT.getSubject();
						 String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
						 Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
						
						stream(roles).forEach(role->{
							authorities.add(new SimpleGrantedAuthority(role));
						});
						 
						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);
						filterChain.doFilter(request, response);
					} catch (Exception e) {
					
					}
										
				}
		}		
		
	}

}
