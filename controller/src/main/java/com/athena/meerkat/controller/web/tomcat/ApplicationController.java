package com.athena.meerkat.controller.web.tomcat;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@RequestMapping("/deploy")
	public @ResponseBody SimpleJsonResponse deploy(SimpleJsonResponse json,
			TomcatApplication app, int domainId) {
		TomcatDomain domain = domainService.getDomain(domainId);
		if (domain == null) {
			json.setMsg("Domain does not exist.");
			json.setSuccess(false);
			return json;
		}
		app.setState(State.APP_STATE_STOPPED);
		// app.setLastModifiedDate(new Date());
		//app.setDeployedDate(new Date());
		appService.deploy(app, domain);
		return json;
	}

	@RequestMapping("/undeploy")
	public @ResponseBody SimpleJsonResponse undeploy(SimpleJsonResponse json,
			int Id, int domainId) {
		TomcatApplication app = appService.getApplication(Id);
		TomcatDomain domain = domainService.getDomain(domainId);
		if (domain == null) {
			json.setMsg("Domain does not exist.");
			json.setSuccess(false);
			return json;
		}
		if (app == null) {
			json.setSuccess(false);
			json.setMsg("Application does not exist.");
			return json;
		} else {
			if (appService.undeploy(app, domain)) {
				json.setSuccess(true);
				return json;
			}
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
//		List<TomcatInstance> tomcats = domain.getTomcats();
//		if (tomcats.size() > 0) {
//			json.setSuccess(true);
//			// json.setData(tomcats.get(0).getApplications());
//		}
		return json;
	}
}
