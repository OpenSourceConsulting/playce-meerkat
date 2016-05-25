package com.athena.meerkat.controller.web;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.resources.services.DataSourceService;
import com.athena.meerkat.controller.web.resources.services.ServerService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;

@Controller
@RequestMapping("dashboard")
public class DashboardController {

	private static final Logger logger = LoggerFactory.getLogger(ConsoleController.class);

	@Autowired
	private TomcatDomainService domainService;
	@Autowired
	private TomcatInstanceService tomcatService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private DataSourceService dsService;

	@RequestMapping(value = "/get/stats", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getStats(SimpleJsonResponse json) {
		long domainCount = domainService.getDomainNo();
		long tomcatInstCount = tomcatService.getTomcatInstNo();
		long serverCount = serverService.getServerNo();
		long dsCount = dsService.getDatasourceNo();

		HashMap<String, Long> value = new HashMap<>();
		value.put("domainCount", domainCount);
		value.put("tomcatInstCount", tomcatInstCount);
		value.put("serverCount", serverCount);
		value.put("dsCount", dsCount);
		json.setData(value);

		return json;
	}

	@RequestMapping(value = "/get/domain/stats", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getDomainStats(GridJsonResponse json) {
		List<TomcatDomain> domains = domainService.getAll();
		//List<TomcatDomain> returnedList = new ArrayList<>();
		//		for (TomcatDomain domain : domains) {
		//			TomcatDomain d = new TomcatDomain();
		//			d.setName(domain.getName());
		//			d.setTomcatInstance
		//			returnedList.add(d);
		//		}
		json.setList(domains);

		return json;
	}
}
