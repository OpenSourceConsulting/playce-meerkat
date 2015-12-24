package com.athena.dolly.controller.web.user;

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
	public List<User2>getUsers(String userID){
		return repo.findByUserNameContaining(userID);
	}

	public List<UserRole2> getRoleList() {
		return roleRepo.findAll();
	}
	
	public UserRole2 getUserRole(int id){
		return roleRepo.findOne(id);
	}
	
	public User2 saveUser(User2 user){
		return repo.save(user);
	}
	
	public User2 findUser(int id){
		return repo.findOne(id);
	}
	
	public void deleteUser(User2 user){
		repo.delete(user);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		System.err.println("\n\nrepo in UserService : " + repo);
	}
	
}
