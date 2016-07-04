package com.athena.meerkat.controller.web.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.TaskHistoryDetail;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.monitoring.jmx.MonJmxService;
import com.athena.meerkat.controller.web.monitoring.stat.MonUtilStat;
import com.athena.meerkat.controller.web.monitoring.stat.MonUtilStatService;
import com.athena.meerkat.controller.web.resources.services.DataSourceService;
import com.athena.meerkat.controller.web.resources.services.ServerService;
import com.athena.meerkat.controller.web.tomcat.services.TaskHistoryService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;
import com.athena.meerkat.controller.web.tomcat.viewmodels.TaskDetailViewModel;
import com.athena.meerkat.controller.web.user.services.UserService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	private TomcatDomainService domainService;
	@Autowired
	private TomcatInstanceService tomcatService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private DataSourceService dsService;
	@Autowired
	private MonJmxService monJmxService;
	@Autowired
	private TaskHistoryService taskService;
	@Autowired
	private UserService userService;
	@Autowired
	private MonUtilStatService monUtilStatService;

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
		json.setList(domains);

		return json;
	}

	@RequestMapping(value = "/get/jdbc/stats/{minsAgo}", method = RequestMethod.GET)
	@ResponseBody
	public List getJDBCStats(GridJsonResponse json, @PathVariable Integer minsAgo) {
		Date now = new Date();
		if (minsAgo <= 0) {
			minsAgo = (int) MeerkatConstants.MONITORING_MINUTE_INTERVAL;
		}
		Date time = new Date(now.getTime() - minsAgo * MeerkatConstants.ONE_MINUTE_IN_MILLIS);
		List<HashMap<String, Object>> list = new ArrayList<>();
		List<DataSource> dss = dsService.getAll();
		for (DataSource ds : dss) {
			HashMap<String, Object> value = new HashMap<>();
			Long connectionCount = monJmxService.getJDBCConnectionCount(ds.getName(), time, now);
			value.put("dsName", ds.getName());
			value.put("monValue", connectionCount == null ? 0 : connectionCount);
			list.add(value);
		}
		//json.setList(list);
		return list;
		//return json;
	}

	@RequestMapping(value = "/get/alerts", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getAlertList(GridJsonResponse json) {
		List<MonUtilStat> alerts = monUtilStatService.getAlerts(MeerkatConstants.DASHBOARD_ALERT_COUNT);
		json.setList(alerts);
		json.setTotal(alerts.size());
		return json;
	}

	@RequestMapping(value = "/get/logs", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getTaskLogs(GridJsonResponse json) {
		List<TaskHistoryDetail> logs = taskService.getTaskHistories(MeerkatConstants.DASHBOARD_LOGS_COUNT);
		List<TaskDetailViewModel> viewmodels = new ArrayList<>();
		for (TaskHistoryDetail detail : logs) {
			TaskDetailViewModel viewmodel = new TaskDetailViewModel(detail);
			viewmodel.setUsername(userService.findUser(viewmodel.getUserId()).getUsername());
			viewmodel.setTargetName(detail.getDomainName() + " > " + detail.getTomcatInstanceName());
			viewmodels.add(viewmodel);
		}
		json.setList(viewmodels);
		json.setTotal(logs.size());
		return json;
	}
}
