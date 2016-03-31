package com.athena.meerkat.controller.web.tomcat;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.common.State;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.services.ApplicationService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;

@Controller
@RequestMapping("application")
public class ApplicationController {

	@Autowired
	private ApplicationService appService;
	@Autowired
	private TomcatDomainService domainService;

	@RequestMapping(value = "/deploy", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse deploy(SimpleJsonResponse json,
			@RequestParam String contextPathTextField,
			@RequestParam String installationRemotePathTextField,
			@RequestParam Integer domainIdHiddenField,
			@RequestParam MultipartFile warLocalPathFileField) {
		TomcatDomain domain = domainService.getDomain(domainIdHiddenField);
		TomcatApplication app = new TomcatApplication(contextPathTextField,
				installationRemotePathTextField, "");
		app.setState(State.APP_STATE_STOPPED);
		app.setLastModifiedTime(new Date());
		app.setDeployedDate(new Date());
		app.setTomcatDomain(domain);
		// TODO: idkjwon provisioning deploy application
		app.setVersion("1.233"); // get by war fileF

		// provisioning before save
		appService.save(app);
		return json;
	}

	@RequestMapping("/undeploy")
	public @ResponseBody SimpleJsonResponse undeploy(SimpleJsonResponse json,
			int Id) {
		TomcatApplication app = appService.getApplication(Id);

		if (app == null) {
			json.setSuccess(false);
			json.setMsg("Application does not exist.");

		} else {
			appService.delete(app);

		}
		return json;
	}

	@RequestMapping("/start")
	public @ResponseBody SimpleJsonResponse start(SimpleJsonResponse json,
			int id) {
		TomcatApplication app = appService.getApplication(id);
		if (app == null) {
			json.setSuccess(false);
			json.setMsg("Application does not exist.");
			return json;
		}

		if (app.getState() == State.APP_STATE_STARTED) {
			json.setSuccess(false);
			json.setMsg("Application has been already started.");
			return json;
		}

		if (appService.start(app)) {
			json.setSuccess(true);
			json.setData(State.APP_STATE_STARTED);
		}
		return json;
	}

	@RequestMapping("/stop")
	public @ResponseBody SimpleJsonResponse stop(SimpleJsonResponse json, int id) {
		TomcatApplication app = appService.getApplication(id);
		if (app == null) {
			json.setSuccess(false);
			json.setMsg("Application does not exist.");
			return json;
		}

		if (app.getState() == State.APP_STATE_STOPPED) {
			json.setSuccess(false);
			json.setMsg("Application has been already stopped.");
			return json;
		}

		if (appService.stop(app)) {
			json.setSuccess(true);
			json.setData(State.APP_STATE_STOPPED);
		}
		return json;
	}

	@RequestMapping("/restart")
	public @ResponseBody SimpleJsonResponse restart(SimpleJsonResponse json,
			int id) {
		TomcatApplication app = appService.getApplication(id);
		if (app == null) {
			json.setSuccess(false);
			json.setMsg("Application does not exist.");
			return json;
		}

		if (app.getState() == State.APP_STATE_STOPPED) {
			json.setSuccess(false);
			json.setMsg("Application has been already stopped.");
			return json;
		} else {
			if (appService.stop(app)) {
				if (appService.start(app)) {
					json.setSuccess(true);
					json.setData(State.APP_STATE_STARTED);
				}
			}
		}

		return json;
	}

	@RequestMapping("/list")
	public @ResponseBody SimpleJsonResponse getAppListByDomain(
			SimpleJsonResponse json, int domainId) {
		TomcatDomain domain = domainService.getDomain(domainId);
		if (domain == null) {
			json.setSuccess(false);
			json.setMsg("Domain does not exist");
		}
		// List<TomcatInstance> tomcats = domain.getTomcats();
		// if (tomcats.size() > 0) {
		// json.setSuccess(true);
		// // json.setData(tomcats.get(0).getApplications());
		// }
		return json;
	}
}
