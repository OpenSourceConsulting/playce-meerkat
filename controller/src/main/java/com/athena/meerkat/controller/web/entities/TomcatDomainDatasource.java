/**
 * 
 */
package com.athena.meerkat.controller.web.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Tran
 *
 */
// @Entity

public class TomcatDomainDatasource {
	@Id
	private int id;
	@ManyToOne
	private DataSource datasource;
	@ManyToOne
	private TomcatDomain tomcatDomain;

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

}
