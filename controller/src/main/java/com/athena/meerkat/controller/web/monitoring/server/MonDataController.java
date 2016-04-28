package com.athena.meerkat.controller.web.monitoring.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.resources.services.ServerService;
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
@RequestMapping("/monitor/server")
public class MonDataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonDataController.class);

	private static final String MSG_MON = "mon";
	private static final String MSG_FS = "fs";

	@Autowired
	private MonDataService service;

	@Autowired
	private ServerService svrService;

	@Autowired
	private TomcatInstanceService tiService;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public MonDataController() {

	}

	/**
	 * <pre>
	 * update server monitoring data (osName, osVersion....)
	 * </pre>
	 * 
	 * @param jsonRes
	 * @param machine
	 * @return
	 */
	@MessageMapping("/monitor/init")
	@SendToUser("/queue/agents")
	public SimpleJsonResponse init(SimpleJsonResponse jsonRes, Map<String, Object> initMon) throws Exception {

		int serverId = Integer.valueOf(initMon.get("id").toString());
		Server dbServer = svrService.getServer(serverId);

		PropertyUtils.copyProperties(dbServer, initMon);

		svrService.save(dbServer);

		LOGGER.debug("init saved. ---------------- {}", serverId);

		jsonRes.setData(tiService.findInstanceConfigs(serverId));

		return jsonRes;
	}

	@MessageMapping("/monitor/create")
	@SendToUser("/queue/agents")
	public SimpleJsonResponse saveMon(List<Map> datas) {

		SimpleJsonResponse jsonRes = new SimpleJsonResponse(MSG_MON);

		List<MonData> monDatas = copyProperties(datas);

		service.insertMonDatas(monDatas);

		LOGGER.debug("saved. ----------------");

		return jsonRes;
	}

	@MessageMapping("/monitor/fs")
	@SendToUser("/queue/agents")
	public SimpleJsonResponse saveFs(List<Map> datas) {

		SimpleJsonResponse jsonRes = new SimpleJsonResponse(MSG_FS);

		List<MonFs> monFsList = copyFSProperties(datas);

		service.saveMonFsList(monFsList);

		LOGGER.debug("saved fs. ----------------");

		return jsonRes;
	}

	@RequestMapping(value = "/cpumon", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getCPUMonData(GridJsonResponse jsonRes, Integer serverId) {
		Date now = new Date();
		Date twoMinsAgo = new Date(now.getTime() - 2 * MeerkatConstants.ONE_MINUTE_IN_MILLIS);
		String[] types = new String[1];
		types[0] = MeerkatConstants.MON_FACTOR_CPU_USED;
		List<MonDataViewModel> results = service.getMonDataList(types, serverId, twoMinsAgo, now);

		jsonRes.setList(results);
		jsonRes.setTotal(results.size());
		return jsonRes;
	}

	@RequestMapping(value = "/memorymon", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getMemoryMonData(GridJsonResponse jsonRes, Integer serverId) {

		Date now = new Date();
		Date twoMinsAgo = new Date(now.getTime() - 2 * MeerkatConstants.ONE_MINUTE_IN_MILLIS);
		String[] types = new String[2];
		types[0] = MeerkatConstants.MON_FACTOR_MEM_USED;
		types[1] = MeerkatConstants.MON_FACTOR_MEM_USED_PER;
		List<MonDataViewModel> results = service.getMonDataList(types, serverId, twoMinsAgo, now);

		jsonRes.setList(results);
		jsonRes.setTotal(results.size());
		return jsonRes;
	}

	@RequestMapping(value = "/nimon/{type}", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getNetworkTrafficMonitoringData(GridJsonResponse jsonRes, @PathVariable(value = "type") String trafficType, Integer serverId) {

		Date now = new Date();
		Date time = new Date(now.getTime() - MeerkatConstants.MONITORING_MINUTE_INTERVAL * MeerkatConstants.ONE_MINUTE_IN_MILLIS);
		String[] types = new String[1];
		if (trafficType.contains(MeerkatConstants.MON_NI_TYPE_IN)) {
			types[0] = MeerkatConstants.MON_FACTOR_NI_IN;
		} else if (trafficType.contains(MeerkatConstants.MON_NI_TYPE_OUT)) {
			types[0] = MeerkatConstants.MON_FACTOR_NI_OUT;
		}
		List<MonDataViewModel> results = service.getMonDataList(types, serverId, time, now);
		jsonRes.setList(results);
		jsonRes.setTotal(results.size());
		return jsonRes;
	}

	@RequestMapping(value = "/diskmon", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getMonDiskData(GridJsonResponse jsonRes, Integer serverId) {

		Date now = new Date();
		Date tenMinsAgo = new Date(now.getTime() - MeerkatConstants.DISK_MON_PERIOD_MINUTE * MeerkatConstants.ONE_MINUTE_IN_MILLIS);

		List<MonFs> results = service.getDiskMonDataList(serverId);

		jsonRes.setList(results);
		jsonRes.setTotal(results.size());
		return jsonRes;
	}

	private List<MonData> copyProperties(List<Map> maps) {

		List<MonData> messages = new ArrayList<MonData>();

		try {
			for (Map map : maps) {

				MonData msg = new MonData();

				PropertyUtils.copyProperties(msg, map);

				messages.add(msg);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return messages;
	}

	private List<MonFs> copyFSProperties(List<Map> maps) {

		List<MonFs> messages = new ArrayList<MonFs>();

		try {
			for (Map map : maps) {

				MonFs msg = new MonFs();

				PropertyUtils.copyProperties(msg, map);

				messages.add(msg);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return messages;
	}
}
// end of MonDataController.java