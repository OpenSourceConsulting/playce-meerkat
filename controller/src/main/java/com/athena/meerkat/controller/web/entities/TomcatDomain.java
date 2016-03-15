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
@Table(name="tomcat_domain")
public class TomcatDomain {

	@Id
	private int id;
	private String name;
	private Integer datagridServerGroupId;
	private Integer createUserId;

	/**
	 * 
	 */
	public TomcatDomain() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the datagridServerGroupId
	 */
	public Integer getDatagridServerGroupId() {
		return datagridServerGroupId;
	}

	/**
	 * @param datagridServerGroupId the datagridServerGroupId to set
	 */
	public void setDatagridServerGroupId(Integer datagridServerGroupId) {
		this.datagridServerGroupId = datagridServerGroupId;
	}

	/**
	 * @return the createUserId
	 */
	public Integer getCreateUserId() {
		return createUserId;
	}

	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

}
