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

import java.util.ArrayList;
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
import com.athena.meerkat.controller.common.State;
import com.athena.meerkat.controller.common.provisioning.ProvisioningHandler;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.datasource.Datasource;
import com.athena.meerkat.controller.web.datasource.DatasourceService;
import com.athena.meerkat.controller.web.domain.Domain;
import com.athena.meerkat.controller.web.domain.DomainService;
import com.athena.meerkat.controller.web.machine.Machine;
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
	@Autowired
	private DomainService domainService;
	@Autowired
	private DatasourceService dsService;
	@Inject
	@Named("provisioningHandler")
	private ProvisioningHandler provisioningHandler;

	public TomcatInstanceController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "/instance/start", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse startTomcat(SimpleJsonResponse json, int id) {
		TomcatInstance tomcat = service.findOne(id);
		return changeTomcatState(json, tomcat, State.TOMCAT_STATE_STARTED);
	}

	@RequestMapping(value = "/instance/restart", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse restartTomcat(SimpleJsonResponse json, int id) {
		TomcatInstance tomcat = service.findOne(id);
		json = changeTomcatState(json, tomcat, State.TOMCAT_STATE_STOPPED);
		json = changeTomcatState(json, tomcat, State.TOMCAT_STATE_STARTED);
		return json;
	}

	private SimpleJsonResponse changeTomcatState(SimpleJsonResponse json,
			TomcatInstance tomcat, int state) {
		if (tomcat == null) {
			json.setSuccess(false);
			json.setMsg("Tomcat does not exist.");
		} else {
			boolean success = false;
			// provisioning
			// ..

			// update to db
			if (state == State.TOMCAT_STATE_STARTED) {
				if (tomcat.getState() == State.TOMCAT_STATE_STARTED) {
					json.setSuccess(false);
					json.setMsg("Tomcat is already started.");
				} else {
					success = service.start(tomcat); // provisioning &&
														// service.start(tomcat);
					json.setData(State.TOMCAT_STATE_STARTED);
				}
			} else if (state == State.TOMCAT_STATE_STOPPED) {
				if (tomcat.getState() == State.TOMCAT_STATE_STOPPED) {
					json.setSuccess(false);
					json.setMsg("Tomcat is already stopped.");
				} else {
					success = service.stop(tomcat); // provisioning &&
					// service.stop(tomcat);
					json.setData(State.TOMCAT_STATE_STOPPED);
				}
			}

			json.setSuccess(success);
		}
		return json;
	}

	@RequestMapping(value = "/instance/stop", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse stopTomcat(SimpleJsonResponse json, int id) {
		TomcatInstance tomcat = service.findOne(id);
		return changeTomcatState(json, tomcat, State.TOMCAT_STATE_STOPPED);
	}

	@RequestMapping(value = "/instance/get")
	@ResponseBody
	public SimpleJsonResponse getTomcat(SimpleJsonResponse json, int id) {
		TomcatInstance tomcat = service.findOne(id);
		if (tomcat == null) {
			json.setSuccess(false);
			json.setMsg("Tomcat instance does not exist.");
		} else {
			json.setSuccess(true);
			json.setData(tomcat);
		}
		return json;
	}

	@RequestMapping("/instance/list")
	public @ResponseBody
	SimpleJsonResponse getTomcatInstance(SimpleJsonResponse json) {
		List<TomcatInstance> tomcats = service.getAll();
		json.setData(tomcats);
		json.setSuccess(true);
		return json;
	}

	@RequestMapping("/instance/listbydomain")
	public @ResponseBody
	SimpleJsonResponse getTomcatInstances(SimpleJsonResponse json, int domainId) {
		Domain domain = domainService.getDomain(domainId);
		if (domain == null) {
			json.setSuccess(false);
			json.setMsg("Domain does not exist.");
		} else {
			json.setData(service.getTomcatListByDomainId(domain.getId()));
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping("/instance/datasource")
	public @ResponseBody
	SimpleJsonResponse getDatasourceByTomcat(SimpleJsonResponse json,
			int tomcatId) {
		TomcatInstance tomcat = service.findOne(tomcatId);
		if (tomcat == null) {
			json.setSuccess(false);
			json.setMsg("Tomcat does not exist");
		} else {
			List<Datasource> datasources = service
					.getDatasourceListByTomcat(tomcat);
			json.setSuccess(true);
			json.setData(datasources);
		}
		return json;
	}

	@RequestMapping("/save")
	@ResponseBody
	// add more param later
	public SimpleJsonResponse save(SimpleJsonResponse json,
			TomcatInstance tomcat, int machineId, int domainId, String dsIds,
			boolean autoRestart) {
		// check existing
		TomcatInstance existingTomcat = service.findByNameAndDomain(
				tomcat.getName(), domainId);
		if (existingTomcat != null) {
			if (existingTomcat.getId() != tomcat.getId()) {
				json.setSuccess(false);
				json.setMsg("Tomcat name is duplicated.");
				return json;
			}
		}
		Machine machine = machineService.retrieve(machineId);
		Domain domain = domainService.getDomain(domainId);
		if (machine != null) {
			tomcat.setMachine(machine);
		}
		if (domain != null) {
			tomcat.setDomain(domain);
		}

		List<Datasource> datasources = new ArrayList<Datasource>();
		String[] idStrings = dsIds.split("#", 0);
		for (int i = 1; i < idStrings.length; i++) { // the first element is
														// empty
			Datasource ds = dsService.findOne(Integer.parseInt(idStrings[i]));
			if (ds != null) {
				datasources.add(ds);
			}
		}
		tomcat.associateDatasources(datasources);

		boolean provisioning_result = true; // change to false when implement
											// provisioning
			// provisioning section
			// ....
			//
		if (provisioning_result) {
			TomcatInstance tc = service.save(tomcat);
			if (tc != null) {
				json.setData(tc);
				json.setSuccess(true);

			} else {
				json.setSuccess(false);
				json.setMsg("An error occurred while saving tomcat instance to database.");
			}
		} else {
			json.setSuccess(false);
			json.setMsg("An error occurred while provisioning to target server.");
		}

		if (autoRestart) {
			service.start(tomcat);
		}

		// String repoAddr = DollyConstants.MEERKAT_REPO;
		/* sample data */
		// String user = "root";
		// String version = "7.0.54";
		// String java_home = "/usr/java/default";
		// String server_home = "/root";
		// String server_name = "rootServer21";
		// String catalina_home = "/root/jboss-ews-2.1/tomcat7";
		// String catalina_base = "/root/Servers/rootServer21222";
		// String encoding = "UTF-8";
		// int port_offset = 0;
		// int heap_size = 1024;
		// int permgen_size = 256;
		// boolean is_enable_http = true;
		// boolean is_high_availability = true;
		// String bind_address = "192.168.0.88";
		// String other_bind_address = "";
		// boolean is_start_service = true;

		return json;
	}
}
