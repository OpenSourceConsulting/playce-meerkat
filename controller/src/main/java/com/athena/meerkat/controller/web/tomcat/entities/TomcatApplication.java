package com.athena.meerkat.controller.web.tomcat.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Id;

import com.athena.meerkat.controller.web.session.Session;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	@JsonBackReference
	@JoinColumn(name = "tomcat_instance_id")
	private TomcatInstance tomcatInstance;

	//
	// @OneToMany(mappedBy = "application")
	// @JsonManagedReference
	// private Collection<Session> sessions;

	public String getContextPath() {
		return contextPath;
	}

	public String getTomcatName() {
		return tomcatInstance.getName();
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public Date getDeployedTime() {
		return deployedTime;
	}

	public String getDeployTimeString() {
		if (deployedTime == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return df.format(deployedTime);
	}

	public void setDeployedDate(Date deployedTime) {
		this.deployedTime = deployedTime;
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
		if (lastModifiedTime == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return df.format(lastModifiedTime);
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
}
