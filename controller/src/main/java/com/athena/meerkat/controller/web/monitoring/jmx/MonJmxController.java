package com.athena.meerkat.controller.web.monitoring.jmx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.monitoring.server.MonDataController;
import com.athena.meerkat.controller.web.monitoring.stat.MonStatisticsAnalyzer;
import com.athena.meerkat.controller.web.resources.services.DataSourceService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/monitor/jmx")
public class MonJmxController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonJmxController.class);
	
	private static final String MDC_TI_KEY = "tInstanceId";

	@Autowired
	private MonJmxService service;

	@Autowired
	private DataSourceService dsService;
	
	@Autowired
	private MonStatisticsAnalyzer monAnalyzer;
	
	@Autowired
	private TomcatInstanceService tiService;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public MonJmxController() {
	}

	@MessageMapping("/monitor/jmx/create")
	@SendToUser(MonDataController.STOMP_USER_DEST)
	public SimpleJsonResponse create(List<Map> datas) {

		SimpleJsonResponse jsonRes = new SimpleJsonResponse();

		List<MonJmx> monJmxs = copyProperties(datas);
		
		boolean mdcEnable = monJmxs != null && monJmxs.size() > 0;
		if (mdcEnable) {
			TomcatInstance ti = tiService.findOne(monJmxs.get(0).getInstanceId());
			
			if (ti != null) {
				mdcEnable = true;
				MDC.put(MonDataController.MDC_SERVER_KEY, ti.getIpaddress());
				MDC.put(MDC_TI_KEY, String.valueOf(ti.getId()));
			}
		}
		
		try {

			service.insertMonJmxs(monJmxs);
			service.saveInstanceState(monJmxs);
	
			monAnalyzer.analyze(monJmxs);
		} finally {
			if (mdcEnable) {
				MDC.remove(MonDataController.MDC_SERVER_KEY);
				MDC.remove(MDC_TI_KEY);
			}
		}
		
		

		return jsonRes;
	}

	@RequestMapping("/tomcat/{tinstId}/{type}/{minutesAgo}")
	@ResponseBody
	public GridJsonResponse getTomcatJMXData(GridJsonResponse json, @PathVariable Integer tinstId, @PathVariable String type, @PathVariable Integer minutesAgo,
			@RequestParam(required = false) Integer dsId) throws Exception {
		Date now = new Date();
		if (minutesAgo <= 0) {
			minutesAgo = (int) MeerkatConstants.MONITORING_MINUTE_INTERVAL;
		}
		Date time = new Date(now.getTime() - minutesAgo * MeerkatConstants.ONE_MINUTE_IN_MILLIS);
		String[] types = new String[1];
		List<MonJmx> list = new ArrayList<>();
		if (type.contains("memory")) {
			types[0] = MeerkatConstants.MON_JMX_FACTOR_HEAP_MEMORY;
		} else if (type.contains("cpu")) {
			types[0] = MeerkatConstants.MON_JMX_FACTOR_CPU_USAGE;
		} else if (type.contains("threads")) {
			types[0] = MeerkatConstants.MON_JMX_FACTOR_ACTIVE_THREADS;
		} else if (type.contains("jdbc")) {
			DataSource ds = dsService.findOne(dsId);
			if (ds != null) {
				types[0] = MeerkatConstants.MON_JMX_FACTOR_JDBC_CONNECTIONS + "." + ds.getName();
			} else {
				json.setMsg("Datasource does not exist");
				json.setSuccess(false);
				return json;
			}

		}
		list = service.getJmxMonDataList(types, tinstId, time, now);

		json.setList(list);

		return json;
	}

	@RequestMapping("/tomcat/all/{type}/{minutesAgo}")
	@ResponseBody
	public GridJsonResponse getAllTomcatJMXData(GridJsonResponse json, @PathVariable String type, @PathVariable Integer minutesAgo) {
		Date now = new Date();
		if (minutesAgo <= 0) {
			minutesAgo = (int) MeerkatConstants.MONITORING_MINUTE_INTERVAL;
		}
		Date time = new Date(now.getTime() - minutesAgo * MeerkatConstants.ONE_MINUTE_IN_MILLIS);
		String[] types = new String[1];
		List<MonJmx> list = new ArrayList<>();
		if (type.contains("jdbc")) {
			types[0] = MeerkatConstants.MON_JMX_FACTOR_JDBC_CONNECTIONS + ".";
		}
		if (type.contains("jdbc")) {
			list = service.getJmxJDBCConnectionList(time, now);
		} else {
			list = service.getJmxMonDataList(types, time, now);
		}
		json.setList(list);
		return json;
	}

	private List<MonJmx> copyProperties(List<Map> maps) {

		List<MonJmx> messages = new ArrayList<MonJmx>();

		try {
			for (Map map : maps) {

				MonJmx msg = new MonJmx();

				PropertyUtils.copyProperties(msg, map);

				messages.add(msg);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return messages;
	}
}
//end of MonJmxController.java