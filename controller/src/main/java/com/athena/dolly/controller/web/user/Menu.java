package com.athena.dolly.controller.web.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "menu")
public class Menu {
	@Id
	@Column(name = "Id")
	private int Id;
	@Column(name = "text")
	private String text;
	@Column(name = "is_read")
	private boolean isRead;
	@Column(name = "is_write")
	private boolean isWrite;

	@Column(name = "is_leaf")
	private boolean isLeaf;
	@Column(name = "parent_menu_id")
	private int parentMenuId;

	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
	@JsonBackReference
	private List<UserRole> userRoles;

	public boolean isWrite() {
		return isWrite;
	}

	public void setWrite(boolean isWrite) {
		this.isWrite = isWrite;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public int getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(int parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
}
