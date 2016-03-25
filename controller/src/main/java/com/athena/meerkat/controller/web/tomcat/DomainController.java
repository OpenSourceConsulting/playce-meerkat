package com.athena.meerkat.controller.web.tomcat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.ClusteringConfiguration;
import com.athena.meerkat.controller.web.entities.ClusteringConfigurationVersion;
import com.athena.meerkat.controller.web.entities.CommonCode;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DatagridServerGroup;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.Session;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.resources.services.DataGridServerGroupService;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainRepository;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;
import com.athena.meerkat.controller.web.user.entities.User;

@Controller
@RequestMapping("/domain")
public class DomainController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DomainController.class);
	@Inject
	private TomcatDomainService domainService;
	@Autowired
	private DataGridServerGroupService datagridGroupService;
	@Autowired
	private TomcatInstanceService tomcatService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody SimpleJsonResponse save(SimpleJsonResponse json,
			TomcatDomain domain) {
		boolean isEdit = !(domain.getId() == 0);
		try {
			List<TomcatDomain> existingDomains = domainService
					.getDomainByName(domain.getName());
			if (existingDomains.size() > 0) {
				if (!isEdit) { // add new domain
					json.setSuccess(false);
					json.setMsg("Domain name is already used.");
					return json;
				} else {
					if (domain.getId() != existingDomains.get(0).getId()) {
						json.setSuccess(false);
						json.setMsg("Domain name is already used.");
						return json;
					}
				}
			}
			if (isEdit) {
				TomcatDomain dbDomain = domainService.getDomain(domain.getId());
				if (dbDomain == null) { // edit domain
					json.setSuccess(false);
					json.setMsg("Domain does not exist.");
					return json;
				}
			}
			if (domain.getServerGroup() != null) {
				DatagridServerGroup group = datagridGroupService
						.getGroup(domain.getServerGroup().getId());
				if (group == null) {
					json.setSuccess(false);
					json.setMsg("Datagrid server group does not exist.");
					return json;
				}
				domain.setServerGroup(group);
			}

			User loginUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			domain.setCreateUser(loginUser.getId());
			domainService.save(domain);
		} catch (Exception ex) {
			LOGGER.debug(ex.getMessage());
		}
		json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse edit(SimpleJsonResponse json, int id) {
		TomcatDomain domain = domainService.getDomain(id);
		if (domain == null) {
			json.setSuccess(false);
			json.setMsg("Domain does not exist.");
		} else {
			json.setSuccess(true);
			json.setData(domain);
		}
		return json;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getDomainList(
			SimpleJsonResponse json) {
		List<TomcatDomain> result = domainService.getAll();
		json.setData(result);
		json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/{domainId}/ds/list", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getDatasourceList(
			GridJsonResponse json, @PathVariable int domainId) {
		TomcatDomain td = domainService.getDomain(domainId);
		if (td == null) {
			json.setSuccess(false);
			json.setMsg("Tomcat domain does not exist.");
		} else {
			List<DataSource> dss = (List<DataSource>) td.getDatasources();
			json.setList(dss);
			json.setTotal(dss.size());
		}
		return json;
	}

	@RequestMapping(value = "/{domainId}/config", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getConfig(SimpleJsonResponse json,
			@PathVariable int domainId) {
		TomcatDomain td = domainService.getDomain(domainId);
		if (td == null) {
			json.setSuccess(false);
			json.setMsg("Tomcat domain does not exist.");
		} else {
			json.setData(td.getDomainTomcatConfig());
			json.setData(false);
		}
		return json;
	}

	@RequestMapping(value = "/{domainId}/configfile/{type}/", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getConfigVersionList(
			GridJsonResponse json, @PathVariable int domainId,
			@PathVariable String type) {
		TomcatDomain td = domainService.getDomain(domainId);
		if (td == null) {
			json.setSuccess(false);
			json.setMsg("Tomcat domain does not exist.");
		} else {
			List<TomcatConfigFile> confVersions = domainService
					.getConfigFileVersions(td, type);
			json.setList(confVersions);
			json.setTotal(confVersions.size());
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping(value = "/{domainId}/configfile/{type}/{version}", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getConfigVersionList(
			SimpleJsonResponse json, @PathVariable int domainId,
			@PathVariable String type, @PathVariable int version) {
		TomcatDomain td = domainService.getDomain(domainId);
		String content = "";
		if (td == null) {
			json.setSuccess(false);
			json.setMsg("Tomcat domain does not exist.");
		} else {
			TomcatConfigFile confFile = domainService.getConfig(td, type,
					version);
			if (confFile != null) {
				// load by ssh ....
				content = "load by ssh ....";
			}
			json.setData(content);
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping(value = "/{domainId}/apps", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getApplications(
			GridJsonResponse json, @PathVariable Integer domainId) {
		TomcatDomain td = domainService.getDomain(domainId);
		if (td == null) {
			json.setSuccess(false);
			json.setMsg("Tomcat domain does not exist.");
		} else {
			List<TomcatApplication> apps = td.getTomcatApplication();
			json.setList(apps);
			json.setTotal(apps.size());
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping(value = "/{domainId}/tomcatconfig", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getTomcatConfig(SimpleJsonResponse json,
			@PathVariable Integer domainId) {

		json.setData(domainService.getTomcatConfig(domainId));
		return json;
	}

	@RequestMapping(value = "/{domainId}/sessions", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getSessions(GridJsonResponse json,
			@PathVariable Integer domainId) {

		TomcatDomain td = domainService.getDomain(domainId);
		if (td == null) {
			json.setSuccess(false);
			json.setMsg("Tomcat domain does not exist.");
		} else {
			// get from session severs
			List<Session> sessions = new ArrayList<Session>();
			Session e = new Session();
			e.setId(1);
			e.setKey("Session 1");
			e.setValue("Value session asdasdkjad kajdah dk");
			e.setLocation("Server 1");
			sessions.add(0, e);

			json.setList(sessions);
			json.setTotal(sessions.size());
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping(value = "/{domainId}/clusteringvers", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getClusteringVers(
			GridJsonResponse json, @PathVariable Integer domainId) {
		TomcatDomain td = domainService.getDomain(domainId);
		if (td == null) {
			json.setSuccess(false);
			json.setMsg("Tomcat domain does not exist.");
		} else {
			List<ClusteringConfigurationVersion> versions = domainService
					.getClusteringConfVersions(td);
			json.setList(versions);
			json.setTotal(versions.size());
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping(value = "/{domainId}/clusteringconf/{version}", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getClusteringConf(
			GridJsonResponse json, @PathVariable Integer domainId,
			@PathVariable Integer version) {
		TomcatDomain td = domainService.getDomain(domainId);
		if (td == null) {
			json.setSuccess(false);
			json.setMsg("Tomcat domain does not exist.");
		} else {
			List<ClusteringConfiguration> confs = domainService
					.getClusteringConf(td, version);
			json.setList(confs);
			json.setTotal(confs.size());
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping(value = "/savetomcatconfig", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse saveTomcatConfig(
			SimpleJsonResponse json,
			DomainTomcatConfiguration domainTomcatConfig) {
		DomainTomcatConfiguration currentConf = domainService
				.getTomcatConfig(domainTomcatConfig.getTomcatDomain().getId());
		int id = currentConf.getId();
		currentConf = domainTomcatConfig;
		currentConf.setId(id);
		currentConf.setModifiedDate(new Date());
		User loginUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		currentConf.setModifiedUserId(loginUser.getId());
		if (domainService.saveDomainTomcatConfig(currentConf) == null) {
			json.setSuccess(false);
			json.setMsg("Domain tomcat configuration is fail.");
		}
		return json;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getDomain(SimpleJsonResponse json,
			int id) {
		TomcatDomain result = domainService.getDomain(id);
		if (result == null) {
			json.setSuccess(false);
			json.setMsg("Domain does not exist");
		} else {
			json.setSuccess(true);
			json.setData(result);
		}
		return json;
	}

	@RequestMapping(value = "/tomcatlist", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getTomcatInstanceByDomain(
			SimpleJsonResponse json, int domainId) {
		// ServiceResult result =
		// tomcatService.getTomcatListByDomainId(domainId);
		// if (result.getStatus() == Status.DONE) {
		// List<TomcatInstance> tomcats = (List<TomcatInstance>) result
		// .getReturnedVal();
		// return tomcats;
		// }
		// return null;
		json.setData(tomcatService.getTomcatListByDomainId(domainId));
		json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/applications", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getApplicationsByDomain(
			SimpleJsonResponse json, int domainId) {
		List<TomcatApplication> apps = domainService
				.getApplicationListByDomain(domainId);
		if (apps != null) {
			json.setSuccess(true);
			json.setData(apps);
		} else {
			json.setSuccess(false);
			json.setMsg("Domain does not exist.");
		}
		return json;
	}

	// @RequestMapping(value = "/clustering/config/save", method =
	// RequestMethod.POST)
	// public @ResponseBody SimpleJsonResponse save(SimpleJsonResponse json,
	// ClusteringConfiguration config, int domainId) {
	// boolean isEdit = !(config.getId() == 0);
	//
	// List<ClusteringConfiguration> existingConfigs = domainService
	// .getClusteringConfigurationByName(config.getName());
	// if (existingConfigs.size() > 0) {
	// if (!isEdit
	// || (isEdit && existingConfigs.get(0).getId() != config
	// .getId())) {
	// json.setSuccess(false);
	// json.setMsg("Config name is duplicated.");
	// return json;
	// }
	// }
	// Domain domain = domainService.getDomain(domainId);
	// // config.setDomain(domain);
	// domainService.saveConfig(config);
	// json.setSuccess(true);
	// return json;
	// }

	// @RequestMapping(value = "/clustering/config/edit", method =
	// RequestMethod.POST)
	// public @ResponseBody SimpleJsonResponse editClusteringConfig(
	// SimpleJsonResponse json, int id) {
	// ClusteringConfiguration config = domainService.getConfig(id);
	// if (config == null) {
	// json.setSuccess(false);
	// json.setMsg("This configuration does not exist");
	// } else {
	// json.setSuccess(true);
	// json.setData(config);
	// }
	// return json;
	// }

	// @RequestMapping(value = "/clustering/config/delete", method =
	// RequestMethod.POST)
	// public @ResponseBody SimpleJsonResponse deleteClusteringConfig(
	// SimpleJsonResponse json, int id) {
	// ClusteringConfiguration config = domainService.getConfig(id);
	// if (config == null) {
	// json.setSuccess(false);
	// json.setMsg("This configuration does not exist");
	// } else {
	// domainService.deleteClusteringConfig(config);
	// json.setSuccess(true);
	// }
	// return json;
	// }

	// @RequestMapping(value = "/clustering/config/list", method =
	// RequestMethod.GET)
	// public @ResponseBody GridJsonResponse getClusteringConfigList(
	// GridJsonResponse json, int domainId, int revision) {
	// List<ClusteringConfiguration> configList = domainService
	// .getClusteringConfigurationList(domainId, revision);
	// json.setList(configList);
	// json.setSuccess(true);
	// return json;
	// }

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse delete(SimpleJsonResponse json,
			int domainId) {
		if (domainService.delete(domainId)) {
			json.setMsg("Domain is deleted successfully.");
			json.setSuccess(true);
		} else {
			json.setSuccess(false);
			json.setMsg("Domain does not exist.");
		}
		return json;
	}
}
