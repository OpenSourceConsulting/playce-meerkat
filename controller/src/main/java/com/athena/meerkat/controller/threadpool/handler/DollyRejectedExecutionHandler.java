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
 * Sang-cheon Park	2014. 12. 30.		First Draft.
 */
package com.athena.meerkat.controller.threadpool.handler;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * queueCapacity 용량 부족으로 처리되지 못하는 task가 존재할 경우 처리(로깅)하기 위한 핸들러
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class DollyRejectedExecutionHandler implements RejectedExecutionHandler {
	
    private static final Logger logger = LoggerFactory.getLogger(DollyRejectedExecutionHandler.class);

	@Override
	public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
		logger.info("[{}] has been rejected.", runnable.toString());
		
		// TODO task의 실행이 거부되었을 경우 처리해야할 로직이 남아 있다면 여기에 추가.
		
	}//end of rejectedExecution()

}//end of DollyRejectedExecutionHandler.java