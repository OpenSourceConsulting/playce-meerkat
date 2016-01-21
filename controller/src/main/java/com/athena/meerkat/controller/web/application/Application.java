package com.athena.meerkat.controller.web.application;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Id;

import com.athena.meerkat.controller.web.session.Session;
import com.athena.meerkat.controller.web.tomcat.instance.TomcatInstance;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "application")
public class Application implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "Id")
	private int Id;
	@Column(name = "context_path")
	private String contextPath;
	@Column(name = "deployed_date")
	private Date deployedDate;
	@Column(name = "version")
	private String version;
	@Column(name = "display_name")
	private String displayName;
	@Column(name = "war_path")
	private String warPath;
	@Column(name = "last_modified_date")
	private Date lastModifiedDate;
	@Column(name = "last_stopped_date")
	private Date lastStoppedDate;
	@Column(name = "last_reloaded_date")
	private Date lastReloadedDate;
	@Column(name = "last_started_date")
	private Date lastStartedDate;
	private int state;
	@ManyToOne
	// using this annotation to prevent Infinite recursion json mapping
	@JsonBackReference
	@JoinColumn(name = "tomcat_id")
	private TomcatInstance tomcat;

	//
	// @OneToMany(mappedBy = "application")
	// @JsonManagedReference
	// private Collection<Session> sessions;

	public String getContextPath() {
		return contextPath;
	}

	public String getTomcatName() {
		return tomcat.getName();
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public Date getDeployedDate() {
		return deployedDate;
	}

	public String getDeployDateString() {
		if (deployedDate == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return df.format(deployedDate);
	}

	public void setDeployedDate(Date deployedDate) {
		this.deployedDate = deployedDate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getWarPath() {
		return warPath;
	}

	public void setWarPath(String warPath) {
		this.warPath = warPath;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public String getLastModifiedDateString() {
		if (lastModifiedDate == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return df.format(lastModifiedDate);
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Date getLastStoppedDate() {
		return lastStoppedDate;
	}

	public String getLastStoppedDateString() {
		if (lastStoppedDate == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return df.format(lastStoppedDate);
	}

	public void setLastStoppedDate(Date lastStoppedDate) {
		this.lastStoppedDate = lastStoppedDate;
	}

	public Date getLastReloadedDate() {
		return lastReloadedDate;
	}

	public String getLastReloadedDateString() {
		if (lastReloadedDate == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return df.format(lastReloadedDate);
	}

	public void setLastReloaddedDate(Date lastReloaddedDate) {
		this.lastReloadedDate = lastReloaddedDate;
	}

	public Date getLastStartedDate() {
		return lastStartedDate;
	}

	public String getLastStartedDateString() {
		if (lastStartedDate == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return df.format(lastStartedDate);
	}

	public void setLastStartedDate(Date lastStartedDate) {
		this.lastStartedDate = lastStartedDate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public TomcatInstance getTomcat() {
		return tomcat;
	}

	public void setTomcat(TomcatInstance tomcat) {
		this.tomcat = tomcat;
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
	public Application() {
	}

	public Application(String name, String context_path, String war_path,
			String version) {
		displayName = name;
		contextPath = context_path;
		warPath = war_path;
		this.version = version;
	}
}
