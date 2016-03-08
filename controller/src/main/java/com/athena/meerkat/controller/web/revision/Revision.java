/* 
 * Athena Peacock - Auto Provisioning
 * 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Tran			Mar 7, 2016		First Draft.
 */
package com.athena.meerkat.controller.web.revision;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.athena.meerkat.controller.web.domain.ClusteringConfigurationValue;
import com.athena.meerkat.controller.web.env.EnvironmentVariable;
import com.athena.meerkat.controller.web.env.EnvironmentVariableValue;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Tran
 * @version 1.0
 */

@Entity
@Table(name = "revision")
public class Revision implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "created_time")
	private Date createdTime;
	@Column(name = "is_active")
	private boolean isActive;

	@OneToMany
	private List<ClusteringConfigurationValue> clusteringConfigs;

	@OneToMany
	private List<EnvironmentVariableValue> environmentVariables;

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the environmentVariables
	 */
	public List<EnvironmentVariableValue> getEnvironmentVariables() {
		return environmentVariables;
	}

	/**
	 * @param environmentVariables
	 *            the environmentVariables to set
	 */
	public void setEnvironmentVariables(
			List<EnvironmentVariableValue> environmentVariables) {
		this.environmentVariables = environmentVariables;
	}

	/**
	 * @return the clusteringConfigs
	 */
	public List<ClusteringConfigurationValue> getClusteringConfigs() {
		return clusteringConfigs;
	}

	/**
	 * @param clusteringConfigs
	 *            the clusteringConfigs to set
	 */
	public void setClusteringConfigs(
			List<ClusteringConfigurationValue> clusteringConfigs) {
		this.clusteringConfigs = clusteringConfigs;
	}
}
// end of Revision.java