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
package com.athena.dolly.controller.threadpool.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.dolly.controller.threadpool.handler.DollyTaskExceptionHandler;

/**
 * <pre>
 * ThreadPoolTaskExecutor에 의해 실행 될 기본 task로써 Runnable interface가 구현되어 있다.
 * BaseTask를 상속받고 taskRun() 메소드를 구현해야 한다.
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public abstract class BaseTask implements Runnable {
    
    protected static final Logger logger = LoggerFactory.getLogger(BaseTask.class);
    protected String taskName;
    
	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName == null ? super.toString() : taskName;
	}

	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
    
    /**
     * <pre>
     * Constructor
     * </pre>
     * @param taskName
     */
    public BaseTask() {
    }//end of Constructor()
    
    /**
     * <pre>
     * Constructor
     * </pre>
     * @param taskName
     */
    public BaseTask(String taskName) {
    	this.taskName = taskName;
    }//end of Constructor()

	/**
     * <pre>
     * 
     * </pre>
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
    	// Thread 실행 시 UncaughtException에 대해 catch 하기 위해 handler 등록.
        Thread.currentThread().setUncaughtExceptionHandler(new DollyTaskExceptionHandler());
        
        logger.debug("[{}] is started.", getTaskName());
        // TODO 사전 작업이 필요하면 beforeRun() 메소드 구현 후 호출
        
    	taskRun();
    	
    	// TODO 사후 작업이 필요하면 afterRun() 메소드 구현 후 호출
    	logger.debug("[{}] is completed.", getTaskName());
    }
    
    @Override
    public String toString() {
    	return getTaskName();
    }
    
    protected abstract void taskRun();
    
}//end of BaseTask.java