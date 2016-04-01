/**
 * 
 */
package com.athena.meerkat.controller.web.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Tran
 *
 */
@Entity
@Table(name = "tomcat_inst_config")
public class TomcatInstConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int id;
	@Column(name = "config_name")
	private String configName;
	@Column(name = "config_value")
	private String configValue;
	@Column(name = "created_date")
	private Date createdDate;

	@ManyToOne
	@JsonIgnore
	private TomcatInstance tomcatInstance;

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
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the configName
	 */
	public String getConfigName() {
		return configName;
	}

	/**
	 * @param configName
	 *            the configName to set
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
	 * @param configValue
	 *            the configValue to set
	 */
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(java.sql.Date createdDate) {
		this.createdDate = createdDate;
	}

	public TomcatInstance getTomcatInstance() {
		return tomcatInstance;
	}

	public void setTomcatInstance(TomcatInstance tomcatInstance) {
		this.tomcatInstance = tomcatInstance;
	}

}
