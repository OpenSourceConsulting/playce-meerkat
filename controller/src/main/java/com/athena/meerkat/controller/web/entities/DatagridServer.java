/* 
 * Copyright (C) 2012-2015 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * BongJin Kwon		2016. 5. 25.		First Draft.
 */
package com.athena.meerkat.controller.web.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Entity
@Table(name = "datagrid_servers")
@IdClass(DatagridServerPK.class)
public class DatagridServer {

	@Id
	@Column(name = "datagrid_server_group_Id")
	private int datagridServerGroupId;//
	
	@Id
	@Column(name = "server_Id")
	private int serverId;
	
	@Column(name = "port")
	private int port = 11222;//
	
	@ManyToOne
	@JoinColumn(name = "server_Id", insertable = false, updatable = false)
	private Server server;
	
	@ManyToOne
	@JoinColumn(name = "datagrid_server_group_Id", insertable = false, updatable = false)
	private DatagridServerGroup datagridServerGroup;
	

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public DatagridServer() {
		
	}
	
	public DatagridServer(int datagridServerGroupId, int serverId) {
		this.datagridServerGroupId = datagridServerGroupId;
		this.serverId = serverId;
		
		this.datagridServerGroup = new DatagridServerGroup();
		this.datagridServerGroup.setId(datagridServerGroupId);
		
		this.server = new Server();
		this.server.setId(serverId);
	}

	public DatagridServer(int datagridServerGroupId, int serverId, int port) {
		this.datagridServerGroupId = datagridServerGroupId;
		this.serverId = serverId;
		
		this.datagridServerGroup = new DatagridServerGroup();
		this.datagridServerGroup.setId(datagridServerGroupId);
		
		this.server = new Server();
		this.server.setId(serverId);
		this.port = port;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * @return the datagridServerGroupId
	 */
	public int getDatagridServerGroupId() {
		return datagridServerGroupId;
	}

	/**
	 * @param datagridServerGroupId the datagridServerGroupId to set
	 */
	public void setDatagridServerGroupId(int datagridServerGroupId) {
		this.datagridServerGroupId = datagridServerGroupId;
	}

	/**
	 * @return the serverId
	 */
	public int getServerId() {
		
		return serverId;
	}

	/**
	 * @param serverId the serverId to set
	 */
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public DatagridServerGroup getDatagridServerGroup() {
		return datagridServerGroup;
	}

	public void setDatagridServerGroup(DatagridServerGroup datagridServerGroup) {
		this.datagridServerGroup = datagridServerGroup;
	}

}
//end of DatagridServers.java