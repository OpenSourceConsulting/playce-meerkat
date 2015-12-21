package com.athena.dolly.controller.web.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "user_role")
public class UserRole2 {

	@Column(name = "Id")
	@Id
	private int Id;
	@Column(name = "name")
	private String name;
	@OneToMany(mappedBy = "userRole", fetch = FetchType.LAZY)
	@JsonBackReference
	private List<User2> users;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User2> getUsers() {
		return users;
	}

	public void setUsers(List<User2> users) {
		this.users = users;
	}
	public int getUserCount(){
		return users.size();
	}

}
