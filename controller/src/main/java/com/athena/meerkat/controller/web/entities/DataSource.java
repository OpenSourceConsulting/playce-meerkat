/**
 * 
 */
package com.athena.meerkat.controller.web.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Tran
 *
 */
@Entity
@Table
public class DataSource {
	@Id
	private int id;
	private String name;
	private String username;
	private String password;
	private int maxConnection;
	private int timeout;
	private int maxConnectionPool;
	private int minConnectionPool;
	private String jdbcUrl;
	private int dbtypeCdId;

	/**
	 * 
	 */
	public DataSource() {
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the maxConnection
	 */
	public int getMaxConnection() {
		return maxConnection;
	}

	/**
	 * @param maxConnection
	 *            the maxConnection to set
	 */
	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the maxConnectionPool
	 */
	public int getMaxConnectionPool() {
		return maxConnectionPool;
	}

	/**
	 * @param maxConnectionPool
	 *            the maxConnectionPool to set
	 */
	public void setMaxConnectionPool(int maxConnectionPool) {
		this.maxConnectionPool = maxConnectionPool;
	}

	/**
	 * @return the minConnectionPool
	 */
	public int getMinConnectionPool() {
		return minConnectionPool;
	}

	/**
	 * @param minConnectionPool
	 *            the minConnectionPool to set
	 */
	public void setMinConnectionPool(int minConnectionPool) {
		this.minConnectionPool = minConnectionPool;
	}

	/**
	 * @return the jdbcUrl
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	/**
	 * @param jdbcUrl
	 *            the jdbcUrl to set
	 */
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * @return the dbtypeCdId
	 */
	public int getDbtypeCdId() {
		return dbtypeCdId;
	}

	/**
	 * @param dbtypeCdId
	 *            the dbtypeCdId to set
	 */
	public void setDbtypeCdId(int dbtypeCdId) {
		this.dbtypeCdId = dbtypeCdId;
	}

}
