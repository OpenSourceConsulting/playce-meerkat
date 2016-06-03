package com.athena.meerkat.controller.web.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "datagrid_server_group")
public class DatagridServerGroup implements Serializable {

	private static final long serialVersionUID = 977431664311727091L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "type_cd_id")
	private int typeCdId;
	@Transient
	private String typeNm;

	@OneToMany(mappedBy = "serverGroup", fetch = FetchType.LAZY)
	private List<TomcatDomain> tomcatDomains;

	@OneToMany(mappedBy = "datagridServerGroup", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<DatagridServer> datagridServers;
	
	@OneToMany(mappedBy = "datagridServerGroup", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ClusteringConfiguration> clusteringConfigurations;

	
	
	public List<TomcatDomain> getTomcatDomains() {
		return tomcatDomains;
	}

	public void setTomcatDomains(List<TomcatDomain> tomcatDomains) {
		this.tomcatDomains = tomcatDomains;
	}

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

	public List<DatagridServer> getDatagridServers() {
		return datagridServers;
	}

	public void setDatagridServers(List<DatagridServer> datagridServers) {
		this.datagridServers = datagridServers;
	}

	public List<Server> getServers() {
		
		List<Server> servers = new ArrayList<Server>();
		
		List<DatagridServer> dgServers = getDatagridServers();
		
		for (DatagridServer datagridServer : dgServers) {
			
			Server server = datagridServer.getServer();
			server.setPort(datagridServer.getPort());
			
			servers.add(server);
		}
		
		return servers;
	}


	public int getServerNo() {
		
		List<Server> servers = getServers();
		if (servers != null) {
			return servers.size();
		}
		return 0;
	}
	
	public int getDomainSize(){
		List<TomcatDomain> domains = getTomcatDomains();
		if(domains != null) {
			return domains.size();
		}
		return 0;
	}

	public int getTypeCdId() {
		return typeCdId;
	}

	public void setTypeCdId(int typeCdId) {
		this.typeCdId = typeCdId;
	}

	public List<ClusteringConfiguration> getClusteringConfigurations() {
		return clusteringConfigurations;
	}

	public void setClusteringConfigurations(
			List<ClusteringConfiguration> clusteringConfigurations) {
		this.clusteringConfigurations = clusteringConfigurations;
	}

	public String getTypeNm() {
		return typeNm;
	}

	public void setTypeNm(String typeNm) {
		this.typeNm = typeNm;
	}
}
