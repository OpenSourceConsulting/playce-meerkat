/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
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

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;

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
		// TODO Auto-generated constructor stub
	}

	@RequestMapping("/onAfterLogout")
	@ResponseBody
	public SimpleJsonResponse logout(SimpleJsonResponse jsonRes,
			HttpSession session) {

		session.invalidate();

		jsonRes.setMsg("로그아웃 되었습니다.");

		return jsonRes;
	}

	@RequestMapping("/onAfterLogin")
	@ResponseBody
	public SimpleJsonResponse onAfterLogin(SimpleJsonResponse jsonRes) {

		User loginUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		jsonRes.setData(loginUser);
		service.updateLastLogin(loginUser.getId());
		return jsonRes;
	}

	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@RequestMapping("/notLogin")
	@ResponseBody
	public SimpleJsonResponse notLogin(SimpleJsonResponse jsonRes) {

		jsonRes.setSuccess(false);
		jsonRes.setMsg("로그인 정보가 없습니다. 관리자에게 문의하세요.");
		jsonRes.setData("notLogin");

		return jsonRes;
	}

	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@RequestMapping("/accessDenied")
	@ResponseBody
	public SimpleJsonResponse accessDenied(SimpleJsonResponse jsonRes) {

		jsonRes.setSuccess(false);
		jsonRes.setMsg("해당 작업에 대한 권한이 없습니다. 관리자에게 문의하세요.");

		return jsonRes;
	}

	@RequestMapping("/loginFail")
	public @ResponseBody
	SimpleJsonResponse loginFail(SimpleJsonResponse jsonRes) {

		jsonRes.setSuccess(false);
		jsonRes.setMsg("login ID 또는 password 가 잘못되었습니다.");

		return jsonRes;
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	public List<User> getUserList() {
		List<User> users = service.getList();
		return users;
	}

	@RequestMapping("/rolelist")
	@ResponseBody
	public List<UserRole> getRoleList() {
		return service.getRoleList();
	}

	@RequestMapping("/save")
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
		LOGGER.debug("passEncoder is {}", passEncoder.getClass().getCanonicalName());
		
		user.setPassword(passEncoder.encode(user.getPassword()));
		user.setUserRole(role);

		
		if (service.saveUser(user) != null) {
			return true;
		}
		return false;
	}

	@RequestMapping("/edit")
	@ResponseBody
	public User editUser(int id) {
		return service.findUser(id);
	}

	@RequestMapping("/search")
	@ResponseBody
	public List<User> searchByUserName(String userName) {
		return service.getUsers(userName);
	}

	@RequestMapping("/delete")
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

	@RequestMapping("/role/delete")
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

	@RequestMapping("/role/edit")
	@ResponseBody
	public UserRole editUserRole(int id) {
		return service.getUserRole(id);
	}

	@RequestMapping("/role/save")
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