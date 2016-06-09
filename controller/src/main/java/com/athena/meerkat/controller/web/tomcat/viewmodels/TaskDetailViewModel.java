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
 * BongJin Kwon		2016. 6. 9.		First Draft.
 */
package com.athena.meerkat.controller.web.tomcat.viewmodels;

import java.util.ArrayList;
import java.util.List;

import com.athena.meerkat.controller.web.entities.TaskHistoryDetail;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
public class TaskDetailViewModel {

	private String name; // domain name or tomcat instance name.
	private String hostName;
	private String ipaddress;
	private int status;
	private String statusName;
	private String logFilePath;
	private boolean leaf;
	private boolean expanded;
	
	private List<TaskDetailViewModel> children;
	
	public TaskDetailViewModel(String domainName) {
		this.name = domainName;
		this.children = new ArrayList<TaskDetailViewModel>();
		this.expanded = true;
	}
	
	public TaskDetailViewModel(TaskHistoryDetail taskDetail) {
		
		this.name 		= taskDetail.getTomcatInstanceName();
		this.hostName 	= taskDetail.getHostName();
		this.ipaddress  = taskDetail.getIpaddress();
		this.status		= taskDetail.getStatus();
		this.logFilePath= taskDetail.getLogFilePath();
		this.leaf = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public List<TaskDetailViewModel> getChildren() {
		return children;
	}

	public void setChildren(List<TaskDetailViewModel> children) {
		this.children = children;
	}
	
	public void addChild(TaskDetailViewModel child) {
		this.children.add(child);
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

}
//end of TaskDetailViewModel.java