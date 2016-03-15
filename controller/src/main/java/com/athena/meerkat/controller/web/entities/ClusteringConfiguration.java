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
@Table(name = "clustering_configuration")
public class ClusteringConfiguration {
	@Id
	private int id;
	private String name;
	private String value;
	private java.sql.Date createdTime;
	private int datagridServerGroupId;
	private int domainId;
	private int version;

	/**
	 * 
	 */
	public ClusteringConfiguration() {
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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the createdTime
	 */
	public java.sql.Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(java.sql.Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the datagridServerGroupId
	 */
	public int getDatagridServerGroupId() {
		return datagridServerGroupId;
	}

	/**
	 * @param datagridServerGroupId
	 *            the datagridServerGroupId to set
	 */
	public void setDatagridServerGroupId(int datagridServerGroupId) {
		this.datagridServerGroupId = datagridServerGroupId;
	}

	/**
	 * @return the domainId
	 */
	public int getDomainId() {
		return domainId;
	}

	/**
	 * @param domainId
	 *            the domainId to set
	 */
	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

}
