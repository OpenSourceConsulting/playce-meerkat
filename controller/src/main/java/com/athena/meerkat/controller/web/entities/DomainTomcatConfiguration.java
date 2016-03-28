package com.athena.meerkat.controller.web.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Tran Ho
 * @version 2.0
 */
@Entity
@Table(name = "domain_tomcat_configuration")
@JsonIgnoreProperties(value = { "handler", "hibernateLazyInitializer" })
public class DomainTomcatConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int Id;
	@Column(name = "java_home")
	private String javaHome;
	@Column(name = "catalina_home")
	private String catalinaHome;
	@Column(name = "catalina_base")
	private String catalinaBase;
	@Column(name = "http_port")
	private int httpPort;
	@Column(name = "ajp_port")
	private int ajpPort;
	@Column(name = "session_timeout")
	private int sessionTimeout;
	@Column(name = "redirect_port")
	private int redirectPort;
	@Column(name = "jmx_enable")
	private boolean jmxEnable;
	@Column(name = "rmi_registry_port")
	private int rmiRegistryPort;
	@Column(name = "rmi_server_port")
	private int rmiServerPort;
	@Column(name = "catalina_opts")
	private String catalinaOpts;
	@Column(name = "modified_user_id")
	private int modifiedUserId;
	@Column(name = "modified_date")
	private Date modifiedDate;
	@Column(name = "tomcat_version")
	private String tomcatVersion;
	@Column(name = "encoding")
	private String encoding;

	@OneToOne
	@JoinColumn(name = "domain_id")
	@JsonBackReference(value="domain-config")
	private TomcatDomain tomcatDomain;

	public TomcatDomain getTomcatDomain() {
		return tomcatDomain;
	}

	public void setTomcatDomain(TomcatDomain tomcatDomain) {
		this.tomcatDomain = tomcatDomain;
	}

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
	
	@PrePersist
	public void onPreSave() {
		this.modifiedDate = new Date();
	}
}
