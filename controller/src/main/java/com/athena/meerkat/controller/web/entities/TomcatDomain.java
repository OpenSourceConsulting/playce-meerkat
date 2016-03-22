package com.athena.meerkat.controller.web.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Id;

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

	// @OneToOne
	// @JoinColumn(name = "datagrid_server_group_id")
	// private DatagridServerGroup serverGroup;

	@OneToMany(mappedBy = "tomcatDomain", fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<TomcatInstance> tomcatInstances;

	// @OneToMany(mappedBy = "tomcatDomain", fetch = FetchType.LAZY)
	// @JsonManagedReference
	// private List<ClusteringConfiguration> clusteringConfigurations;

	// @OneToMany(mappedBy = "tomcatDomain", fetch = FetchType.LAZY)
	// @JsonManagedReference
	// private List<TomcatConfigFile> tomcatConfigFiles;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tomcat_domain_datasource", joinColumns = @JoinColumn(name = "tomcat_domain_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "datasource_id", referencedColumnName = "id"))
	@JsonManagedReference
	private Collection<DataSource> datasources;

	public String getName() {
		return name;
	}

//	public String getClusteringString() {
//		return this.isClustering ? "Clustering" : "Non-clustering";
//	}

	// public int getDatagridServerGroupId() {
	// if (serverGroup != null) {
	// return serverGroup.getId();
	// }
	// return 0;
	// }

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
//
//	public boolean getIsClustering() {
//		return isClustering;
//	}
//
//	public void setIsClustering(boolean isClustering) {
//		this.isClustering = isClustering;
//	}

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

//	public TomcatDomain(String name, boolean is_clustering) {
//		this.name = name;
//		this.isClustering = is_clustering;
//	}

	public int getTomcatInstancesCount() {
		return tomcatInstances.size();
	}

	public Collection<DataSource> getDatasources() {
		return datasources;
	}

	public void setDatasources(Collection<DataSource> datasources) {
		this.datasources = datasources;
	}

	// public String getDatagridServerGroupName() {
	// if (getServerGroup() != null) {
	// return this.getServerGroup().getName();
	// }
	// return "";
	// }

	// public DatagridServerGroup getServerGroup() {
	// return serverGroup;
	// }
	//
	// public void setServerGroup(DatagridServerGroup serverGroup) {
	// this.serverGroup = serverGroup;
	// }
	//
	// public List<TomcatConfigFile> getTomcatConfigFiles() {
	// return tomcatConfigFiles;
	// }
	//
	// public void setTomcatConfigFiles(List<TomcatConfigFile>
	// tomcatConfigFiles) {
	// this.tomcatConfigFiles = tomcatConfigFiles;
	// }

	// public Collection<ClusteringConfigurationValue> getClusteringConfigVals()
	// {
	// return clusteringConfigVals;
	// }
	//
	// public void setClusteringConfigVals(
	// Collection<ClusteringConfigurationValue> clusteringConfigs) {
	// this.clusteringConfigVals = clusteringConfigs;
	// }
}
