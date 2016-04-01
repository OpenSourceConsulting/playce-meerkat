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
package com.athena.meerkat.controller.web.tomcat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.common.State;
import com.athena.meerkat.controller.common.provisioning.ProvisioningHandler;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.common.util.WebUtil;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstConfig;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.resources.services.DataSourceService;
import com.athena.meerkat.controller.web.resources.services.ServerService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;
import com.athena.meerkat.controller.web.tomcat.viewmodels.TomcatInstanceViewModel;

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
	private ServerService machineService;
	@Autowired
	private TomcatDomainService domainService;
	@Autowired
	private DataSourceService dsService;
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
		if (tomcat != null) {
			TomcatInstanceViewModel viewmodel = new TomcatInstanceViewModel(
					tomcat);
			// get configurations that are different to domain tomcat config
			List<TomcatInstConfig> changedConfigs = service
					.getTomcatInstConfigs(tomcat.getId());
			if (changedConfigs == null || changedConfigs.size() <= 0) {
				DomainTomcatConfiguration domainConf = domainService
						.getTomcatConfig(tomcat.getDomainId());
				viewmodel.setHttpPort(domainConf.getHttpPort());
				viewmodel.setAjpPort(domainConf.getAjpPort());
				viewmodel.setRedirectPort(domainConf.getRedirectPort());
				viewmodel.setTomcatVersion(domainConf.getTomcatVersion());
			} else {
				for (TomcatInstConfig c : changedConfigs) {
					if (c.getConfigName() == MeerkatConstants.TOMCAT_INST_CONFIG_HTTPPORT_NAME) {
						viewmodel.setHttpPort(Integer.parseInt(c
								.getConfigValue()));
					}
				}
			}
			json.setData(viewmodel);
		}
		return json;
	}

	@RequestMapping(value = "/instance/{id}/conf", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getTomcatConf(SimpleJsonResponse json,
			@PathVariable Integer id) {
		TomcatInstance tomcat = service.findOne(id);

		if (tomcat != null) {
			DomainTomcatConfiguration conf = domainService
					.getTomcatConfig(tomcat.getDomainId());
			// get configurations that are different to domain tomcat config
			List<TomcatInstConfig> changedConfigs = service
					.getTomcatInstConfigs(tomcat.getId());
			if (changedConfigs != null) {
				for (TomcatInstConfig c : changedConfigs) {
					if (c.getConfigName() == MeerkatConstants.TOMCAT_INST_CONFIG_HTTPPORT_NAME) {
						conf.setHttpPort(Integer.parseInt(c.getConfigValue()));
					} else if (c.getConfigName() == MeerkatConstants.TOMCAT_INST_CONFIG_JAVAHOME_NAME) {
						conf.setJavaHome(c.getConfigValue());
					} else if (c.getConfigName() == MeerkatConstants.TOMCAT_INST_CONFIG_SESSION_TIMEOUT_NAME) {
						conf.setSessionTimeout(Integer.parseInt(c
								.getConfigValue()));
					}
				}
			}
			json.setData(conf);
		}
		return json;
	}

	@RequestMapping("/instance/list")
	public @ResponseBody SimpleJsonResponse getTomcatInstance(
			SimpleJsonResponse json) {
		List<TomcatInstance> tomcats = service.getAll();
		json.setData(tomcats);
		json.setSuccess(true);
		return json;
	}

	@RequestMapping("/instance/listbydomain")
	public @ResponseBody SimpleJsonResponse getTomcatInstances(
			SimpleJsonResponse json, int domainId) {
		TomcatDomain domain = domainService.getDomain(domainId);
		if (domain == null) {
			json.setSuccess(false);
			json.setMsg("Domain does not exist.");
		} else {
			json.setData(service.getTomcatListByDomainId(domain.getId()));
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping(value = "/instance/{id}/ds", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getDatasourceByTomcat(
			GridJsonResponse json, @PathVariable Integer id) {
		TomcatInstance tomcat = service.findOne(id);

		List<DataSource> datasources = domainService
				.getDatasourceByDomainId(tomcat.getDomainId());
		json.setList(datasources);
		json.setTotal(datasources.size());

		return json;
	}

	@RequestMapping("/save")
	@ResponseBody
	// add more param later
	public SimpleJsonResponse save(SimpleJsonResponse json,
			TomcatInstance tomcat, int machineId, int domainId, String dsIds,
			boolean autoRestart) {
		// check existing
		// TomcatInstance existingTomcat = service.findByNameAndDomain(
		// tomcat.getName(), domainId);
		TomcatInstance existingTomcat = new TomcatInstance();
		if (existingTomcat != null) {
			if (existingTomcat.getId() != tomcat.getId()) {
				json.setSuccess(false);
				json.setMsg("Tomcat name is duplicated.");
				return json;
			}
		}
		Server machine = machineService.retrieve(machineId);
		TomcatDomain domain = domainService.getDomain(domainId);
		// if (machine != null) {
		// tomcat.setMachine(machine);
		// }
		// if (domain != null) {
		// tomcat.setDomain(domain);
		// }

		List<DataSource> datasources = new ArrayList<DataSource>();
		String[] idStrings = dsIds.split("#", 0);
		for (int i = 1; i < idStrings.length; i++) { // the first element is
														// empty
			DataSource ds = dsService.findOne(Integer.parseInt(idStrings[i]));
			if (ds != null) {
				datasources.add(ds);
			}
		}
		// tomcat.associateDatasources(datasources);

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

	@RequestMapping(value = "/saveList", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse saveList(SimpleJsonResponse json,
			@RequestBody List<TomcatInstance> tomcats) {

		int loginUserId = WebUtil.getLoginUserId();

		for (TomcatInstance tomcatInstance : tomcats) {

			TomcatDomain domain = new TomcatDomain();
			domain.setId(tomcatInstance.getDomainId());

			Server server = new Server();
			server.setId(tomcatInstance.getServerId());

			tomcatInstance.setTomcatDomain(domain);
			tomcatInstance.setServer(server);
			tomcatInstance.setCreateUserId(loginUserId);
		}

		service.saveList(tomcats);

		return json;
	}

}
