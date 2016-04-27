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

import java.util.ArrayList;
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

import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.user.entities.User;
import com.athena.meerkat.controller.web.user.entities.UserRole;
import com.athena.meerkat.controller.web.user.services.UserService;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

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
	public SimpleJsonResponse saveUser(SimpleJsonResponse json, User user, String retypePassword, String userRoleStrIds) {

		if (user.getId() == 0) {
			User existingUser = service.getUser(user.getUsername());
			if (existingUser != null) {
				json.setMsg("Username is duplicated.");
				json.setSuccess(false);
				return json;
			}
			user.setCreatedDate(new Date());
		}

		if (!user.getPassword().equals(retypePassword)) {
			json.setMsg("Retype password does not match.");
			json.setSuccess(false);
			return json;
		}
		String[] userRolesStrArray = userRoleStrIds.split("#");
		List<Integer> userRoleIds = new ArrayList<>();
		for (String str : userRolesStrArray) {
			if (!str.equals("")) {
				userRoleIds.add(Integer.parseInt(str));
			}
		}
		List<UserRole> roles = service.getRoleList(userRoleIds);

		LOGGER.debug("user fullname is {}", user.getFullName());
		LOGGER.debug("passEncoder is {}", passEncoder.getClass().getCanonicalName());

		user.setPassword(passEncoder.encode(user.getPassword()));
		user.setUserRoles(roles);

		service.saveUser(user);
		return json;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse editUser(SimpleJsonResponse json, int id) {
		User user = service.findUser(id);
		List<UserRole> roles = user.getUserRoles();
		List<UserRole> dbRoles = service.getRoleList();
		for (UserRole role : dbRoles) {
			if (roles.contains(role)) {
				role.setSelected(true);
			}
		}
		user.setUserRoles(dbRoles);
		json.setData(user);
		return json;
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