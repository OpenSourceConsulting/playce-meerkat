/* 
 * Copyright (C) 2012-2015 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * BongJin Kwon		2016. 4. 19.		First Draft.
 */
package com.athena.meerkat.controller.web.monitoring.server;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bongjin Kwon
 * @version 1.0
 */
@Entity
@Table(name = "mon_fs_tbl")
@IdClass(MonFsPK.class)
public class MonFs {

	@Id
	@Column(name = "server_id")
	private int serverId;//

	@Id
	@Column(name = "fs_name")
	private String fsName;//

	@Id
	@Column(name = "mon_dt")
	private java.util.Date monDt;//

	@Column(name = "total")
	private int total;//
	@Column(name = "used")
	private int used;//
	@Column(name = "use_per")
	private Double usePer;//
	@Column(name = "avail")
	private int avail;//

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public MonFs() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the serverId
	 */
	public int getServerId() {
		return serverId;
	}

	/**
	 * @param serverId
	 *            the serverId to set
	 */
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	/**
	 * @return the fsName
	 */
	public String getFsName() {
		return fsName;
	}

	/**
	 * @param fsName
	 *            the fsName to set
	 */
	public void setFsName(String fsName) {
		this.fsName = fsName;
	}

	/**
	 * @return the monDt
	 */
	public java.util.Date getMonDt() {
		return monDt;
	}

	/**
	 * @param monDt
	 *            the monDt to set
	 */
	public void setMonDt(java.util.Date monDt) {
		this.monDt = monDt;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the used
	 */
	public int getUsed() {
		return used;
	}

	/**
	 * @param used
	 *            the used to set
	 */
	public void setUsed(int used) {
		this.used = used;
	}

	/**
	 * @return the usePer
	 */
	public Double getUsePer() {
		return usePer;
	}

	/**
	 * @param usePer
	 *            the usePer to set
	 */
	public void setUsePer(Double usePer) {
		this.usePer = usePer;
	}

	/**
	 * @return the avail
	 */
	public int getAvail() {
		return avail;
	}

	/**
	 * @param avail
	 *            the avail to set
	 */
	public void setAvail(int avail) {
		this.avail = avail;
	}
	
	@PrePersist
	public void onPrePersist() {
		this.monDt = new Date();
	}

	public Double getAvailPer() {
		return 100 - usePer;
	}

	public Double getAvailPer() {
		return 100 - usePer;
	}

}
// end of MonFs.java
