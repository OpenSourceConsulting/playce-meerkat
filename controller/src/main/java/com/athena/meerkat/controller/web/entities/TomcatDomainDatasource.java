/**
 * 
 */
package com.athena.meerkat.controller.web.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Tran
 *
 */
@Entity
@Table(name = "tomcat_domain_datasource")
public class TomcatDomainDatasource {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int id;
	
	@Column(name = "datasource_id")
	private int datasourceId;
	
	@Column(name = "tomcat_domain_id")
	private int tomcatDomainId;

	/**
	 * 
	 */
	public TomcatDomainDatasource() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public int getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(int datasourceId) {
		this.datasourceId = datasourceId;
	}

	public int getTomcatDomainId() {
		return tomcatDomainId;
	}

	public void setTomcatDomainId(int tomcatDomainId) {
		this.tomcatDomainId = tomcatDomainId;
	}

}
