package com.athena.meerkat.controller.web.tomcat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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
import com.athena.meerkat.controller.web.common.util.WebUtil;
import com.athena.meerkat.controller.web.entities.CommonCode;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
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

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse saveConfigFile(
			SimpleJsonResponse json, String content, String confType,
			String objType, int id, int objId) {
		TomcatDomain td = null;
		TomcatInstance tcinst = null;
		TomcatConfigFile conf = new TomcatConfigFile();
		if (objType.equals(MeerkatConstants.CONFIG_FILE_OBJ_TYPE_DOMAIN)) {
			td = domainService.getDomain(objId);
			conf.setTomcatDomain(td);
		} else if (objType.equals(MeerkatConstants.CONFIG_FILE_OBJ_TYPE_TOMCAT)) {
			tcinst = tomcatService.findOne(objId);
			conf.setTomcatInstance(tcinst);
		}
		// TomcatConfigFile dbConf =
		// TomcatConfigFile.getTomcatConfigFileById(id);

		conf.setCreatedTime(new Date());
		conf.setCreateUserId(WebUtil.getLoginUserId());
		CommonCode code = commonHandler.getCode(confType);
		if (code != null) {
			conf.setFileTypeCdId(code.getId());
		}

		TomcatConfigFile latestVersion = tomcatConfigFileService
				.getLatestConfVersion(td, tcinst, confType);
		if (latestVersion != null) {
			conf.setVersion(latestVersion.getVersion() + 1);
		} else {
			conf.setVersion(1);
		}
		// TODO idkbj: save by provisioning .... and get path
		String path = conf.getVersion() + "/" + confType;
		conf.setFilePath(path);
		conf = tomcatConfigFileService.saveConfigFile(conf);
		json.setData(conf.getId());
		return json;
	}

	@RequestMapping(value = "{type}/domain/{domainId}", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getConfigVersionList(
			GridJsonResponse json, @PathVariable int domainId,
			@PathVariable String type) {
		TomcatDomain td = domainService.getDomain(domainId);

		List<TomcatConfigFile> confVersions = tomcatConfigFileService
				.getConfigFileVersions(td.getId(), type);
		json.setList(confVersions);
		json.setTotal(confVersions.size());
		json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/{configFileId}", method = RequestMethod.GET)
	public @ResponseBody SimpleJsonResponse getConfigVersionList(
			SimpleJsonResponse json, @PathVariable Integer configFileId) {
		TomcatConfigFile file = tomcatConfigFileService
				.getTomcatConfigFileById(configFileId);
		if (file != null) {
			// TODO: idkbj load content by provisioning
			String content = "loading ....." + configFileId.toString();
			json.setData(content);
		}
		return json;
	}

	@RequestMapping("/diff/{firstId}/{secondId}")
	public String diff(Map<String, String> model,
			@PathVariable Integer firstId, @PathVariable Integer secondId) {
		TomcatConfigFile firstConfig = tomcatConfigFileService
				.getTomcatConfigFileById(firstId);
		TomcatConfigFile secondConfig = tomcatConfigFileService
				.getTomcatConfigFileById(secondId);
		String content = "";
		if (firstConfig != null) {
			// TODO idkbj get content of config file and put to model
			content = "18:55:51.351 [http-nio-8080-exec-7] DEBUG o.s.s.w.c.SecurityContextPersistenceFilter - SecurityContextHolder now cleared, as request processing completed";
			model.put("firstConfig", content);
			model.put("firstConfigVersion",
					firstConfig.getVersionAndTimeAndTomcat());
		}
		if (secondConfig != null) {
			// TODO idkbj get content of config file and put to model
			content = "18:55:51.351 [http-nio-8080-exec-7] DEBUG o.s.s.w.c.SecurityC3213123ntextPersistenceFilter - SecurityContextHolder now cleared, as request processing completed";
			model.put("secondConfig", content);
			model.put("secondConfigVersion",
					secondConfig.getVersionAndTimeAndTomcat());
		}
		return "configdiff";
	}

	@RequestMapping(value = "/{type}/domain/{domainId}/tomcat/{tomcatId}", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getConfigFileVersions(GridJsonResponse json,
			@PathVariable Integer domainId, @PathVariable Integer tomcatId,
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
			confVersions = tomcatConfigFileService.getConfigFileVersions(
					tomcat.getDomainId(), type);
			List<TomcatConfigFile> modifiedConfigVersions = tomcatConfigFileService
					.getConfigFileVersions(tomcat, type);
			if (modifiedConfigVersions != null) {
				confVersions.addAll(modifiedConfigVersions);
			}
		} else if (td != null) {
			confVersions = tomcatConfigFileService.getConfigFileVersions(
					domainId, type);
		}
		json.setList(confVersions);
		json.setTotal(confVersions.size());
		return json;
	}

	@RequestMapping(value = "/{type}/latest/domain/{domainId}/tomcat/{tomcatId}", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getLatestVersionConfigFile(
			SimpleJsonResponse json, @PathVariable Integer domainId,
			@PathVariable Integer tomcatId, @PathVariable String type) {
		TomcatConfigFile conf = null;
		if (tomcatId > 0) {
			TomcatInstance tc = tomcatService.findOne(tomcatId);
			conf = tomcatConfigFileService.getLatestConfVersion(null, tc, type);
		} else if (domainId > 0) {
			TomcatDomain td = domainService.getDomain(domainId);
			conf = tomcatConfigFileService.getLatestConfVersion(td, null, type);
		}

		if (conf != null) {
			// TODO idkbj get content of config file by provisioning
			String content = "This is example content for config ID:"
					+ conf.getId();
			conf.setContent(content);
			json.setData(conf);
		}
		return json;
	}
	// @RequestMapping(value = "{type}/{version}/domain/{domainId}", method =
	// RequestMethod.GET)
	// public @ResponseBody SimpleJsonResponse getConfigVersionList(
	// SimpleJsonResponse json, @PathVariable Integer domainId,
	// @PathVariable String type, @PathVariable Integer version) {
	// TomcatDomain td = domainService.getDomain(domainId);
	// String content = "";
	// if (td == null) {
	// json.setSuccess(false);
	// json.setMsg("Tomcat domain does not exist.");
	// } else {
	// TomcatConfigFile confFile = tomcatConfigFileService.getConfig(td,
	// type, version);
	// if (confFile != null) {
	// // TODO idkbj: load by ssh ....
	// content = "load by ssh ...." + version;
	// }
	// json.setData(content);
	// json.setSuccess(true);
	// }
	// return json;
	// }

}
