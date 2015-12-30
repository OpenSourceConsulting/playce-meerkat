package com.athena.dolly.controller.web.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.dolly.controller.ServiceResult;
import com.athena.dolly.controller.ServiceResult.Status;
import com.athena.dolly.controller.web.application.Application;
import com.athena.dolly.controller.web.datagridserver.DataGridServerService;
import com.athena.dolly.controller.web.datagridserver.DatagridServerGroup;
import com.athena.dolly.controller.web.tomcat.instance.TomcatInstance;
import com.athena.dolly.controller.web.tomcat.instance.TomcatInstanceService;

@Controller
@RequestMapping("/domain")
public class DomainController {

	@Autowired
	private DomainService domainService;
	@Autowired
	private DataGridServerService datagridService;
	@Autowired
	private TomcatInstanceService tomcatService;

	@RequestMapping("/save")
	public @ResponseBody
	boolean save(int id, String name, boolean isClustering,
			int datagridServerGroupId) {
		Domain domain = domainService.getDomainByName(name);
		if (domain != null) {
			if (domain.getId() != id) {// domain exist but not in edit
										// case.
				return false;
			}
		}
		if (id > 0) { // edit
			domain = domainService.getDomain(id);
			if (domain == null) {
				return false;
			}
			domain.setName(name);
			domain.setClustering(isClustering);
		} else {
			domain = new Domain(name, isClustering);
		}

		DatagridServerGroup group = datagridService
				.getGroup(datagridServerGroupId);
		if (group == null) {
			return false;
		}
		domain.setServerGroup(group);
		domainService.save(domain);
		// update on server group
		group.setDomain(domainService.getDomainByName(name));
		datagridService.saveGroup(group);
		return true;
	}

	@RequestMapping("/edit")
	public @ResponseBody
	Domain edit(int id) {
		return domainService.getDomain(id);
	}

	@RequestMapping("/list")
	public @ResponseBody
	List<Domain> getDomainList() {
		ServiceResult result = domainService.getAll();
		if (result.getStatus() == Status.DONE) {
			List<Domain> domains = (List<Domain>) result.getReturnedVal();
			return domains;
		}
		return null;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody
	Domain getDomain(int id) {
		Domain result = domainService.getDomain(id);
		return result;
	}

	@RequestMapping("/tomcatlist")
	public @ResponseBody
	List<TomcatInstance> getTomcatInstanceByDomain(int domainId) {
		// ServiceResult result =
		// tomcatService.getTomcatListByDomainId(domainId);
		// if (result.getStatus() == Status.DONE) {
		// List<TomcatInstance> tomcats = (List<TomcatInstance>) result
		// .getReturnedVal();
		// return tomcats;
		// }
		// return null;
		return tomcatService.getTomcatListByDomainId(domainId);
	}

	@RequestMapping("/applications")
	public @ResponseBody
	List<Application> getApplicationsByDomain(int domainId) {
		ServiceResult result = domainService
				.getApplicationListByDomain(domainId);
		if (result.getStatus() == Status.DONE) {
			List<Application> apps = (List<Application>) result
					.getReturnedVal();
			return apps;
		}
		return null;
	}

	@RequestMapping("/delete")
	public @ResponseBody
	boolean delete(int domainId) {
		return domainService.delete(domainId);
	}
}
