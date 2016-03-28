package com.athena.meerkat.controller.web.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.common.MeerkatUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;

//
@Entity
@Table(name = "tomcat_application")
public class TomcatApplication implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int Id;
	@Column(name = "context_path")
	private String contextPath;
	@Column(name = "deployed_time")
	private Date deployedTime;
	@Column(name = "version")
	private String version;
	@Column(name = "war_path")
	private String warPath;
	@Column(name = "last_modified_time")
	private Date lastModifiedTime;
	private int state;

	@ManyToOne(fetch = FetchType.LAZY)
	// using this annotation to prevent Infinite recursion json mapping
	@JsonBackReference(value="inst-app")
	private TomcatInstance tomcatInstance;

	@ManyToOne(fetch = FetchType.LAZY)
	// using this annotation to prevent Infinite recursion json mapping
	@JsonBackReference(value="domain-app")
	@JoinColumn(name = "domain_id")
	private TomcatDomain tomcatDomain;

	//
	// @OneToMany(mappedBy = "application")
	// @JsonManagedReference
	// private Collection<Session> sessions;

	public String getContextPath() {
		return contextPath;
	}

	public String getTomcatName() {
		if (tomcatInstance != null) {
			return tomcatInstance.getName();
		}
		return "";
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public Date getDeployedTime() {
		return deployedTime;
	}

	public String getDeployedTimeString() {
		return MeerkatUtils.dateTimeToString(deployedTime,
				MeerkatConstants.DATE_TIME_FORMATTER);
	}

	public void setDeployedDate(Date deployedTime) {
		this.setDeployedTime(deployedTime);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getWarPath() {
		return warPath;
	}

	public void setWarPath(String warPath) {
		this.warPath = warPath;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public String getLastModifiedTimeString() {
		return MeerkatUtils.dateTimeToString(lastModifiedTime,
				MeerkatConstants.DATE_TIME_FORMATTER);
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public TomcatInstance getTomcatInstance() {
		return tomcatInstance;
	}

	public void setTomcatInstance(TomcatInstance tomcat) {
		this.tomcatInstance = tomcat;
	}

	// public Collection<Session> getSessions() {
	// return sessions;
	// }
	//
	// public void setSessions(Collection<Session> sessions) {
	// this.sessions = sessions;
	// }

	/**
	 * Constructor
	 */
	public TomcatApplication() {
	}

	public TomcatApplication(String context_path, String war_path,
			String version) {
		contextPath = context_path;
		warPath = war_path;
		this.version = version;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public TomcatApplication clone() {
		try {
			return (TomcatApplication) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public TomcatDomain getTomcatDomain() {
		return tomcatDomain;
	}

	public void setTomcatDomain(TomcatDomain tomcatDomain) {
		this.tomcatDomain = tomcatDomain;
	}

	public void setDeployedTime(Date deployedTime) {
		this.deployedTime = deployedTime;
	}
}
