package com.athena.meerkat.controller.web.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Tran Ho
 * @version 2.0
 */

@Entity
@Table(name = "data_source")
public class DataSource implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int Id;
	@Column(name = "name")
	private String name;
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
	@Column(name = "dbtype_cd_id")
	private int dbType;
	
	@Transient
	private String dbTypeName;

	@Transient
	private boolean selected;
	@Transient
	private int tomcatInstancesNo;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tomcat_domain_datasource", joinColumns = @JoinColumn(name = "datasource_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tomcat_domain_id", referencedColumnName = "id"))
	@JsonIgnore
	private List<TomcatDomain> tomcatDomains;

	public int getId() {
		return Id;
	}

	public String getDbTypeName() {
		return dbTypeName;
	}

	public void setDbTypeName(String dbTypeName) {
		this.dbTypeName = dbTypeName;
	}

	public List<TomcatDomain> getTomcatDomains() {
		return tomcatDomains;
	}

	public void setTomcatDomains(List<TomcatDomain> tomcatDomains) {
		this.tomcatDomains = tomcatDomains;
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

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getDbType() {
		return dbType;
	}

	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

	public int getTomcatInstancesNo() {
		return tomcatInstancesNo;
	}

	public void setTomcatInstancesNo(int tomcatInstancesNo) {
		this.tomcatInstancesNo = tomcatInstancesNo;
	}

}
