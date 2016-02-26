/* 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Bong-Jin Kwon	2013. 9. 25.		First Draft.
 */
package com.athena.meerkat.controller.web.user;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <pre>
 * 사용자 관리 컨트롤러.
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserController.class);

	public static final String SESSION_USER_KEY = "loginUser";

	@Autowired
	private UserService service;

	@Autowired
	private PasswordEncoder passEncoder;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public UserController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<User> getUserList() {
		List<User> users = service.getList();
		return users;
	}

	@RequestMapping(value = "/rolelist", method = RequestMethod.GET)
	@ResponseBody
	public List<UserRole> getRoleList() {
		return service.getRoleList();
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public boolean saveUser(User user) {

		if (user.getId() == 0) {
			User existingUser = service.getUser(user.getUsername());
			if (existingUser != null) {
				return false;
			}

			user.setCreatedDate(new Date());
		}

		UserRole role = service.getUserRole(user.getUserRoleId());

		LOGGER.debug("user fullname is {}", user.getFullName());
		LOGGER.debug("passEncoder is {}", passEncoder.getClass()
				.getCanonicalName());

		user.setPassword(passEncoder.encode(user.getPassword()));
		user.setUserRole(role);

		if (service.saveUser(user) != null) {
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public User editUser(int id) {
		return service.findUser(id);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public List<User> searchByUserName(String userName) {
		return service.getUsers(userName);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public boolean delete(int id) {
		User user = service.findUser(id);
		if (user != null) {
			service.deleteUser(user);
			return true;
		} else {
			return false;
		}

	}

	@RequestMapping(value = "/role/delete", method = RequestMethod.POST)
	@ResponseBody
	public boolean deleteRole(int id) {
		UserRole userRole = service.getUserRole(id);
		if (userRole != null) {
			service.deleteUserRole(userRole);
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping(value = "/role/edit", method = RequestMethod.POST)
	@ResponseBody
	public UserRole editUserRole(int id) {
		return service.getUserRole(id);
	}

	@RequestMapping(value = "/role/save", method = RequestMethod.POST)
	@ResponseBody
	public boolean saveUserRole(UserRole userRole) {
		UserRole role = null;
		if (userRole.getId() > 0) { // edit
			role = service.getUserRole(userRole.getId());
		}
		// check existing user role by name

		UserRole existingRole = service.getUserRole(userRole.getName());
		if (existingRole != null) {
			if (existingRole.getId() != userRole.getId()) {
				return false;
			}
		}

		if (role == null) {
			role = userRole;
		} else {
			role.setName(userRole.getName());
		}
		if (service.saveUserRole(role) != null) {
			return true;
		}
		return false;

	}
}
// end of UserController.java