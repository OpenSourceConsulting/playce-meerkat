/**
 * 
 */
package com.athena.meerkat.controller.web.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Tran
 *
 */
@Entity
@Table
public class TomcatDomainDatasource {
	@Id
	private int id;
	private int datasourceId;
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
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the datasourceId
	 */
	public int getDatasourceId() {
		return datasourceId;
	}

	/**
	 * @param datasourceId the datasourceId to set
	 */
	public void setDatasourceId(int datasourceId) {
		this.datasourceId = datasourceId;
	}

	/**
	 * @return the tomcatDomainId
	 */
	public int getTomcatDomainId() {
		return tomcatDomainId;
	}

	/**
	 * @param tomcatDomainId the tomcatDomainId to set
	 */
	public void setTomcatDomainId(int tomcatDomainId) {
		this.tomcatDomainId = tomcatDomainId;
	}

}
