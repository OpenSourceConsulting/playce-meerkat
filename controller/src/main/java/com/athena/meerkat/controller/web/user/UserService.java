package com.athena.meerkat.controller.web.user;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements InitializingBean, UserDetailsService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserService.class);

	@Autowired
	private UserRepository repo;

	@Autowired
	private UserRoleRepository roleRepo;


	public List<User> getList() {
		return repo.findAll();
	}

	public User getUser(String userName) {
		return repo.findByUsername(userName);
	}

	public List<User> getUsers(String userName) {
		return repo.findByUsernameContaining(userName);
	}

	public List<UserRole> getRoleList() {
		return roleRepo.findAll();
	}

	public UserRole getUserRole(String name) {
		return roleRepo.findByName(name);
	}

	public UserRole getUserRole(int id) {
		return roleRepo.findOne(id);
	}

	public User saveUser(User user) {
		return repo.save(user);
	}

	public User findUser(int id) {
		return repo.findOne(id);
	}

	public void deleteUser(User user) {
		repo.delete(user);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LOGGER.debug("\n\nrepo in UserService : " + repo);
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		return repo.findByUsername(username);
	}

	public void updateLastLogin(int id) {
		User user = findUser(id);
		user.setLastLoginDate(new Date());
		repo.save(user);
	}

	public UserRole saveUserRole(UserRole role) {
		return roleRepo.save(role);
	}

	public void deleteUserRole(UserRole userRole) {
		repo.delete(userRole.getUsers());
		roleRepo.delete(userRole);
	}

}
