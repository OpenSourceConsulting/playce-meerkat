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
import com.athena.meerkat.controller.web.entities.Session;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstConfig;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.resources.services.DataSourceService;
import com.athena.meerkat.controller.web.resources.services.ServerService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatConfigFileService;
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
	@Autowired
	private TomcatConfigFileService tomcatConfigFileService;
	@Inject
	@Named("provisioningHandler")
	private ProvisioningHandler provisioningHandler;

	public TomcatInstanceController() {
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

		boolean success = false;
		// TODO idkjwon provisioning
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
			// set latest Config file id
			TomcatConfigFile latestServerXml = tomcatConfigFileService
					.getLatestConfVersion(null, tomcat,
							MeerkatConstants.CONFIG_FILE_TYPE_SERVER_XML);
			TomcatConfigFile latestContextXml = tomcatConfigFileService
					.getLatestConfVersion(null, tomcat,
							MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML);
			if (latestServerXml != null) {
				viewmodel.setLatestServerXmlConfigFileId(latestServerXml
						.getId());
			}
			if (latestContextXml != null) {
				viewmodel.setLatestContextXmlConfigFileId(latestContextXml
						.getId());
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
		return json;
	}

	@RequestMapping("/instance/listbydomain")
	public @ResponseBody SimpleJsonResponse getTomcatInstances(
			SimpleJsonResponse json, int domainId) {
		TomcatDomain domain = domainService.getDomain(domainId);
		if (domain != null) {
			json.setData(service.getTomcatListByDomainId(domain.getId()));
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

	@RequestMapping(value = "/instance/{id}/apps", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getAppsByTomcat(
			GridJsonResponse json, @PathVariable Integer id) {
		TomcatInstance tomcat = service.findOne(id);
		if (tomcat != null) {
			/* example */
			// TODO idkjwon loading
			List<TomcatApplication> domainApps = domainService
					.getApplicationListByDomain(tomcat.getDomainId());
			List<TomcatApplication> tomcatApps = service
					.getApplicationByTomcat(tomcat.getId());
			tomcatApps.addAll(domainApps);
			json.setList(tomcatApps);
			json.setTotal(tomcatApps.size());
		}
		return json;
	}

	@RequestMapping(value = "/instance/{id}/sessions", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getSessionsByTomcat(
			GridJsonResponse json, @PathVariable Integer id) {
		TomcatInstance tomcat = service.findOne(id);
		if (tomcat != null) {
			// TODO idkjwon get from clustering session server
			List<Session> sessions = new ArrayList<Session>();
			Session e = new Session();
			e.setId(1);
			e.setKey("Session 1");
			e.setValue("Value session asdasdkjad kajdah dk");
			e.setLocation("Server 1");
			sessions.add(0, e);

			json.setList(sessions);
			json.setTotal(sessions.size());
		}
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
