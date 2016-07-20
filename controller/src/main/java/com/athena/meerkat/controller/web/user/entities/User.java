/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 8. 19.		First Draft.
 */
package com.athena.meerkat.controller.web.user.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.athena.meerkat.controller.web.common.converter.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 2.0
 */

@Entity
@Table(name = "user")
public class User implements UserDetails {

	private static final long serialVersionUID = -1083153050593982734L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int Id;
	@Column(name = "fullname")
	private String fullName;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "email")
	private String email;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@Column(name = "last_login_date")
	private Date lastLoginDate;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@Column(name = "created_date", updatable = false)
	private Date createdDate;


	@ManyToMany
	@JoinTable(name = "user_multi_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<UserRole> userRoles;

	public User(String usrName, String pwd) {
		setUsername(usrName);
		setPassword(pwd);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		if (userRoles != null) {
			for (UserRole userRole : userRoles) {
				if (userRole != null) {
					authorities.add(new SimpleGrantedAuthority(userRole.getName()));
				}
			}
		}

		return authorities;
	}

	@Override
	public String getPassword() {

		return this.password;
	}

	@Override
	public String getUsername() {

		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}

	// public void addAuthority(SimpleGrantedAuthority authority) {
	// this.authorities.add(authority);
	// }

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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
/*
	public String getLastLoginDateString() {
		return MeerkatUtils.dateTimeToString(lastLoginDate, MeerkatConstants.DATE_TIME_FORMATTER);
	}

	public String getCreatedDateString() {
		return MeerkatUtils.dateTimeToString(createdDate, MeerkatConstants.DATE_TIME_FORMATTER);
	}
*/
	public String getUserRolesString() {
		String userRolesStr = "";
		for (UserRole role : this.userRoles) {
			userRolesStr += role.getName() + ",";
		}
		int length = userRolesStr.length();
		if (length > 0) {
			userRolesStr = userRolesStr.substring(0, length - 1);
		}
		return userRolesStr;
	}

	public List<Integer> getUserRoleIds() {
		List<Integer> userIds = new ArrayList<>();
		for (UserRole role : userRoles) {
			userIds.add(role.getId());
		}
		return userIds;
	}

	public User(String _userID, String _fullName, String _password, String _email, List<UserRole> _userRoles) {
		username = _userID;
		fullName = _fullName;
		password = _password;
		email = _email;
		userRoles = _userRoles;
		createdDate = new Date();
	}

	public User() {

	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

}
// end of User.java