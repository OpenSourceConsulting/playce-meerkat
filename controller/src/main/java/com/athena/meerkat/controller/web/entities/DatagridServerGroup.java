/**
 * 
 */
package com.athena.meerkat.controller.web.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Tran
 *
 */
@Entity
@Table
public class DatagridServerGroup {
	@Id
	private int id;
	private String name;
	private int typeCdId;

	/**
	 * 
	 */
	public DatagridServerGroup() {
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

	/**
	 * @return the typeCdId
	 */
	public int getTypeCdId() {
		return typeCdId;
	}

	/**
	 * @param typeCdId
	 *            the typeCdId to set
	 */
	public void setTypeCdId(int typeCdId) {
		this.typeCdId = typeCdId;
	}

}
