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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * Runnable Task 실행 시 발생되는 UncaughtException을 처리하기 위한 핸들러
 * 예상되는 Exception은 task 내의 run() 내에서 구현.
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class DollyTaskExceptionHandler implements Thread.UncaughtExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(DollyTaskExceptionHandler.class);

    /**
     * <pre>
     * 
     * </pre>
     * @param t
     * @param e
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logger.info("Exception occurred during [{}] thread was running.", t.getName());
        logger.error("[" + t.getName() + "]'s Exception detail => ", e);
        
        // TODO task 실행시 발생된 UncaughtException에 대하여 공통적으로 처리해야할 부분이 있을 경우 구현한다.
    }
}//end of DollyTaskExceptionHandler.java