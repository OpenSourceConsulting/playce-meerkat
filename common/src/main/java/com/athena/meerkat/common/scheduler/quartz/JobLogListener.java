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
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * Batch Job의 상태를 trace 할 수 있는  Listener
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class JobLogListener implements JobListener {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(JobLogListener.class);
	private String name;
	
	public void setName(String name) {
	    this.name = name;
	}

	public String getName() {
		return name == null ? "JobLogListener" : name;
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param context
	 * @see org.quartz.JobListener#jobExecutionVetoed(org.quartz.JobExecutionContext)
	 */
	public void jobExecutionVetoed(JobExecutionContext context) {
		LOGGER.info(context.toString());
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param context
	 * @see org.quartz.JobListener#jobToBeExecuted(org.quartz.JobExecutionContext)
	 */
	public void jobToBeExecuted(JobExecutionContext context) {
		LOGGER.info(context.toString());
	}

	/**
	 * <pre>
	 * Batch Job 실행 시 Job 상세정보 이력을 저장한다.
	 * </pre>
	 * @param context
	 * @param jobException
	 * @see org.quartz.JobListener#jobWasExecuted(org.quartz.JobExecutionContext, org.quartz.JobExecutionException)
	 */
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		// 시작 로그
		JobDetailLog log = new JobDetailLog();

		try {
			log.setActualFireTime(context.getFireTime());
			log.setScheduledFireTime(context.getScheduledFireTime());
			log.setJobGroup(context.getJobDetail().getGroup());
			log.setJobName(context.getJobDetail().getName());
			log.setRunningTime(context.getJobRunTime());
			log.setSchedulerName(context.getScheduler().getSchedulerName());
			log.setTriggerName(context.getTrigger().getName());
			
			LOGGER.debug("JobDetailLog : [{}]", log);
		} catch (SchedulerException e) {
		    LOGGER.error("Job Detail 정보 로깅 중 예기치 못한 상황이 발생했습니다.", e);
		}
	}
}//end of JobLogListener.java