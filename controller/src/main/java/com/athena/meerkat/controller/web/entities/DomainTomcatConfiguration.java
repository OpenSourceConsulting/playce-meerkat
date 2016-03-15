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
public class DomainTomcatConfiguration {
	@Id
	private int id;
	private int domainId;
	private String javaHome;
	private String catalinaHome;
	private String catalinaBase;
	private int httpPort;
	private int sessionTimeout;
	private int ajpPort;
	private int redirectPort;
	private boolean jmxEnable;
	private int rmiRegistryPort;
	private int rmiServerPort;
	private String catalinaOpts;
	private int modifiedUserId;
	private java.sql.Date modifiedDate;

	/**
	 * 
	 */
	public DomainTomcatConfiguration() {
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
	 * @return the javaHome
	 */
	public String getJavaHome() {
		return javaHome;
	}

	/**
	 * @param javaHome
	 *            the javaHome to set
	 */
	public void setJavaHome(String javaHome) {
		this.javaHome = javaHome;
	}

	/**
	 * @return the catalinaHome
	 */
	public String getCatalinaHome() {
		return catalinaHome;
	}

	/**
	 * @param catalinaHome
	 *            the catalinaHome to set
	 */
	public void setCatalinaHome(String catalinaHome) {
		this.catalinaHome = catalinaHome;
	}

	/**
	 * @return the catalinaBase
	 */
	public String getCatalinaBase() {
		return catalinaBase;
	}

	/**
	 * @param catalinaBase
	 *            the catalinaBase to set
	 */
	public void setCatalinaBase(String catalinaBase) {
		this.catalinaBase = catalinaBase;
	}

	/**
	 * @return the httpPort
	 */
	public int getHttpPort() {
		return httpPort;
	}

	/**
	 * @param httpPort
	 *            the httpPort to set
	 */
	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	/**
	 * @return the sessionTimeout
	 */
	public int getSessionTimeout() {
		return sessionTimeout;
	}

	/**
	 * @param sessionTimeout
	 *            the sessionTimeout to set
	 */
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	/**
	 * @return the ajpPort
	 */
	public int getAjpPort() {
		return ajpPort;
	}

	/**
	 * @param ajpPort
	 *            the ajpPort to set
	 */
	public void setAjpPort(int ajpPort) {
		this.ajpPort = ajpPort;
	}

	/**
	 * @return the redirectPort
	 */
	public int getRedirectPort() {
		return redirectPort;
	}

	/**
	 * @param redirectPort
	 *            the redirectPort to set
	 */
	public void setRedirectPort(int redirectPort) {
		this.redirectPort = redirectPort;
	}

	/**
	 * @return the jmxEnable
	 */
	public boolean getJmxEnable() {
		return jmxEnable;
	}

	/**
	 * @param jmxEnable
	 *            the jmxEnable to set
	 */
	public void setJmxEnable(boolean jmxEnable) {
		this.jmxEnable = jmxEnable;
	}

	/**
	 * @return the rmiRegistryPort
	 */
	public int getRmiRegistryPort() {
		return rmiRegistryPort;
	}

	/**
	 * @param rmiRegistryPort
	 *            the rmiRegistryPort to set
	 */
	public void setRmiRegistryPort(int rmiRegistryPort) {
		this.rmiRegistryPort = rmiRegistryPort;
	}

	/**
	 * @return the rmiServerPort
	 */
	public int getRmiServerPort() {
		return rmiServerPort;
	}

	/**
	 * @param rmiServerPort
	 *            the rmiServerPort to set
	 */
	public void setRmiServerPort(int rmiServerPort) {
		this.rmiServerPort = rmiServerPort;
	}

	/**
	 * @return the catalinaOpts
	 */
	public String getCatalinaOpts() {
		return catalinaOpts;
	}

	/**
	 * @param catalinaOpts
	 *            the catalinaOpts to set
	 */
	public void setCatalinaOpts(String catalinaOpts) {
		this.catalinaOpts = catalinaOpts;
	}

	/**
	 * @return the modifiedUserId
	 */
	public int getModifiedUserId() {
		return modifiedUserId;
	}

	/**
	 * @param modifiedUserId
	 *            the modifiedUserId to set
	 */
	public void setModifiedUserId(int modifiedUserId) {
		this.modifiedUserId = modifiedUserId;
	}

	/**
	 * @return the modifiedDate
	 */
	public java.sql.Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate
	 *            the modifiedDate to set
	 */
	public void setModifiedDate(java.sql.Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
