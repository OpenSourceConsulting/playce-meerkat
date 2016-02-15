package com.athena.meerkat.controller.web.domain;

import java.io.Serializable;
import java.util.Collection;

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
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "value")
	private String value;
	@Column(name = "revision")
	private int revision;

	@ManyToOne
	@JsonBackReference
	private Domain domain;

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public int getDomainId() {
		if (getDomain() != null) {
			return getDomain().getId();
		}
		return 0;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

}
