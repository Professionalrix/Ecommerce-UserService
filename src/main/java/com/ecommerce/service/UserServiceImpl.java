package com.ecommerce.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

	Logger logger = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepo.findByUserName(username);
		
		if (user == null) {
			logger.error("User not found in the database :{}", username);
		} else {
			logger.info("User found in the database :{}", username);
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});

		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(), authorities);
	}

	@Override
	public User saveUser(User user) {
		logger.info("Saving new user {} to the database", user.getUserName());
		user.setUserPassword(passwordEncoder.encode(user.getUserPassword())); 
		return userRepo.save(user);
	}

	@Override
	public Role saveRole(Role role) {
		logger.info("Saving new role {} to the database", role.getName());
		return roleRepo.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		logger.info("Adding role {}  to user {}", roleName, username);
		User user = userRepo.findByUserName(username.toLowerCase());
		Role role = roleRepo.findByName(roleName);
		user.getRoles().add(role);

	}

	@Override
	public User getUser(String name) {
		logger.info("fetching user{}", name);
		return userRepo.findByUserName(name);
	}

	@Override
	public List<User> getUsers() {
		logger.info("fetching all users ");
		return userRepo.findAll();
	}

	@Override
	public User createUser(User user) {

		return userRepo.save(user);
	}

	@Override
	public User getUser(Long id) {

		return userRepo.findById(id).get();
	}

	@Override
	public User updateUser(User user, Long id) {
		User user2 = userRepo.findById(id).get();
		user2.setUserAdd1(user.getUserAdd1());
		user2.setUserAdd2(user.getUserAdd2());
		user2.setUserEmail(user.getUserEmail());
		user2.setUserMobile(user.getUserMobile());
		user2.setUserName(user.getUserName());
		user2.setUserPassword(user.getUserPassword());
		user2.setUserPincode(user.getUserPassword());

		return userRepo.save(user2);
	}

}
