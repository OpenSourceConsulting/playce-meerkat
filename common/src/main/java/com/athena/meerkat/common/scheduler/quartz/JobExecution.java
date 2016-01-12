/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2013. 8. 1.		First Draft.
 */
package com.athena.meerkat.common.scheduler.quartz;

import org.quartz.JobExecutionContext;

import com.athena.meerkat.common.scheduler.JobStatus;

/**
 * <pre>
 * {@link JobExecutionContext}의 래핑 클래스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class JobExecution {

	private JobStatus jobStatus;
	private final JobExecutionContext context;

	private JobExecution(JobExecutionContext context) {
		this.context = context;
	}

	public void initJobStatus() {
		jobStatus = JobStatus.start();
	}

	public JobStatus getJobStatus() {
		return this.jobStatus;
	}

	public JobExecutionContext getContext() {
		return context;
	}

	public static JobExecution newExecution(JobExecutionContext context) {
		JobExecution execution = new JobExecution(context);
		execution.initJobStatus();
		return execution;
	}
}//end of JobExecution.java