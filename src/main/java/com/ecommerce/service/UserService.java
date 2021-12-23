package com.ecommerce.service;

import java.util.List;

import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;

public interface UserService {
	
	User saveUser(User user);
	Role saveRole(Role role);
	void addRoleToUser(String username, String roleName);
	User getUser(String name);
	List<User> getUsers();
	User getUser(Long id);
	User updateUser(User user, Long id);
	User createUser(User user);

}
