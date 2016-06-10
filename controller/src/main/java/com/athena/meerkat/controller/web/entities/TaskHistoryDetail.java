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
 * BongJin Kwon		2016. 6. 8.		First Draft.
 */
package com.athena.meerkat.controller.web.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bongjin Kwon
 * @version 1.0
 */
@Entity
@Table(name = "task_history_detail")
public class TaskHistoryDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int id;//

	@Column(name = "task_history_id", insertable = false, updatable = false)
	private int taskHistoryId;//

	@Column(name = "tomcat_instance_id", insertable = false, updatable = false)
	private Integer tomcatInstanceId;//
	@Column(name = "tomcat_instance_name", insertable = false, updatable = false)
	private String tomcatInstanceName;//

	@Column(name = "tomcat_domain_id", insertable = false, updatable = false)
	private int tomcatDomainId;//

	@Column(name = "status")
	private short status;//0:작업대기중, 1: 작업진행중, 2: 작업완료, 3: 작업실패

	@Column(name = "finished_time")
	private Date finishedTime;//

	@Column(name = "ip_addr")
	private String ipAddr;//

	@ManyToOne
	private TaskHistory taskHistory;

	@ManyToOne
	private TomcatInstance tomcatInstance;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public TaskHistoryDetail() {
	}

	public TaskHistoryDetail(int taskHistoryId, Integer tomcatInstanceId) {
		super();
		this.taskHistoryId = taskHistoryId;
		this.tomcatInstanceId = tomcatInstanceId;
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
	 * @return the taskHistoryId
	 */
	public int getTaskHistoryId() {
		return taskHistoryId;
	}

	/**
	 * @param taskHistoryId
	 *            the taskHistoryId to set
	 */
	public void setTaskHistoryId(int taskHistoryId) {
		this.taskHistoryId = taskHistoryId;
	}

	/**
	 * @return the tomcatInstanceId
	 */
	public int getTomcatInstanceId() {
		return tomcatInstanceId;
	}

	/**
	 * @param tomcatInstanceId
	 *            the tomcatInstanceId to set
	 */
	public void setTomcatInstanceId(int tomcatInstanceId) {
		this.tomcatInstanceId = tomcatInstanceId;
	}

	/**
	 * @return the status
	 */
	public short getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(short status) {
		this.status = status;
	}

	/**
	 * @return the finishedTime
	 */
	public java.util.Date getFinishedTime() {
		return finishedTime;
	}

	/**
	 * @param finishedTime
	 *            the finishedTime to set
	 */
	public void setFinishedTime(java.util.Date finishedTime) {
		this.finishedTime = finishedTime;
	}

	public TaskHistory getTaskHistory() {
		return taskHistory;
	}

	public void setTaskHistory(TaskHistory taskHistory) {
		this.taskHistory = taskHistory;
	}

	public TomcatInstance getTomcatInstance() {
		return tomcatInstance;
	}

	public void setTomcatInstance(TomcatInstance tomcatInstance) {
		this.tomcatInstance = tomcatInstance;
	}

	public int getTomcatDomainId() {
		return tomcatDomainId;
	}

	public void setTomcatDomainId(int tomcatDomainId) {
		this.tomcatDomainId = tomcatDomainId;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getTomcatInstanceName() {
		return tomcatInstanceName;
	}

	public void setTomcatInstanceName(String tomcatInstanceName) {
		this.tomcatInstanceName = tomcatInstanceName;
	}

}
//end of TaskHistoryDetail.java