/**
 * 
 */
package com.athena.meerkat.controller.web.entities;

import javax.persistence.Id;

/**
 * @author Tran
 *
 */
public class UserRole {
	@Id
	private int id;
	private String name;

	/**
	 * 
	 */
	public UserRole() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
