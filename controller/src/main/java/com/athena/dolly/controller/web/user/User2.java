package com.athena.dolly.controller.web.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "user")
public class User2 {

	@Id
	@Column(name = "Id")
	private int Id;
	@Column(name = "fullname")
	private String fullName;
	@Column(name = "username")
	private String userName;
	@Column(name = "password")
	private String password;
	@Column(name = "email")
	private String email;
	@Column(name = "last_login_date")
	private Date lastLoginDate;
	@Column(name = "created_date")
	private Date createdDate;

	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "user_role_id")
	private UserRole2 userRole;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public UserRole2 getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole2 userRole) {
		this.userRole = userRole;
	}

	public String getUserRoleName()
	{
		return this.userRole.getName();
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastLoginDateString() {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return df.format(createdDate);
	}

	public String getCreatedDateString() {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return df.format(createdDate);
	}

	public String getUserRoleString() {
		return userRole.getName();
	}

	public User2(String _userID, String _fullName, String _password, String _email, UserRole2 _userRole){
		userName = _userID;
		fullName = _fullName;
		password =_password;
		email = _email;
		userRole = _userRole;
		createdDate = new Date();
	}
	public User2(){
		
	}
}
