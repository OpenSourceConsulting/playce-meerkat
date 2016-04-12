package com.athena.meerkat.controller.web.tomcat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.common.util.WebUtil;
import com.athena.meerkat.controller.web.entities.ClusteringConfiguration;
import com.athena.meerkat.controller.web.entities.ClusteringConfigurationVersion;
import com.athena.meerkat.controller.web.entities.DatagridServerGroup;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.Session;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatDomainDatasource;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.resources.services.DataGridServerGroupService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatConfigFileService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;
import com.athena.meerkat.controller.web.tomcat.viewmodels.ClusteringConfComparisionViewModel;

@Controller
@RequestMapping("/domain")
public class DomainController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DomainController.class);
	@Autowired
	private TomcatDomainService domainService;
	@Autowired
	private TomcatConfigFileService tomcatConfigFileService;
	@Autowired
	private DataGridServerGroupService datagridGroupService;
	@Autowired
	private TomcatInstanceService tomcatService;

	@Autowired
	private CommonCodeHandler commonHandler;

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

			domain.setCreateUser(WebUtil.getLoginUserId());
			domain = domainService.save(domain);
		} catch (Exception ex) {
			LOGGER.debug(ex.getMessage());
		}
		json.setSuccess(true);
		json.setData(domain.getId());
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

	@RequestMapping(value = "/saveWithConfig", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse saveWithConfig(SimpleJsonResponse json,
			TomcatDomain domain, DomainTomcatConfiguration config) {

		int loginUserId = WebUtil.getLoginUserId();

		domain.setCreateUser(loginUserId);

		config.setModifiedUserId(loginUserId);

		domainService.saveWithConfig(domain, config);

		json.setData(domain);

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
	public @ResponseBody GridJsonResponse getDomainList(GridJsonResponse json) {
		List<TomcatDomain> result = domainService.getAll();
		json.setList(result);
		json.setTotal(result.size());
		return json;
	}

	@RequestMapping(value = "/{domainId}/apps", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getApplications(
			GridJsonResponse json, @PathVariable Integer domainId) {
		TomcatDomain td = domainService.getDomain(domainId);
		if (td != null) {
			List<TomcatApplication> apps = td.getTomcatApplication();
			json.setList(apps);
			json.setTotal(apps.size());
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
			// TODO idkbj get from session servers
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
		if (td != null) {
			List<ClusteringConfigurationVersion> versions = domainService
					.getClusteringConfVersions(td);
			json.setList(versions);
			json.setTotal(versions.size());
		}
		return json;
	}

	@RequestMapping(value = "/{domainId}/clusteringconf/{version}", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getClusteringConf(
			GridJsonResponse json, @PathVariable Integer domainId,
			@PathVariable Integer version) {
		TomcatDomain td = domainService.getDomain(domainId);
		if (td != null) {
			List<ClusteringConfiguration> confs = domainService
					.getClusteringConf(td, version);
			json.setList(confs);
			json.setTotal(confs.size());
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
		currentConf.setModifiedUserId(WebUtil.getLoginUserId());
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
		json.setData(result);

		return json;
	}

	@RequestMapping(value = "/tomcatlist", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getTomcatInstanceByDomain(
			GridJsonResponse json, int domainId) {
		json.setList(tomcatService.getTomcatListByDomainId(domainId));
		json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/clustering/config/save", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse saveClusteringConfig(
			SimpleJsonResponse json, ClusteringConfiguration config,
			Integer tomcatDomainId) {
		boolean isEdit = !(config.getId() == 0);

		ClusteringConfiguration currentConfig = domainService
				.getClusteringConfig(config.getId());
		if (tomcatDomainId > 0) {
			TomcatDomain domain = domainService.getDomain(tomcatDomainId);
			config.setTomcatDomain(domain);
			List<ClusteringConfiguration> confs = null;
			ClusteringConfigurationVersion latestVersion = domainService
					.getLatestClusteringConfVersion(domain.getId());
			ClusteringConfigurationVersion versionObj = new ClusteringConfigurationVersion();
			versionObj.setCreatedTime(new Date());
			if (latestVersion == null) {
				versionObj.setVersion(1);
			} else {
				confs = domainService.getClusteringConf(domain,
						latestVersion.getId());
				versionObj.setVersion(latestVersion.getVersion() + 1);
			}
			versionObj = domainService.saveCluteringConfVersion(versionObj);
			List<ClusteringConfiguration> cloneConfs = new ArrayList<ClusteringConfiguration>();
			if (confs != null) {
				for (ClusteringConfiguration c : confs) {
					if (c.getId() != config.getId()) { // dont clone the edited
														// config
						ClusteringConfiguration clone = (ClusteringConfiguration) c
								.clone();
						if (clone != null) {
							clone.setClusteringConfigurationVersion(versionObj);
							cloneConfs.add(clone);
						}
					}
				}
				// TODO idkbj apply to clustering server
				domainService.saveClusteringConfigs(cloneConfs);
			}
			if (config.getId() != 0) {
				// reset id for editing case
				config.setId(0);
			}
			config.setClusteringConfigurationVersion(versionObj);
			domainService.saveClusteringConfig(config);
			json.setData(versionObj.getId());
			json.setSuccess(true);
		}

		return json;
	}

	@RequestMapping(value = "/clustering/config/edit", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse editClusteringConfig(
			SimpleJsonResponse json, int id) {
		ClusteringConfiguration config = domainService.getClusteringConfig(id);
		json.setData(config);
		return json;
	}

	@RequestMapping(value = "/clustering/config/delete", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse deleteClusteringConfig(
			SimpleJsonResponse json, int id) {
		ClusteringConfiguration config = domainService.getClusteringConfig(id);
		ClusteringConfigurationVersion latestVersion = domainService
				.getLatestClusteringConfVersion(config.getTomcatDomainId());
		// if this is not latest version, just delete
		if (config.getClusteringConfigurationVersion().getId() != latestVersion
				.getId()) {
			domainService.deleteClusteringConfig(config);
			json.setData(latestVersion.getId());
		} else {
			ClusteringConfigurationVersion versionObj = new ClusteringConfigurationVersion();
			versionObj.setCreatedTime(new Date());
			versionObj.setVersion(latestVersion.getVersion() + 1);
			versionObj = domainService.saveCluteringConfVersion(versionObj);
			List<ClusteringConfiguration> confs = domainService
					.getClusteringConf(config.getTomcatDomain(),
							latestVersion.getId());
			List<ClusteringConfiguration> cloneConfs = new ArrayList<ClusteringConfiguration>();
			if (confs != null) {
				for (ClusteringConfiguration c : confs) {
					if (c.getId() != config.getId()) {
						ClusteringConfiguration clone = (ClusteringConfiguration) c
								.clone();
						if (clone != null) {
							clone.setClusteringConfigurationVersion(versionObj);
							cloneConfs.add(clone);
						}
					}
				}
				// TODO idkbj apply to clustering servers
				domainService.saveClusteringConfigs(cloneConfs);
			}
			json.setData(versionObj.getId());
		}

		json.setSuccess(true);

		return json;
	}

	@RequestMapping(value = "/{domainId}/clustering/config/compare/{firstVersion}/to/{secondVersion}", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse compareClusteringConfig(
			GridJsonResponse json, @PathVariable Integer domainId,
			@PathVariable Integer firstVersion,
			@PathVariable Integer secondVersion) {
		List<ClusteringConfComparisionViewModel> viewmodels = domainService
				.getClusteringConfComparison(domainId, firstVersion,
						secondVersion);
		json.setList(viewmodels);
		json.setTotal(viewmodels.size());
		return json;
	}

	@RequestMapping(value = "/{domainId}/clustering/config/latest", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getLatestClusteringVersion(
			SimpleJsonResponse json, @PathVariable Integer domainId) {

		ClusteringConfigurationVersion latestVersion = domainService
				.getLatestClusteringConfVersion(domainId);
		if (latestVersion != null) {
			json.setData(latestVersion.getId());
		}
		return json;
	}

	@RequestMapping(value = "/{domainId}/clustering/config/{versionId}/search/{keyword}", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse compareClusteringConfig(
			GridJsonResponse json, @PathVariable int domainId,
			@PathVariable int versionId, @PathVariable String keyword) {
		List<ClusteringConfiguration> list = domainService
				.searchClusteringConfByDomainAndVersionAndName(domainId,
						versionId, keyword);
		json.setList(list);
		json.setTotal(list.size());
		return json;
	}

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

	@RequestMapping(value = "/saveDatasources", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse saveDatasources(SimpleJsonResponse json,
			@RequestBody List<TomcatDomainDatasource> datasources) {

		domainService.saveDatasources(datasources);

		return json;
	}

	@RequestMapping(value = "/{domainId}/tomcat/search/{keyword}", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse search(GridJsonResponse json,
			@PathVariable Integer domainId, @PathVariable String keyword) {
		List<TomcatInstance> result = tomcatService.findByNameAndDomain(
				keyword, domainId);
		json.setList(result);
		json.setTotal(result.size());
		return json;
	}
}
