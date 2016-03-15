/**
 * 
 */
package com.athena.meerkat.controller.web.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author Tran
 *
 */
@Entity
@Table(name = "tomcat_instance")
public class TomcatInstance {

	@Id
	private int id;
	private int domainId;
	private int serverId;
	private int state;
	private String name;
	private int networkInterfaceId;
	private int sshAccountId;
	private java.sql.Timestamp createdTime;
	private int createUserId;
	private String version;
	@ManyToOne
	@JsonBackReference
	private Server server;

	/**
	 * 
	 */
	public TomcatInstance() {
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
	 * @return the serverId
	 */
	public int getServerId() {
		return serverId;
	}

	/**
	 * @param serverId
	 *            the serverId to set
	 */
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state) {
		this.state = state;
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
	 * @return the networkInterfaceId
	 */
	public int getNetworkInterfaceId() {
		return networkInterfaceId;
	}

	/**
	 * @param networkInterfaceId
	 *            the networkInterfaceId to set
	 */
	public void setNetworkInterfaceId(int networkInterfaceId) {
		this.networkInterfaceId = networkInterfaceId;
	}

	/**
	 * @return the sshAccountId
	 */
	public int getSshAccountId() {
		return sshAccountId;
	}

	/**
	 * @param sshAccountId
	 *            the sshAccountId to set
	 */
	public void setSshAccountId(int sshAccountId) {
		this.sshAccountId = sshAccountId;
	}

	/**
	 * @return the createdTime
	 */
	public java.sql.Timestamp getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(java.sql.Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the createUserId
	 */
	public int getCreateUserId() {
		return createUserId;
	}

	/**
	 * @param createUserId
	 *            the createUserId to set
	 */
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

}
