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
	
	@Column(name = "tomcat_domain_id")
	private int tomcatDomainId;
	
	@Column(name = "tomcat_instance_id", insertable = false, updatable = false)
	private int tomcatInstanceId;//
	
	@Column(name = "tomcat_domain_name")
	private String tomcatDomainName;
	
	@Column(name = "tomcat_instance_name")
	private String tomcatInstanceName;
	
	@Column(name = "host_name")
	private String hostName;
	
	@Column(name = "ip_addr")
	private String ipaddress;
	
	@Column(name = "logfile_path")
	private String logFilePath;
	
	@Column(name = "status")
	private short status;//0:작업대기중, 1: 작업진행중, 2: 작업완료, 3: 작업실패
	
	@Column(name = "finished_time")
	private Date finishedTime;//
	
	
	
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
	
	public TaskHistoryDetail(int taskHistoryId, TomcatInstance tomcatInstance) {
		this(taskHistoryId, tomcatInstance, null);
	}
	
	public TaskHistoryDetail(int taskHistoryId, TomcatInstance tomcatInstance, String logFilePath) {
		super();
		setTaskHistoryId(taskHistoryId);
		
		this.tomcatDomainId = tomcatInstance.getDomainId();
		setTomcatInstanceId(tomcatInstance.getId());
		this.tomcatDomainName = tomcatInstance.getDomainName();
		this.tomcatInstanceName = tomcatInstance.getName();
		this.hostName = tomcatInstance.getHostName();
		this.ipaddress = tomcatInstance.getIpaddress();
		this.logFilePath = logFilePath;
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
	 * @return the taskHistoryId
	 */
	public int getTaskHistoryId() {
		return taskHistoryId;
	}

	/**
	 * @param taskHistoryId the taskHistoryId to set
	 */
	public void setTaskHistoryId(int taskHistoryId) {
		this.taskHistoryId = taskHistoryId;
		
		if (this.taskHistory == null) {
			this.taskHistory = new TaskHistory();
		}
		
		this.taskHistory.setId(taskHistoryId);
	}

	/**
	 * @return the tomcatInstanceId
	 */
	public int getTomcatInstanceId() {
		return tomcatInstanceId;
	}

	/**
	 * @param tomcatInstanceId the tomcatInstanceId to set
	 */
	public void setTomcatInstanceId(int tomcatInstanceId) {
		this.tomcatInstanceId = tomcatInstanceId;
		
		if(this.tomcatInstance == null) {
			this.tomcatInstance = new TomcatInstance();
		}
		this.tomcatInstance.setId(tomcatInstanceId);
	}

	public int getTomcatDomainId() {
		return tomcatDomainId;
	}

	public void setTomcatDomainId(int tomcatDomainId) {
		this.tomcatDomainId = tomcatDomainId;
	}

	public String getTomcatDomainName() {
		return tomcatDomainName;
	}

	public void setTomcatDomainName(String tomcatDomainName) {
		this.tomcatDomainName = tomcatDomainName;
	}

	public String getTomcatInstanceName() {
		return tomcatInstanceName;
	}

	public void setTomcatInstanceName(String tomcatInstanceName) {
		this.tomcatInstanceName = tomcatInstanceName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	/**
	 * @return the status
	 */
	public short getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
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
	 * @param finishedTime the finishedTime to set
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
	
	public String getDomainName() {
		if(this.tomcatInstance != null) {
			return this.tomcatInstance.getDomainName();
		}
		
		return "";
	}

}
//end of TaskHistoryDetail.java