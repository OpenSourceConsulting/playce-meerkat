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
package com.athena.meerkat.controller.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.athena.meerkat.controller.web.datagridserver.DatagridServerGroup;
import com.athena.meerkat.controller.web.revision.Revision;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Tran
 * @version 1.0
 */
@Entity
@Table(name = "clustering_configuration_value")
public class ClusteringConfigurationValue implements Serializable {

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int Id;
	@Column(name = "value")
	private String value;
	@ManyToOne(fetch=FetchType.LAZY)
	private ClusteringConfiguration clusteringConfiguration;
	@ManyToOne(fetch=FetchType.LAZY)
	private Domain domain;
	@ManyToOne(fetch=FetchType.LAZY)
	private DatagridServerGroup serverGroup;

	@Column(name = "created_time")
	private Date createdTime;
	@ManyToOne(fetch=FetchType.LAZY)
	private Revision revision;

	/**
	 * @return the revision
	 */
	public Revision getRevision() {
		return revision;
	}

	/**
	 * @param revision
	 *            the revision to set
	 */
	public void setRevision(Revision revision) {
		this.revision = revision;
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
	 * @return the serverGroup
	 */
	public DatagridServerGroup getServerGroup() {
		return serverGroup;
	}

	/**
	 * @param serverGroup
	 *            the serverGroup to set
	 */
	public void setServerGroup(DatagridServerGroup serverGroup) {
		this.serverGroup = serverGroup;
	}

	/**
	 * @return the domain
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * @return the clusteringConfiguration
	 */
	public ClusteringConfiguration getClusteringConfiguration() {
		return clusteringConfiguration;
	}

	/**
	 * @param clusteringConfiguration
	 *            the clusteringConfiguration to set
	 */
	public void setClusteringConfiguration(
			ClusteringConfiguration clusteringConfiguration) {
		this.clusteringConfiguration = clusteringConfiguration;
	}
}
// end of ClusteringConfigurationValue.java