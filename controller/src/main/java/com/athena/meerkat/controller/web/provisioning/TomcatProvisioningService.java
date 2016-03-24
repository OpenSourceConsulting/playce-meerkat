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
 * BongJin Kwon		2016. 3. 24.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Service
public class TomcatProvisioningService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatProvisioningService.class);
	
	@Autowired
	private TomcatDomainService domainService;
	
	@Autowired
	private TomcatInstanceService instanceService;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public TomcatProvisioningService() {
		// TODO Auto-generated constructor stub
	}
	
	public void installTomcatInstance(int domainId) {
		
				
		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		//List<Tomcat>   TomcatInstanceService
		List<TomcatInstance> list = instanceService.getTomcatListByDomainId(domainId);
		
	}
	
	private DomainTomcatConfiguration getDomainTomcatConfiguration(int domainId) {
		DomainTomcatConfiguration config = new DomainTomcatConfiguration();
		config.setJavaHome("/usr/java/jdk1.7.0_80");
		config.setCatalinaHome("/home/centos/tmp/apache-tomcat-7.0.68/apache-tomcat-7.0.68");
		config.setCatalinaBase("/home/centos/tmp/instance1");
		
		return config;
	}

}
//end of TomcatProvisioningService.java