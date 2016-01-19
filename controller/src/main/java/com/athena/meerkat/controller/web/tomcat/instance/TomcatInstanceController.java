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
 * Bong-Jin Kwon	2015. 1. 9.		First Draft.
 */
package com.athena.meerkat.controller.web.tomcat.instance;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.common.provisioning.ProvisioningHandler;
import com.athena.meerkat.controller.web.machine.MachineService;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 2.0
 */
@RestController
@RequestMapping("/tomcat")
public class TomcatInstanceController {

	@Autowired
	private TomcatInstanceService service;
	@Autowired
	private MachineService machineService;

	@Inject
	@Named("provisioningHandler")
	private ProvisioningHandler provisioningHandler;

	public TomcatInstanceController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "/instance/start", method = RequestMethod.POST)
	@ResponseBody
	public boolean startTomcat(int id) {
		return service.start(id);
	}

	@RequestMapping(value = "/instance/restart", method = RequestMethod.POST)
	@ResponseBody
	public boolean restartTomcat(int id) {
		return service.restart(id);
	}

	@RequestMapping(value = "/instance/stop", method = RequestMethod.POST)
	@ResponseBody
	public boolean stopTomcat(int id) {
		return service.stop(id);
	}

	@RequestMapping("/list")
	List<TomcatInstance> getTomcatInstance() {
		ServiceResult result = service.getAll();
		if (result.getStatus() == Status.DONE) {
			List<TomcatInstance> tomcats = (List<TomcatInstance>) result
					.getReturnedVal();
			return tomcats;
		}
		return null;
	}

	@RequestMapping("/listbydomain")
	@ResponseBody
	List<TomcatInstance> getTomcatInstances(int domainId) {
		return service.getTomcatListByDomainId(domainId);
	}

	@RequestMapping("/addNew")
	@ResponseBody
	// add more param later
	public boolean addNew(String name, int domainId, int machineId) {
		boolean result = false;
		// Machine machine = machineService.retrieve(machineId);
		// String repoAddr = DollyConstants.MEERKAT_REPO;
		/* sample data */
		String user = "root";
		String version = "7.0.54";
		String java_home = "/usr/java/default";
		String server_home = "/root";
		String server_name = "rootServer21";
		String catalina_home = "/root/jboss-ews-2.1/tomcat7";
		String catalina_base = "/root/Servers/rootServer21222";
		String encoding = "UTF-8";
		int port_offset = 0;
		int heap_size = 1024;
		int permgen_size = 256;
		boolean is_enable_http = true;
		boolean is_high_availability = true;
		String bind_address = "192.168.0.88";
		String other_bind_address = "";
		boolean is_start_service = true;
		
		return false;
	}
}
