/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Bong-Jin Kwon	2015. 1. 9.		First Draft.
 */
package com.athena.meerkat.controller.web.tomcat.instance;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.application.Application;
import com.athena.meerkat.controller.web.datasource.Datasource;
import com.athena.meerkat.controller.web.domain.Domain;
import com.athena.meerkat.controller.web.machine.Machine;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Tran Ho
 * @version 2.0
 */
@Entity
@Table(name = "tomcat")
public class TomcatInstance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "Id")
	private int Id;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(nullable = false, name = "http_port")
	private int httpPort;
	@Column(nullable = false, name = "ajp_port")
	private int ajpPort;
	@Column(nullable = false, name = "redirect_port")
	private int redirectPort;
	@Column(nullable = false, name = "web_server")
	private String webServer;

	@ManyToOne
	// using this annotation to prevent Infinite recursion json mapping
	@JsonBackReference
	private Machine machine;
	@ManyToOne
	// using this annotation to prevent Infinite recursion json mapping
	@JsonBackReference
	@JoinColumn(name = "domain_id")
	private Domain domain;

	@ManyToMany(mappedBy = "tomcat")
	// using this annotation to prevent Infinite recursion json mapping
	@JsonManagedReference
	private Collection<Application> applications;

	@JoinTable(name = "tomcat_datasource", joinColumns = { @JoinColumn(name = "tomcat_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "datasource_id", referencedColumnName = "id") })
	@ManyToMany(fetch = FetchType.LAZY)
	@JsonManagedReference
	private Collection<Datasource> datasources;

	private int state;

	public TomcatInstance() {
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String instanceName) {
		this.name = instanceName;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public int getAjpPort() {
		return ajpPort;
	}

	public void setAjpPort(int ajpPort) {
		this.ajpPort = ajpPort;
	}

	public int getRedirectPort() {
		return redirectPort;
	}

	public void setRedirectPort(int redirectPort) {
		this.redirectPort = redirectPort;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Collection<Application> getApplications() {
		return applications;
	}

	public void setApplications(Collection<Application> applications) {
		this.applications = applications;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Collection<Datasource> getDatasources() {
		return datasources;
	}

	public void setDatasources(Collection<Datasource> datasources) {
		this.datasources = datasources;
	}

	public void associateDatasource(Datasource ds) {
		this.datasources.add(ds);
	}

	public void associateDatasources(List<Datasource> dss) {
		this.datasources.addAll(dss);
	}

	public void removeDatasource(Datasource ds) {
		this.datasources.remove(ds);
	}

	public void removeDatasources(List<Datasource> dss) {
		this.datasources.removeAll(dss);
	}

	public String getHostName() {
		return machine.getHostName();
	}

	public String getIPAddress() {
		return machine.getSSHIPAddr();// not sure it's correct
	}

	public String getStatusString() {
		switch (this.state) {
		case MeerkatConstants.TOMCAT_STATUS_RUNNING:
			return "Running";
		case MeerkatConstants.TOMCAT_STATUS_SHUTDOWN:
			return "Stopped";
		default:
			return "Unknown";
		}
	}

	public String getWebServer() {
		return webServer;
	}

	public void setWebServer(String webServer) {
		this.webServer = webServer;
	}

	public String getOSName() {
		if (machine != null) {
			return machine.getOsName();
		}
		return "";
	}

	public String getJvm() {
		if (machine != null) {
			return machine.getJvmVersion();
		}
		return "";
	}

	public String getDomainName() {
		if (domain != null) {
			return domain.getName();
		}
		return "";
	}

}
