package com.athena.meerkat.controller.web.entities;

import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * A domain is used for grouping one or more tomcat instance. It is associated
 * to domain table in database
 * 
 * @author Tran Ho
 * 
 */
@Entity
@Table(name = "tomcat_domain")
public class TomcatDomain {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "create_user_id")
	private int createUser;

	@OneToOne
	@JsonBackReference
	@JoinColumn(name = "datagrid_server_group_id")
	private DatagridServerGroup serverGroup;
	@OneToMany(mappedBy = "tomcatDomain", fetch = FetchType.LAZY)
	@JsonManagedReference
	@Basic(fetch = FetchType.LAZY)
	private List<TomcatInstance> tomcatInstances;

	@OneToMany(mappedBy = "tomcatDomain", fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<ClusteringConfiguration> clusteringConfigurations;

	@OneToOne
	@JsonManagedReference
	@JoinColumn(name = "domain_tomcat_configuration_id")
	private DomainTomcatConfiguration domainTomcatConfig;

	@OneToMany(mappedBy = "tomcatDomain", fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<TomcatConfigFile> tomcatConfigFiles;

	@OneToMany(mappedBy = "tomcatDomain", fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<TomcatApplication> tomcatApplication;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tomcat_domain_datasource", joinColumns = @JoinColumn(name = "tomcat_domain_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "datasource_id", referencedColumnName = "id"))
	@JsonManagedReference
	private Collection<DataSource> datasources;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<TomcatInstance> getTomcats() {
		return tomcatInstances;
	}

	public void setTomcats(List<TomcatInstance> tomcats) {
		this.tomcatInstances = tomcats;
	}

	/**
	 * Constructor
	 */
	public TomcatDomain() {
	}

	// public TomcatDomain(String name, boolean is_clustering) {
	// this.name = name;
	// this.isClustering = is_clustering;
	// }

	public int getTomcatInstancesCount() {
		return tomcatInstances.size();
	}

	public Collection<DataSource> getDatasources() {
		return datasources;
	}

	public void setDatasources(Collection<DataSource> datasources) {
		this.datasources = datasources;
	}

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}

	public DatagridServerGroup getServerGroup() {
		return serverGroup;
	}

	public void setServerGroup(DatagridServerGroup serverGroup) {
		this.serverGroup = serverGroup;
	}

	public String getDatagridServerGroupName() {
		if (getServerGroup() != null) {
			return this.getServerGroup().getName();
		}
		return "";
	}

	public int getDataGridServerGroupId() {
		if (getServerGroup() != null) {
			return this.getServerGroup().getId();
		}
		return 0;

	}
	
	public List<ClusteringConfiguration> getClusteringConfigurations() {
		return clusteringConfigurations;
	}

	public void setClusteringConfigurations(
			List<ClusteringConfiguration> clusteringConfigurations) {
		this.clusteringConfigurations = clusteringConfigurations;
	}

	public boolean isClustering() {
		if (serverGroup == null) {
			return false;
		}
		return true;
	}

	public DomainTomcatConfiguration getDomainTomcatConfig() {
		return domainTomcatConfig;
	}

	public void setDomainTomcatConfig(
			DomainTomcatConfiguration domainTomcatConfig) {
		this.domainTomcatConfig = domainTomcatConfig;
	}
}
