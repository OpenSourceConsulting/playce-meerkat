package com.athena.meerkat.controller.web.datagridserver;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import com.athena.meerkat.controller.web.domain.Domain;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int Id;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

	@OneToMany(mappedBy = "datagridServerGroup", fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<DatagridServer> datagridServers;

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

	public List<DatagridServer> getDatagridServers() {
		return datagridServers;
	}

	public void setDatagridServers(List<DatagridServer> datagridServers) {
		this.datagridServers = datagridServers;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public int getServerNo() {
		if (datagridServers != null) {
			return datagridServers.size();
		}
		return 0;
	}

}
