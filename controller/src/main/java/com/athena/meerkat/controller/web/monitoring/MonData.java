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
 * BongJin Kwon		2016. 4. 6.		First Draft.
 */
package com.athena.meerkat.controller.web.monitoring;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.athena.meerkat.controller.common.MeerkatUtils;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bongjin Kwon
 * @version 1.0
 */
@Entity
@Table(name = "mon_data_tbl")
@IdClass(MonDataPK.class)
public class MonData implements Serializable {

	private static final long serialVersionUID = 8781936650855661482L;

	@Id
	@Column(name = "mon_factor_id")
	private String monFactorId;//

	@Id
	@Column(name = "server_id")
	private int serverId;//

	@Id
	@Column(name = "mon_dt")
	private Date monDt;//

	@Column(name = "mon_value")
	private Double monValue;//

	@Transient
	private String monDtString;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public MonData() {
		
	}

	/**
	 * @return the monFactorId
	 */
	public String getMonFactorId() {
		return monFactorId;
	}

	/**
	 * @param monFactorId
	 *            the monFactorId to set
	 */
	public void setMonFactorId(String monFactorId) {
		this.monFactorId = monFactorId;
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
	 * @return the monValue
	 */
	public Double getMonValue() {
		return monValue * 100;
	}

	/**
	 * @param monValue
	 *            the monValue to set
	 */
	public void setMonValue(Double monValue) {
		this.monValue = monValue;
	}

	@PrePersist
	public void onPrePersist() {
		this.monDt = new Date();
	}

	public String getMonDtString() {
		return MeerkatUtils.dateTimeToString(this.monDt, "hh:mm:ss");
	}

	public void setMonDtString(String monDtString) {
		this.monDtString = monDtString;
	}

}
// end of MonData.java