package com.athena.meerkat.controller.web.tomcat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
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
import org.springframework.web.servlet.ModelAndView;

import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.common.util.WebUtil;
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
import com.athena.meerkat.controller.web.entities.TomcatDomainDatasource;
import com.athena.meerkat.controller.web.resources.services.DataGridServerGroupService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;
import com.athena.meerkat.controller.web.tomcat.viewmodels.ClusteringConfComparisionViewModel;

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
			domainService.save(domain);
		} catch (Exception ex) {
			LOGGER.debug(ex.getMessage());
		}
		json.setSuccess(true);
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
		json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/{domainId}/ds/list", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getDatasourceList(
			GridJsonResponse json, @PathVariable Integer domainId) {

		List<DataSource> dss = domainService.getDatasourceByDomainId(domainId);
		json.setList(dss);
		json.setTotal(dss.size());

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

		List<TomcatConfigFile> confVersions = domainService
				.getConfigFileVersions(td, type);
		json.setList(confVersions);
		json.setTotal(confVersions.size());
		json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/configfile/save", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse saveConfigFile(
			SimpleJsonResponse json, String content, int id, int domainId,
			String type) {
		TomcatDomain td = domainService.getDomain(domainId);
		TomcatConfigFile dbConf = domainService.getTomcatConfigFileById(id);
		if (td != null) {
			TomcatConfigFile latestVersion = domainService
					.getLatestConfVersion(domainId, type);
			TomcatConfigFile conf = new TomcatConfigFile();
			conf.setCreatedTime(new Date());
			conf.setCreateUserId(WebUtil.getLoginUserId());
			conf.setTomcatDomain(td);
			CommonCode code = commonHandler.getCode(type);
			if (code != null) {
				conf.setFileTypeCdId(code.getId());
			}
			if (latestVersion != null) {
				conf.setVersion(latestVersion.getVersion() + 1);
			} else {
				conf.setVersion(1);
			}
			// TODO idkbj: save by provisioning .... and get path

			String path = conf.getVersion() + type;
			conf.setFilePath(path);
			conf = domainService.saveConfigFile(conf);
			json.setData(conf.getId());
		}
		return json;
	}

	@RequestMapping(value = "/{domainId}/configfile/{type}/{version}", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getConfigVersionList(
			SimpleJsonResponse json, @PathVariable Integer domainId,
			@PathVariable String type, @PathVariable Integer version) {
		TomcatDomain td = domainService.getDomain(domainId);
		String content = "";
		if (td == null) {
			json.setSuccess(false);
			json.setMsg("Tomcat domain does not exist.");
		} else {
			TomcatConfigFile confFile = domainService.getConfig(td, type,
					version);
			if (confFile != null) {
				// TODO idkbj: load by ssh ....
				content = "load by ssh ...." + version;
			}
			json.setData(content);
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping(value = "/configfile/{configFileId}", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getConfigVersionList(
			SimpleJsonResponse json, @PathVariable Integer configFileId) {
		TomcatConfigFile file = domainService
				.getTomcatConfigFileById(configFileId);
		if (file != null) {
			// TODO: idkbj load content by provisioning
			String content = "loading ....." + configFileId.toString();
			json.setData(content);
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

		int latestVersionId = 0;
		if (result != null) {
			ClusteringConfigurationVersion latestVersion = domainService
					.getLatestClusteringConfVersion(result.getId());
			if (latestVersion != null) {
				latestVersionId = latestVersion.getId();
			}
		}

		TomcatConfigFile latestServerXmlVersion = domainService
				.getLatestConfVersion(id, "server.xml");
		TomcatConfigFile latestContextXmlVersion = domainService
				.getLatestConfVersion(id, "context.xml");
		result.setLatestServerXmlVersion(latestServerXmlVersion == null ? 0
				: latestServerXmlVersion.getId());
		result.setLatestContextXmlVersion(latestContextXmlVersion == null ? 0
				: latestContextXmlVersion.getId());
		result.setLatestConfVersionId(latestVersionId);

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

	@RequestMapping(value = "/{domainId}/applications", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getApplicationsByDomain(
			GridJsonResponse json, @PathVariable Integer domainId) {
		List<TomcatApplication> apps = domainService
				.getApplicationListByDomain(domainId);
		if (apps != null) {
			json.setSuccess(true);
			json.setList(apps);
			json.setTotal(apps.size());
		} else {
			json.setSuccess(false);
			json.setMsg("Domain does not exist.");
		}
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

	@RequestMapping("/configfile/diff/{firstId}/{secondId}")
	public String diff(Map<String, String> model,
			@PathVariable Integer firstId, @PathVariable Integer secondId) {
		TomcatConfigFile firstConfig = domainService
				.getTomcatConfigFileById(firstId);
		TomcatConfigFile secondConfig = domainService
				.getTomcatConfigFileById(secondId);
		String content = "";
		if (firstConfig != null) {
			// TODO idkbj get content of config file and put to model
			content = "18:55:51.351 [http-nio-8080-exec-7] DEBUG o.s.s.w.c.SecurityContextPersistenceFilter - SecurityContextHolder now cleared, as request processing completed";
			model.put("firstConfig", content);
			model.put("firstConfigVersion", firstConfig.getVersionAndTime());
		}
		if (secondConfig != null) {
			// TODO idkbj get content of config file and put to model
			content = "18:55:51.351 [http-nio-8080-exec-7] DEBUG o.s.s.w.c.SecurityC3213123ntextPersistenceFilter - SecurityContextHolder now cleared, as request processing completed";
			model.put("secondConfig", content);
			model.put("secondConfigVersion", secondConfig.getVersionAndTime());
		}
		return "configdiff";
	}
}
