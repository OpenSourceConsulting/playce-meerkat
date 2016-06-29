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
 * BongJin Kwon		2016. 3. 21.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.TaskHistory;
import com.athena.meerkat.controller.web.tomcat.services.TaskHistoryService;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/provi/agent")
public class AgentProvisioningController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AgentProvisioningController.class);
	
	@Autowired
	private AgentProvisioningService service;
	
	@Autowired
	private TaskHistoryService taskService;

	
	@RequestMapping(value = "/install/{serverId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse install(SimpleJsonResponse jsonRes, @PathVariable("serverId") int serverId, int taskHistoryId) {
		
		
		service.installAgent(serverId, taskHistoryId, null);
		
		return jsonRes;
	}
	
	@RequestMapping(value = "/uninstall/{serverId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse uninstall(SimpleJsonResponse jsonRes, @PathVariable("serverId") int serverId, int taskHistoryId) {
		
		service.uninstallAgent(serverId, taskHistoryId, null);
		
		return jsonRes;
	}
	
	@RequestMapping(value = "/reinstall/{serverId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse reinstall(SimpleJsonResponse jsonRes, @PathVariable("serverId") int serverId, int taskHistoryId) {
		
		service.reinstallAgent(serverId, taskHistoryId, null);
		
		return jsonRes;
	}
	

}
//end of ProvisioningController.java
