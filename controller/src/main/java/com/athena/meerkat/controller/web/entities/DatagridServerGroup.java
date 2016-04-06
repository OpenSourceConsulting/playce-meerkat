package com.athena.meerkat.controller.web.entities;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonManagedReference;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	@OneToMany(mappedBy = "datagridServerGroup", fetch = FetchType.LAZY)
	@JsonManagedReference(value = "grid-server")
	private List<Server> servers;
	//
	@OneToMany(mappedBy = "datagridServerGroup", fetch = FetchType.LAZY)
	@JsonManagedReference(value = "grid-configs")
	private List<ClusteringConfiguration> clusteringConfigurations;

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

	public List<Server> getServers() {
		return servers;
	}

	public void setServers(List<Server> servers) {
		this.servers = servers;
	}

	public int getServerNo() {
		if (servers != null) {
			return servers.size();
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
}
