package com.athena.meerkat.controller.web.tomcat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.common.util.WebUtil;
import com.athena.meerkat.controller.web.entities.CommonCode;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.MonAlertConfig;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.TaskHistory;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatDomainDatasource;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.monitoring.stat.AlertSettingService;
import com.athena.meerkat.controller.web.provisioning.TomcatProvisioningService;
import com.athena.meerkat.controller.web.resources.services.DataGridServerGroupService;
import com.athena.meerkat.controller.web.resources.services.DataSourceService;
import com.athena.meerkat.controller.web.tomcat.services.TaskHistoryService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatConfigFileService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;

@Controller
@RequestMapping("/domain")
public class DomainController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DomainController.class);

	@Autowired
	private TomcatDomainService domainService;

	@Autowired
	private DataSourceService dsService;

	@Autowired
	private TomcatConfigFileService tomcatConfigFileService;

	@Autowired
	private DataGridServerGroupService datagridGroupService;

	@Autowired
	private TomcatInstanceService tomcatService;

	@Autowired
	private CommonCodeHandler commonHandler;

	@Autowired
	private TomcatProvisioningService proviService;

	@Autowired
	private TaskHistoryService taskService;

	@Autowired
	private AlertSettingService alertService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody SimpleJsonResponse save(SimpleJsonResponse json, TomcatDomain domain) {
		boolean isEdit = domain.getId() > 0;

		List<TomcatDomain> existingDomains = domainService.getDomainByName(domain.getName());
		if (existingDomains.size() > 0) {
			if (isEdit) {

				for (TomcatDomain tomcatDomain : existingDomains) {
					if (tomcatDomain.getId() != domain.getId()) {
						json.setSuccess(false);
						json.setMsg("Domain name is already used.");
						break;
					}
				}

			} else {
				// add new domain
				json.setSuccess(false);
				json.setMsg("Domain name is already used.");
			}
		}

		if (json.isSuccess() == false) {

			return json;
		}

		domain.setCreateUser(WebUtil.getLoginUserId());
		domain = domainService.save(domain);

		if (isEdit) {
			// for configure session clustering.
			List<TomcatInstance> tomcats = tomcatService.getTomcatListByDomainId(domain.getId());
			domain.setTomcatInstances(tomcats);
		}

		json.setData(domain);
		return json;
	}

	@RequestMapping(value = "/{domainId}/config", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getConfig(SimpleJsonResponse json, @PathVariable int domainId) {
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
	public SimpleJsonResponse saveWithConfig(SimpleJsonResponse json, TomcatDomain domain, DomainTomcatConfiguration config) {

		int loginUserId = WebUtil.getLoginUserId();

		domain.setCreateUser(loginUserId);

		config.setModifiedUserId(loginUserId);

		domainService.saveWithConfig(domain, config);

		json.setData(domain);

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
	public @ResponseBody GridJsonResponse getApplications(GridJsonResponse json, @PathVariable Integer domainId) {
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
	public SimpleJsonResponse getTomcatConfig(SimpleJsonResponse json, @PathVariable Integer domainId) {

		json.setData(domainService.getTomcatConfig(domainId));
		return json;
	}

	@RequestMapping(value = "/conf/save", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse saveTomcatConfig(SimpleJsonResponse json, DomainTomcatConfiguration domainTomcatConfig, boolean changeRMI) {
		DomainTomcatConfiguration savedConfig = null;
		if (domainTomcatConfig.getId() == 0) {
			CommonCode tomcatVersion = commonHandler.getCode(domainTomcatConfig.getTomcatVersionCd());
			if (tomcatVersion != null) {
				domainTomcatConfig.setCatalinaHome(domainTomcatConfig.getCatalinaHome() + "/" + tomcatVersion.getCodeNm());
			}
			savedConfig = domainService.saveNewDomainTomcatConfig(domainTomcatConfig);
		} else {
			savedConfig = domainService.saveDomainTomcatConfig(domainTomcatConfig);
		}
		if (savedConfig != null) {
			//proviService.updateTomcatInstanceConfig(savedConfig.getTomcatDomain().getId(), changeRMI, null);

			TaskHistory task = taskService.createTasks(savedConfig.getTomcatDomain().getId(), MeerkatConstants.TASK_CD_TOMCAT_CONFIG_UPDATE);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("configId", savedConfig.getId());
			resultMap.put("task", task);

			json.setData(resultMap);

		} else {
			json.setMsg("Configuration is failed.");
			json.setSuccess(false);
		}

		return json;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getDomain(SimpleJsonResponse json, int id) {
		TomcatDomain result = domainService.getDomain(id);
		json.setData(result);

		return json;
	}

	@RequestMapping(value = "/tomcatlist", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getTomcatInstanceByDomain(GridJsonResponse json, int domainId) {
		json.setList(tomcatService.getTomcatListByDomainId(domainId));
		json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse delete(SimpleJsonResponse json, int domainId) {
		if (domainService.delete(domainId)) {
			json.setMsg("Domain is deleted successfully.");
			json.setSuccess(true);
		} else {
			json.setSuccess(false);
			json.setMsg("Domain does not exist.");
		}
		return json;
	}

	@RequestMapping(value = "/datasource/list", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getDatasourceList(GridJsonResponse json, @RequestParam(value = "domainId") int domainId) {
		List<DataSource> datasources = domainService.getDatasources(domainId);
		json.setList(datasources);
		json.setTotal(datasources.size());

		return json;
	}

	@RequestMapping(value = "/server/availist", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getAvaiableServers(GridJsonResponse json, @RequestParam(value = "domainId") int domainId) {
		List<Server> servers = domainService.getAvailableServers(domainId);
		json.setList(servers);
		json.setTotal(servers.size());

		return json;
	}

	/**
	 * <pre>
	 * Domain 관리화면에서의 추가.
	 * </pre>
	 * 
	 * @param json
	 * @param datasources
	 * @return
	 */
	@RequestMapping(value = "/addDatasources", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse saveDatasources(SimpleJsonResponse json, @RequestBody List<TomcatDomainDatasource> datasources) {

		TomcatConfigFile confFile = domainService.addDatasources(datasources);
		
		TaskHistory task = taskService.createTasks(confFile.getTomcatDomain().getId(), MeerkatConstants.TASK_CD_DATASOURCE_ADD);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("updatedXml", confFile);
		resultMap.put("task", task);

		json.setData(resultMap);

		return json;
	}

	/**
	 * <pre>
	 * wizard ui를 통한 추가
	 * </pre>
	 * 
	 * @param json
	 * @param datasources
	 * @return
	 */
	@RequestMapping(value = "/saveFirstDatasources", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse saveFirstDatasources(SimpleJsonResponse json, @RequestBody List<TomcatDomainDatasource> datasources) {

		domainService.saveFirstDatasources(datasources);

		return json;
	}

	@RequestMapping(value = "/datasource/delete", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse deleteDatasourceMapping(SimpleJsonResponse json, int domainId, int dsId) {

		domainService.deleteDomainDatasource(domainId, dsId);
		
		return json;
	}
	
	@RequestMapping(value = "/datasource/rmupdate", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse rmUpdateContextXml(SimpleJsonResponse json, int domainId, int dsId) {

		TomcatConfigFile updatedXml = domainService.rmUpdateContextXml(domainId, dsId);
		
		TaskHistory task = taskService.createTasks(domainId, MeerkatConstants.TASK_CD_DATASOURCE_REMOVE);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("updatedXml", updatedXml);
		resultMap.put("task", task);

		json.setData(resultMap);
		
		return json;
	}

	@RequestMapping(value = "/{domainId}/tomcat/search/{keyword}", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse search(GridJsonResponse json, @PathVariable Integer domainId, @PathVariable String keyword) {
		List<TomcatInstance> result = tomcatService.findByNameAndDomain(keyword, domainId);
		json.setList(result);
		json.setTotal(result.size());
		return json;
	}

	@RequestMapping(value = "/{domainId}/alertsettings", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getAlertSettingList(GridJsonResponse json, @PathVariable Integer domainId) {
		TomcatDomain td = domainService.getDomain(domainId);
		if (td != null) {
			List<MonAlertConfig> alertSettings = td.getMonAlertConfigs();

			json.setList(alertSettings);
			json.setTotal(alertSettings.size());
		}
		return json;
	}
}
