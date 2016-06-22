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
 * BongJin Kwon		2016. 6. 16.		First Draft.
 */
package com.athena.meerkat.controller.web.monitoring.stat;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.monitoring.jmx.MonJmx;
import com.athena.meerkat.controller.web.monitoring.server.MonData;
import com.athena.meerkat.controller.web.monitoring.server.MonFs;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bongjin Kwon
 * @version 1.0
 */
@Entity
@Table(name = "mon_util_stat")
public class MonUtilStat {

	private static final String JMX_PROCESS_CPU_LOAD = "jmx.ProcessCpuLoad";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int id;//

	@Column(name = "server_id")
	private Integer serverId;//

	@Column(name = "tomcat_instance_id")
	private Integer tomcatInstanceId;//

	@Column(name = "mon_factor_id")
	private String monFactorId;//

	@Column(name = "mon_value")
	private Double monValue;//percentage value.

	@Column(name = "update_dt")
	private java.util.Date updateDt;//최근 업데이트 일시.

	@Transient
	private String name; // name of tomcat instance OR server
	@Transient
	private String type; //type of alert

	@ManyToOne
	private Server server;
	@ManyToOne
	private TomcatInstance tomcatInstance;

	public MonUtilStat() {

	}

	@Transient
	private boolean alertStatus;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public MonUtilStat(MonData monData) {
		this.serverId = monData.getServerId();
		this.monFactorId = monData.getMonFactorId();
		this.monValue = monData.getMonValue();
	}

	public MonUtilStat(MonJmx monJmx) {
		this.tomcatInstanceId = monJmx.getInstanceId();
		this.monFactorId = monJmx.getMonFactorId();

		if (JMX_PROCESS_CPU_LOAD.equals(this.monFactorId)) {
			this.monValue = monJmx.getMonValue();

		} else if (monJmx.getMonValue2() > 0) {
			this.monValue = monJmx.getMonValue() * 100D / monJmx.getMonValue2();
		}
	}

	public MonUtilStat(MonFs monFs) {
		this.serverId = monFs.getServerId();
		this.monFactorId = monFs.getFsName();
		this.monValue = monFs.getUsePer();
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

	/**
	 * @return the serverId
	 */
	public Integer getServerId() {
		return server == null ? 0 : server.getId();
	}

	/**
	 * @return the tomcatInstanceId
	 */
	public Integer getTomcatInstanceId() {
		return tomcatInstance == null ? 0 : tomcatInstance.getId();
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
	 * @return the monValue
	 */
	public Double getMonValue() {
		return monValue;
	}

	/**
	 * @param monValue
	 *            the monValue to set
	 */
	public void setMonValue(Double monValue) {
		this.monValue = monValue;
	}

	/**
	 * @return the updateDt
	 */
	public java.util.Date getUpdateDt() {
		return updateDt;
	}

	/**
	 * @param updateDt
	 *            the updateDt to set
	 */
	public void setUpdateDt(java.util.Date updateDt) {
		this.updateDt = updateDt;
	}

	@PrePersist
	@PreUpdate
	public void onPreSave() {
		this.updateDt = new Date();
	}

	public boolean isAlertStatus() {
		return alertStatus;
	}

	public void setAlertStatus(boolean alertStatus) {
		this.alertStatus = alertStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getDecimalMonValue() {
		return monValue / 100;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public TomcatInstance getTomcatInstance() {
		return tomcatInstance;
	}

	public void setTomcatInstance(TomcatInstance tomcatInstance) {
		this.tomcatInstance = tomcatInstance;
	}

}
//end of MonUtilStat.java