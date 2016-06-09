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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.athena.meerkat.controller.web.common.util.WebUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Entity
@Table(name = "task_history")
public class TaskHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int id;//
	
	@Column(name = "task_cd_id")
	private int taskCdId;//task code id
	
	@Column(name = "create_user_id")
	private int createUserId;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Transient
	private String taskName;
	
	@OneToMany(mappedBy = "taskHistory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<TaskHistoryDetail> taskHistoryDetails;
	

	public TaskHistory() {
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
	 * @return the taskCdId
	 */
	public int getTaskCdId() {
		return taskCdId;
	}

	/**
	 * @param taskCdId the taskCdId to set
	 */
	public void setTaskCdId(int taskCdId) {
		this.taskCdId = taskCdId;
	}

	/**
	 * @return the createUserId
	 */
	public int getCreateUserId() {
		return createUserId;
	}

	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * @return the createTime
	 */
	public java.util.Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public List<TaskHistoryDetail> getTaskHistoryDetails() {
		return taskHistoryDetails;
	}

	public void setTaskHistoryDetails(List<TaskHistoryDetail> taskHistoryDetails) {
		this.taskHistoryDetails = taskHistoryDetails;
	}
	
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@PrePersist
	public void onPreSave() {
		this.createTime = new Date();
		this.createUserId = WebUtil.getLoginUserId();
	}

}
//end of TaskHistory.java