package com.athena.dolly.controller.web.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements InitializingBean {

	@Autowired
	private UserRepository repo;

	@Autowired
	private UserRoleRepository roleRepo;

	public List<User2> getList() {
		return repo.findAll();
	}
	
	public List<User2> getUsers(String userID, String email){
		return repo.findByUserNameOrEmail(userID, email);
	}

	public List<UserRole2> getRoleList() {
		return roleRepo.findAll();
	}
	
	public UserRole2 getUserRole(int id){
		return roleRepo.findOne(id);
	}
	
	public User2 addUser(User2 user){
		return repo.save(user);
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		System.err.println("\n\nrepo in UserService : " + repo);
	}
	
}
