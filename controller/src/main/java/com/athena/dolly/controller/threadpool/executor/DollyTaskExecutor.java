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
package com.athena.dolly.controller.threadpool.executor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.dolly.controller.threadpool.task.BaseTask;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class DollyTaskExecutor extends BaseTaskExecutor {
    
    protected static final Logger LOGGER = LoggerFactory.getLogger(DollyTaskExecutor.class);

	@SuppressWarnings("unchecked")
	public void execute(Object... obj) {
        List<BaseTask> tasks = (List<BaseTask>)obj[0];
        for(BaseTask task : tasks) {
            executor.execute(task);
        }
	}
}
//end of DollyTaskExecutor.java