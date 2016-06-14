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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/provi/tomcat")
public class TomcatProvisioningController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatProvisioningController.class);
	
	@Value("${meerkat.jdbc.driver.mysql}")
    private String mysqlDriverFile;
	
	@Autowired
	private TomcatProvisioningService proviService;
	
	@Autowired
	private TomcatInstanceService tomcatServer;
	
	@RequestMapping(value = "/installs/{domainId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse installs(@PathVariable("domainId") int domainId, int taskHistoryId) {
		
		proviService.installTomcatInstances(domainId, taskHistoryId, null);
		
		return new SimpleJsonResponse();
	}
	
	@RequestMapping(value = "/install", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse installSingleTomcat(int domainId, int serverId, int taskHistoryId) {
		
		TomcatInstance tomcatInstance = tomcatServer.getTomcatInstance(domainId, serverId);
		
		proviService.installSingleTomcatInstance(tomcatInstance.getDomainId(), taskHistoryId, tomcatInstance);
		
		return new SimpleJsonResponse();
	}
	
	@RequestMapping(value = "/rework/{taskDetailId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse install(@PathVariable("taskDetailId") int taskDetailId) {
		
		proviService.rework(taskDetailId);

		
		return new SimpleJsonResponse();
	}
	
	@RequestMapping(value = "/deployWar/{tomcatInstanceId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse deployWar(@PathVariable("tomcatInstanceId") int tomcatInstanceId, int taskHistoryId, int applicationId) {
		
		proviService.deployWar(tomcatInstanceId, taskHistoryId, applicationId);
		
		return new SimpleJsonResponse();
	}
	
	@RequestMapping(value = "/undeployWar/{tomcatInstanceId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse undeployWar(@PathVariable("tomcatInstanceId") int tomcatInstanceId, int taskHistoryId, int applicationId) {
		
		//proviService.deployWar(tomcatInstanceId, taskHistoryId, applicationId);
		
		return new SimpleJsonResponse();
	}
	
	@RequestMapping(value = "/updateXml/{tomcatInstanceId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse updateXml(@PathVariable("tomcatInstanceId") int tomcatInstanceId, int configFileId, int taskHistoryId) {
		
		proviService.updateXml(tomcatInstanceId, configFileId, taskHistoryId);
		
		return new SimpleJsonResponse();
	}
	
	@RequestMapping(value = "/updateConfig/{tomcatInstanceId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse updateConfig(@PathVariable("tomcatInstanceId") int tomcatInstanceId, int taskHistoryId, boolean changeRMI) {
		
		proviService.updateTomcatInstanceConfig(tomcatInstanceId, taskHistoryId, changeRMI);
		
		return new SimpleJsonResponse();
	}
	
	@RequestMapping(value = "/installJDBCDriver/{tomcatInstanceId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse installJDBCDriver(@PathVariable("tomcatInstanceId") int tomcatInstanceId, int taskHistoryId, String dbType) {
		
		String installJarName = mysqlDriverFile;
		/*
		if ("MySQL".equals(dbType)) {
			installJarName = mysqlDriverFile;
		}
		*/
		proviService.installJar(tomcatInstanceId, installJarName, taskHistoryId);
		
		return new SimpleJsonResponse();
	}
	

}
//end of ProvisioningController.java
