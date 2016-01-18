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

	public List<User> getList() {
		return repo.findAll();
	}
	
	public List<User> getUsers(String userID, String email){
		return repo.findByUserNameOrEmail(userID, email);
	}
	public List<User>getUsers(String userID){
		return repo.findByUserNameContaining(userID);
	}

	public List<UserRole> getRoleList() {
		return roleRepo.findAll();
	}
	
	public UserRole getUserRole(int id){
		return roleRepo.findOne(id);
	}
	
	public User saveUser(User user){
		return repo.save(user);
	}
	
	public User findUser(int id){
		return repo.findOne(id);
	}
	
	public void deleteUser(User user){
		repo.delete(user);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		System.err.println("\n\nrepo in UserService : " + repo);
	}
	
}
