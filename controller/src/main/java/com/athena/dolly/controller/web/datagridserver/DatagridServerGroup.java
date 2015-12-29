package com.athena.dolly.controller.web.datagridserver;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import com.athena.dolly.controller.web.domain.Domain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "datagrid_server_group")
public class DatagridServerGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "Id")
	private int Id;

	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "datagridServerGroup", fetch = FetchType.LAZY)
	@JsonBackReference
	private Collection<DatagridServer> datagridServers;

	@OneToOne
	@JsonBackReference
	private Domain domain;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Collection<DatagridServer> getDatagridServers() {
		return datagridServers;
	}

	public void setDatagridServers(Collection<DatagridServer> datagridServers) {
		this.datagridServers = datagridServers;
	}

}
