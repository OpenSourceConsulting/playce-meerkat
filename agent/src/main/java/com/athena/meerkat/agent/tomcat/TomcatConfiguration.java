package com.athena.meerkat.agent.tomcat;

import java.util.Date;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Tran Ho
 * @version 2.0
 */
public class TomcatConfiguration {

	private int Id;

	private String javaHome;

	private String catalinaHome;

	private String catalinaBase;

	private int httpPort;
	private int ajpPort;
	private int sessionTimeout;
	private int redirectPort;
	private boolean jmxEnable;
	private int rmiRegistryPort;
	private int rmiServerPort;
	private String catalinaOpts;
	private int modifiedUserId;
	private Date modifiedDate;
	private String tomcatVersion;
	private String encoding;


	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getModifiedUserId() {
		return modifiedUserId;
	}

	public void setModifiedUserId(int modifiedUserId) {
		this.modifiedUserId = modifiedUserId;
	}

	public String getCatalinaOpts() {
		return catalinaOpts;
	}

	public void setCatalinaOpts(String catalinaOpts) {
		this.catalinaOpts = catalinaOpts;
	}

	public int getRmiServerPort() {
		return rmiServerPort;
	}

	public void setRmiServerPort(int rmiServerPort) {
		this.rmiServerPort = rmiServerPort;
	}

	public int getRmiRegistryPort() {
		return rmiRegistryPort;
	}

	public void setRmiRegistryPort(int rmiRegistryPort) {
		this.rmiRegistryPort = rmiRegistryPort;
	}

	public boolean isJmxEnable() {
		return jmxEnable;
	}

	public void setJmxEnable(boolean jmxEnable) {
		this.jmxEnable = jmxEnable;
	}

	public int getRedirectPort() {
		return redirectPort;
	}

	public void setRedirectPort(int redirectPort) {
		this.redirectPort = redirectPort;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public int getAjpPort() {
		return ajpPort;
	}

	public void setAjpPort(int ajpPort) {
		this.ajpPort = ajpPort;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public String getCatalinaBase() {
		return catalinaBase;
	}

	public void setCatalinaBase(String catalinaBase) {
		this.catalinaBase = catalinaBase;
	}

	public String getCatalinaHome() {
		return catalinaHome;
	}

	public void setCatalinaHome(String catalinaHome) {
		this.catalinaHome = catalinaHome;
	}

	public String getJavaHome() {
		return javaHome;
	}

	public void setJavaHome(String javaHome) {
		this.javaHome = javaHome;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getTomcatVersion() {
		return tomcatVersion;
	}

	public void setTomcatVersion(String tomcatVersion) {
		this.tomcatVersion = tomcatVersion;
	}
	
}
