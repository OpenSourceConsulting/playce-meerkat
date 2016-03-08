package com.athena.meerkat.controller.web.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import com.athena.meerkat.controller.web.datagridserver.DatagridServerGroup;
import com.athena.meerkat.controller.web.env.EnvironmentVariable;
import com.athena.meerkat.controller.web.revision.Revision;
import com.athena.meerkat.controller.web.tomcat.instance.TomcatInstance;
import com.athena.meerkat.controller.web.user.UserRole;
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
@Table(name = "clustering_configuration")
public class ClusteringConfiguration implements Serializable {
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
	@OneToMany(mappedBy = "clustering_configuration")
	private List<ClusteringConfigurationValue> clusteringConfigValues;

	public String getName() {
		return name;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the clusteringConfigValues
	 */
	public List<ClusteringConfigurationValue> getClusteringConfigValues() {
		return clusteringConfigValues;
	}

	/**
	 * @param clusteringConfigValues
	 *            the clusteringConfigValues to set
	 */
	public void setClusteringConfigValues(
			List<ClusteringConfigurationValue> clusteringConfigValues) {
		this.clusteringConfigValues = clusteringConfigValues;
	}

}
