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
public class TomcatInstConfig {
	@Id
	private int id;
	private int tomcatInstanceId;
	private String configName;
	private String configValue;
	private java.sql.Date createdDate;

	/**
	 * 
	 */
	public TomcatInstConfig() {
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
	 * @return the tomcatInstanceId
	 */
	public int getTomcatInstanceId() {
		return tomcatInstanceId;
	}

	/**
	 * @param tomcatInstanceId the tomcatInstanceId to set
	 */
	public void setTomcatInstanceId(int tomcatInstanceId) {
		this.tomcatInstanceId = tomcatInstanceId;
	}

	/**
	 * @return the configName
	 */
	public String getConfigName() {
		return configName;
	}

	/**
	 * @param configName the configName to set
	 */
	public void setConfigName(String configName) {
		this.configName = configName;
	}

	/**
	 * @return the configValue
	 */
	public String getConfigValue() {
		return configValue;
	}

	/**
	 * @param configValue the configValue to set
	 */
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	/**
	 * @return the createdDate
	 */
	public java.sql.Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(java.sql.Date createdDate) {
		this.createdDate = createdDate;
	}

}
