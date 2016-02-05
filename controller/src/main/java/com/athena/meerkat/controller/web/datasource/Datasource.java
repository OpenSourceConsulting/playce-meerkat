package com.athena.meerkat.controller.web.datasource;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import javax.persistence.Transient;

import com.athena.meerkat.controller.web.tomcat.instance.TomcatInstance;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "data_source")
public class Datasource {
	@Id
	@Column(name = "Id")
	private int Id;
	@Column(name = "name")
	private String name;
	@Column(name = "database_type")
	private String dbType;
	@Column(name = "username")
	private String userName;
	@Column(name = "password")
	private String password;
	@Column(name = "max_connection")
	private int maxConnection;
	@Column(name = "timeout")
	private int timeout;
	@Column(name = "max_connection_pool")
	private int maxConnectionPool;
	@Column(name = "min_connection_pool")
	private int minConnectionPool;
	@Column(name = "jdbc_url")
	private String jdbcUrl;

	@Transient
	private boolean selected;

	@JoinTable(name = "tomcat_datasource", joinColumns = { @JoinColumn(name = "datasource_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "tomcat_id", referencedColumnName = "id") })
	@ManyToMany(fetch = FetchType.LAZY)
	@JsonBackReference
	private Collection<TomcatInstance> tomcatInstances;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxConnection() {
		return maxConnection;
	}

	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getMaxConnectionPool() {
		return maxConnectionPool;
	}

	public void setMaxConnectionPool(int maxConnectionPool) {
		this.maxConnectionPool = maxConnectionPool;
	}

	public int getMinConnectionPool() {
		return minConnectionPool;
	}

	public void setMinConnectionPool(int minConnectionPool) {
		this.minConnectionPool = minConnectionPool;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public Collection<TomcatInstance> getTomcatInstances() {
		return tomcatInstances;
	}

	public void setTomcatInstances(Collection<TomcatInstance> tomcatInstances) {
		this.tomcatInstances = tomcatInstances;
	}

	public void associateTomcat(TomcatInstance tomcat) {
		this.tomcatInstances.add(tomcat);
	}

	public void removeTomcat(TomcatInstance tomcat) {
		this.tomcatInstances.remove(tomcat);
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
