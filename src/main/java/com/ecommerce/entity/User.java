package com.ecommerce.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable{
	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="name")
	private String userName;
	
	@Column(name="full_name")
	private String fullName;
	
	@Column(name="email")
	private String  userEmail;
	
	@Column(name="password")
	private String userPassword;
	
	@Column(name="role")
	@ManyToMany(fetch = FetchType.EAGER)
	Collection<Role> roles = new ArrayList<>();
	
	@Column(name="mobile")
	private String userMobile;
	
	@Column(name="add1")
	private String userAdd1;
	
	@Column(name="add2")
	private String userAdd2;
	
	@Column(name="pincode")
	private String userPincode;
	
	
	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}
	
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	public String getUserMobile() {
		return userMobile;
	}
	
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	
	public String getUserAdd1() {
		return userAdd1;
	}
	
	public void setUserAdd1(String userAdd1) {
		this.userAdd1 = userAdd1;
	}

	public String getUserAdd2() {
		return userAdd2;
	}
	
	public void setUserAdd2(String userAdd2) {
		this.userAdd2 = userAdd2;
	}
	
	public String getUserPincode() {
		return userPincode;
	}
	
	public void setUserPincode(String userPincode) {
		this.userPincode = userPincode;
	}

	public User() {
		super();
	
	}

	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public User(Long userId, String fullName,String userName, String userPassword, Collection<Role> roles) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.roles = roles;
	}
	
	
	
	
}
