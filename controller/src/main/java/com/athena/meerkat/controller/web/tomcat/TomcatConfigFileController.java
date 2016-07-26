	package com.athena.meerkat.controller.web.tomcat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.CommonCode;
import com.athena.meerkat.controller.web.entities.TaskHistory;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.services.TaskHistoryService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatConfigFileService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;

@Controller
@RequestMapping("/configfile")
public class TomcatConfigFileController {

	@Autowired
	private TomcatDomainService domainService;

	@Autowired
	private CommonCodeHandler commonHandler;

	@Autowired
	private TomcatInstanceService tomcatService;

	@Autowired
	private TomcatConfigFileService tomcatConfigFileService;

	@Autowired
	private TaskHistoryService taskService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse saveConfigFile(SimpleJsonResponse json, String content, String confType, String objType, int id, int objId) {
		TomcatDomain td = null;
		TomcatInstance tcinst = null;
		TomcatConfigFile conf = new TomcatConfigFile();

		if (objType.equals(MeerkatConstants.OBJ_TYPE_DOMAIN)) {
			td = domainService.getDomain(objId);
			conf.setTomcatDomain(td);

		} else if (objType.equals(MeerkatConstants.OBJ_TYPE_TOMCAT)) {
			tcinst = tomcatService.findOne(objId);
			conf.setTomcatInstance(tcinst);
			conf.setTomcatDomain(tcinst.getTomcatDomain());//don't delete.
		}

		CommonCode code = commonHandler.getCode(confType);
		if (code != null) {
			conf.setFileTypeCdId(code.getId());
		}

		TomcatConfigFile latestVersion = tomcatConfigFileService.getLatestConfVersion(td, tcinst, conf.getFileTypeCdId());

		if (latestVersion != null) {
			conf.setVersion(latestVersion.getVersion() + 1);
		} else {
			conf.setVersion(1);
		}

		conf.setContent(content);
		if (td != null) {
			conf = tomcatConfigFileService.saveConfigXmlFile(conf, td.getDomainTomcatConfig());
		} else {
			conf = tomcatConfigFileService.saveConfigXmlFile(conf, tcinst.getTomcatDomain().getDomainTomcatConfig());
		}

		// create task
		TaskHistory task = taskService.createConfigXmlUpdateTask(conf.getTomcatDomain().getId(), conf.getFileTypeCdId());

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("configFileId", conf.getId());
		resultMap.put("task", task);

		json.setData(resultMap);

		return json;
	}

	@RequestMapping(value = "{type}/domain/{domainId}", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getConfigVersionList(GridJsonResponse json, @PathVariable int domainId, @PathVariable String type) {
		TomcatDomain td = domainService.getDomain(domainId);

		List<TomcatConfigFile> confVersions = tomcatConfigFileService.getConfigFileVersions(td.getId(), type);
		json.setList(confVersions);
		json.setTotal(confVersions.size());
		json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/{configFileId}", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getConfigFileContents(SimpleJsonResponse json, @PathVariable Integer configFileId) {

		json.setData(tomcatConfigFileService.getConfigFileContents(configFileId));

		return json;
	}

	@RequestMapping("/diff/{firstId}/{secondId}")
	public String diff(Map<String, String> model, @PathVariable Integer firstId, @PathVariable Integer secondId) {

		TomcatConfigFile firstConfig = tomcatConfigFileService.getTomcatConfigFileById(firstId);
		TomcatConfigFile secondConfig = tomcatConfigFileService.getTomcatConfigFileById(secondId);

		String content = "";
		if (firstConfig != null) {

			content = tomcatConfigFileService.getConfigFileContents(firstConfig.getFilePath());
			model.put("firstConfig", content);
			model.put("firstConfigVersion", firstConfig.getVersionAndTimeAndTomcat());
		}
		if (secondConfig != null) {
			content = tomcatConfigFileService.getConfigFileContents(secondConfig.getFilePath());
			model.put("secondConfig", content);
			model.put("secondConfigVersion", secondConfig.getVersionAndTimeAndTomcat());
		}
		return "configdiff";
	}

	@RequestMapping(value = "/{type}/domain/{domainId}/tomcat/{tomcatId}", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getConfigFileVersions(GridJsonResponse json, @PathVariable Integer domainId, @PathVariable Integer tomcatId,
			@PathVariable String type) {
		List<TomcatConfigFile> confVersions = new ArrayList<TomcatConfigFile>();
		TomcatInstance tomcat = null;
		TomcatDomain td = null;
		if (tomcatId > 0) {
			tomcat = tomcatService.findOne(tomcatId);
		}
		if (domainId > 0) {
			td = domainService.getDomain(domainId);
		}
		if (tomcat != null) {
			// list of config files modified in domain level
			confVersions = tomcatConfigFileService.getConfigFileVersions(tomcat.getDomainId(), type);
			List<TomcatConfigFile> modifiedConfigVersions = tomcatConfigFileService.getConfigFileVersions(tomcat, commonHandler.getCode(type).getId());
			if (modifiedConfigVersions != null) {
				confVersions.addAll(modifiedConfigVersions);
			}
		} else if (td != null) {
			confVersions = tomcatConfigFileService.getConfigFileVersions(domainId, type);
		}
		json.setList(confVersions);
		json.setTotal(confVersions.size());
		return json;
	}

	@RequestMapping(value = "/{type}/latest/domain/{domainId}/tomcat/{tomcatId}", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getLatestVersionConfigFile(SimpleJsonResponse json, @PathVariable Integer domainId, @PathVariable Integer tomcatId,
			@PathVariable String type) {
		TomcatConfigFile conf = null;

		int fileTypeCdId = commonHandler.getCode(type).getId();//TODO tran : temporary code. don't do this.

		if (tomcatId > 0) {
			TomcatInstance tc = tomcatService.findOne(tomcatId);
			conf = tomcatConfigFileService.getLatestConfVersion(null, tc, fileTypeCdId);
		} else if (domainId > 0) {
			TomcatDomain td = domainService.getDomain(domainId);
			conf = tomcatConfigFileService.getLatestConfVersion(td, null, fileTypeCdId);
		}

		if (conf != null) {
			String content = tomcatConfigFileService.getConfigFileContents(conf.getId());
			conf.setContent(content);
			json.setData(conf);
		}
		return json;
	}

}
