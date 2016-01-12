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

import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.lang.time.DateFormatUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.ApplicationContext;

import com.athena.meerkat.common.scheduler.InternalJobExecutionException;

/**
 * <pre>
 * 스케줄링 잡을 표준화한 클래스로, 잡 구현시 상속받아 사용하는 추상 클래스이다.
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public abstract class BaseJob implements Job, JobEventListener {

	protected static final Logger LOGGER = LoggerFactory
			.getLogger(BaseJob.class);
	private static final String LOGGING_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String APPLICAITON_CONTEXT_KEY = "APPCTX";

	protected ApplicationContext context;

	/**
	 * <pre>
	 * Trigger에 의해 실행되는 Quartz Job의 실행 메소드
	 * </pre>
	 * 
	 * @param context
	 * @throws JobExecutionException
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		LOGGER.debug(
				"예정된 잡(JOB)이 호출됐습니다.[job: {} , instance: {}, start: {}]",
				new Object[] {
						context.getJobDetail().getFullName(),
						context.getJobInstance().toString(),
						DateFormatUtils.format(System.currentTimeMillis(),
								LOGGING_DATE_FORMAT) });

		initializingContext(context);

		// TODO 필요한 설정 추가
		JobExecution wrapper = JobExecution.newExecution(context);

		try {
			// 전 처리
			beforeJob(wrapper);

			// 잡 실행
			executeInternal(wrapper);

			// 후 처리
			afterJob(wrapper);

			wrapper.getJobStatus().complete();
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(
						"잡(JOB)이 예기치 못한 예외로 작업을 실패 했습니다.[job: "
								+ context.getJobDetail().getFullName()
								+ ", instance:"
								+ context.getJobInstance().toString()
								+ " , interrupt: "
								+ DateFormatUtils.format(
										System.currentTimeMillis(),
										LOGGING_DATE_FORMAT) + "]", e);
			}

			wrapper.getJobStatus().fail();
			onError(wrapper, e);
			throw new JobExecutionException("Job 내부 실행 중 예외가 발생했습니다", e);
		}

		LOGGER.debug(
				"잡(JOB)이 성공적으로 작업을 완료했습니다.[job: {} , instance: {}, end: {}]",
				new Object[] {
						context.getJobDetail().getFullName(),
						context.getJobInstance().toString(),
						DateFormatUtils.format(System.currentTimeMillis(),
								LOGGING_DATE_FORMAT) });
	}// end of execute()

	/**
	 * <pre>
	 * 잡 내부 컨텍스트 객체인 {@link JobExecutionContext}의 초기화 메소드.
	 * </pre>
	 * 
	 * @param context
	 * @throws JobExecutionException
	 */
	public void initializingContext(JobExecutionContext context)
			throws JobExecutionException {
		try {
			this.context = (ApplicationContext) context.getScheduler()
					.getContext().get(APPLICAITON_CONTEXT_KEY);

			BeanWrapper bw = PropertyAccessorFactory
					.forBeanPropertyAccess(this);
			MutablePropertyValues pvs = new MutablePropertyValues();
			pvs.addPropertyValues(context.getScheduler().getContext());
			pvs.addPropertyValues(context.getMergedJobDataMap());

			resolveDependenciesOfJobObject(context, pvs);

			bw.setPropertyValues(pvs, true);
		} catch (SchedulerException ex) {
			throw new JobExecutionException(ex);
		}
	}// end of initializingContext()

	/**
	 * <pre>
	 * {@link BaseJob} 구현 클래스의 의존성 주입을 수행
	 * </pre>
	 * 
	 * @param context
	 * @param pvs
	 */
	@SuppressWarnings("unchecked")
	private void resolveDependenciesOfJobObject(JobExecutionContext context,
			MutablePropertyValues pvs) {
		for (Iterator<Entry<String, Object>> iterator = context
				.getMergedJobDataMap().entrySet().iterator(); iterator
				.hasNext();) {
			Entry<String, Object> entry = (Entry<String, Object>) iterator
					.next();
			pvs.addPropertyValue((String) entry.getKey(), entry.getValue());
		}
	}// end of resolveDependenciesOfJobObject()

	/**
	 * <pre>
	 * 스케줄링 잡에서 실제로 수행해야 하는 비즈니스 로직을 구현하는 템플릿 메소드. 잡 구현시 반드시 구현해야 하는 추상 메소드 이다.
	 * </pre>
	 * 
	 * @param context
	 * @throws InternalJobExecutionException
	 */
	protected abstract void executeInternal(JobExecution context)
			throws InternalJobExecutionException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.athena.peacock.scheduler.quartz.JobEventListener#afterJob(com.athena
	 * .peacock.scheduler.quartz.JobExecution)
	 */
	public void afterJob(JobExecution context) {
		LOGGER.debug("{} will be terminated... [instance : {}]", this
				.getClass().getSimpleName(), context);
	}// end of afterJob()

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.athena.peacock.scheduler.quartz.JobEventListener#beforeJob(com.athena
	 * .peacock.scheduler.quartz.JobExecution)
	 */
	public void beforeJob(JobExecution context) {
		LOGGER.debug("{} will be started... [instance : {}]", this.getClass()
				.getSimpleName(), context);
	}// end of beforeJob()

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.athena.peacock.scheduler.quartz.JobEventListener#onError(com.athena
	 * .peacock.scheduler.quartz.JobExecution, java.lang.Throwable)
	 */
	public void onError(JobExecution context, Throwable t) {
		LOGGER.error("Exception has occurred : ", t);
	}// end of onError()
}// end of BaseJob.java