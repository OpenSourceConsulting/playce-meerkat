package com.athena.meerkat.controller.web.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "clustering_configuration")
public class ClusteringConfiguration implements Serializable, Cloneable,
		Comparable {

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
	@Column(name = "value")
	private String value;
	@Column(name = "created_time")
	private Date createdTime;
	@ManyToOne
	@JsonBackReference(value = "grid-configs")
	private DatagridServerGroup datagridServerGroup;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "domain_id")
	private TomcatDomain tomcatDomain;

	@ManyToOne
	@JoinColumn(name = "clustering_conf_version_id")
	// @JsonManagedReference(value="grid-configVersion")
	private ClusteringConfigurationVersion clusteringConfigurationVersion;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public DatagridServerGroup getDatagridServerGroup() {
		return datagridServerGroup;
	}

	public void setDatagridServerGroup(DatagridServerGroup datagridServerGroup) {
		this.datagridServerGroup = datagridServerGroup;
	}

	public TomcatDomain getTomcatDomain() {
		return tomcatDomain;
	}

	public void setTomcatDomain(TomcatDomain tomcatDomain) {
		this.tomcatDomain = tomcatDomain;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public int getTomcatDomainId() {
		if (tomcatDomain != null) {
			return tomcatDomain.getId();
		}
		return 0;
	}

	public ClusteringConfigurationVersion getClusteringConfigurationVersion() {
		return clusteringConfigurationVersion;
	}

	public void setClusteringConfigurationVersion(
			ClusteringConfigurationVersion clusteringConfigurationVersion) {
		this.clusteringConfigurationVersion = clusteringConfigurationVersion;
	}

	public ClusteringConfiguration clone() {
		try {

			ClusteringConfiguration clone = (ClusteringConfiguration) super
					.clone();
			clone.setId(0);// reset ID;
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public int compareTo(Object o) {
		return this.name.compareTo(((ClusteringConfiguration) o).getName());
	}
}
