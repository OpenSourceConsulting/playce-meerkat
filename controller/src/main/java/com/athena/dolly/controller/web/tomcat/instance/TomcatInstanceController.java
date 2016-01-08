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
package com.athena.dolly.controller.web.tomcat.instance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.athena.dolly.controller.DollyConstants;
import com.athena.dolly.controller.ServiceResult;
import com.athena.dolly.controller.ServiceResult.Status;
import com.athena.dolly.controller.common.SSHManager;
import com.athena.dolly.controller.web.common.model.ExtjsGridParam;
import com.athena.dolly.controller.web.common.model.GridJsonResponse;
import com.athena.dolly.controller.web.common.model.SimpleJsonResponse;
import com.athena.dolly.controller.web.machine.Machine;
import com.athena.dolly.controller.web.machine.MachineService;

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
		Machine machine = machineService.retrieve(machineId);
		String repoAddr = DollyConstants.MEERKAT_REPO;
		String command = "";
		if (machine == null) {
			return false;
		}

		SSHManager sshMng = new SSHManager(machine.getSshUsername(),
				machine.getSshPassword(), machine.getSSHIPAddr(), "",
				machine.getSshPort(), 1000);
		String errorMsg = sshMng.connect();
		if (errorMsg != null || errorMsg != "") {
			return false;
		}
		// copy template to target server by sshManager
		command = "scp " + repoAddr + "*" + machine.getSshUsername() + "@"
				+ machine.getSSHIPAddr() + ":/tmp/";
		sshMng.sendCommand(command);
		// extract template to target server by sshManager command
		command = "unzip " + DollyConstants.TOMCAT_ARCHIVE_FILE + "-d ."
				+ " & unzip " + DollyConstants.TOMCAT_ARCHIVE_TEMPLATE_FILE
				+ "-d .";
		// config by sshManager command
		// start by sshManager command
		// store to database
		return false;
	}
}
