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
@Table(name = "tomcat_application")
public class TomcatApplication {

	@Id
	private int id;
	
	private String contextPath;
	private int state;
	private java.sql.Date deployedTime;
	private String version;
	private String warPath;
	private java.sql.Date lastModifiedTime;
	private int domainId;
	private int tomcatInstanceId;

	/**
	 * 
	 */
	public TomcatApplication() {
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
	 * @return the contextPath
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * @param contextPath the contextPath to set
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the deployedTime
	 */
	public java.sql.Date getDeployedTime() {
		return deployedTime;
	}

	/**
	 * @param deployedTime the deployedTime to set
	 */
	public void setDeployedTime(java.sql.Date deployedTime) {
		this.deployedTime = deployedTime;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the warPath
	 */
	public String getWarPath() {
		return warPath;
	}

	/**
	 * @param warPath the warPath to set
	 */
	public void setWarPath(String warPath) {
		this.warPath = warPath;
	}

	/**
	 * @return the lastModifiedTime
	 */
	public java.sql.Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * @param lastModifiedTime the lastModifiedTime to set
	 */
	public void setLastModifiedTime(java.sql.Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	/**
	 * @return the domainId
	 */
	public int getDomainId() {
		return domainId;
	}

	/**
	 * @param domainId the domainId to set
	 */
	public void setDomainId(int domainId) {
		this.domainId = domainId;
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

}
