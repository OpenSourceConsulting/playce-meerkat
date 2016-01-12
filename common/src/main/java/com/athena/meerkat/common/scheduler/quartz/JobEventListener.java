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

/**
 * <pre>
 * {@link BaseJob}의 이벤트 처리 기능을 제공하는 인터페이스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public interface JobEventListener {

	/**
	 * <pre>
	 * 비즈니스 로직 수행 후 처리해야 하는 로직이 필요한 경우 구현하는 메소드(후행 처리 메소드)
	 * </pre>
	 * @param context
	 */
	void afterJob(JobExecution context);

	/**
	 * <pre>
	 * 비즈니스 로직 수행 전에 처리해야 하는 로직이 필요한 경우 구현하는 메소드(선행 처리 메소드)
	 * </pre>
	 * @param context
	 */
	void beforeJob(JobExecution context);

	/**
	 * <pre>
	 * {@link BaseJob#executeInternal(JobExecution)}에서 예외가 발생한 경우 자동으로 호출되는 예외 처리 메소드
	 * </pre>
	 * @param context
	 * @param e
	 */
	void onError(JobExecution context, Throwable e);
}//end of JobEventListener.java