/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2014. 3. 27.		First Draft.
 */
package com.athena.meerkat.controller.web.dolly;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.dolly.common.cache.DollyManager;
import com.athena.dolly.common.cache.SessionKey;
import com.athena.dolly.common.cache.client.DollyClient;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.resources.services.DataGridServerGroupService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/dolly")
public class DollyController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DollyController.class);

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private TomcatDomainService domainService;
	
	@Autowired
	private DataGridServerGroupService sessionGroupService;
	
	private DollyClient getDollyClient(Integer sessionServerGroupId) {
		
		LOGGER.debug("group id is {}", sessionServerGroupId);
		
    	DollyClient client = DollyManager.getClient(sessionServerGroupId.toString());
    	
    	if (client == null) {
    		client = createDollyClient(sessionServerGroupId);
		}
    	
    	return client;
    }
    
    private DollyClient createDollyClient(Integer sessionServerGroupId) {
    	
    	LOGGER.debug("create dolly client. group id is {}", sessionServerGroupId);
    	
    	Resource propFile = resourceLoader.getResource("classpath:/dollyconfig/dolly.properties");
    	
    	Properties prop = new Properties();
    	
    	try{
    		prop.load(propFile.getInputStream());
    	}catch(IOException e){
    		throw new RuntimeException(e);
    	}
    	
    	prop.setProperty("infinispan.client.hotrod.server_list", sessionGroupService.getServerListPropertyValue(sessionServerGroupId));
    	
    	return DollyManager.createClient(sessionServerGroupId.toString(), prop);
    	
    }

    @RequestMapping(value="/getSessionKeyList", method=RequestMethod.GET)
    @ResponseBody
    public List<SessionKey> getSessionKeyList(int domainId) {
    	
    	Integer sessionServerGroupId = getSessionServerGroupId(domainId);
    	
    	return getDollyClient(sessionServerGroupId).getKeys(null);
    }
    

    @RequestMapping(value="/getSessionData", method=RequestMethod.GET)
    @ResponseBody
    public Object getSessionData(int domainId, String key) {
    	
    	Integer sessionServerGroupId = getSessionServerGroupId(domainId);
    	
    	return getDollyClient(sessionServerGroupId).get(key);
    }

    @RequestMapping(value="/deleteSessionData", method=RequestMethod.GET)
    @ResponseBody
    public SimpleJsonResponse deleteSessionData(int domainId, String key) throws Exception {
    	
    	Integer sessionServerGroupId = getSessionServerGroupId(domainId);
    	
    	getDollyClient(sessionServerGroupId).remove(key);
    	
    	return new SimpleJsonResponse();
    }
    
    private Integer getSessionServerGroupId(int domainId) {
    	return domainService.getDomain(domainId).getDataGridServerGroupId();
    }

}
//end of ConsoleController.java