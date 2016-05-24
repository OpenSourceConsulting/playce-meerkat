package com.athena.meerkat.controller.web.monitoring.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
		//recalculate  
		for (MonData data : monDatas) {

		}
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

	@RequestMapping(value = "/{monType}/{minsAgo}/all", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getServersMonData(GridJsonResponse json, @PathVariable String monType, @PathVariable Integer minsAgo) {
		List<MonDataViewModel> results = new ArrayList<>();
		List<Server> servers = svrService.getList();
		Date now = new Date();
		if (minsAgo <= 0) {
			minsAgo = (int) MeerkatConstants.MONITORING_MINUTE_INTERVAL;
		}
		Date time = new Date(now.getTime() - minsAgo * MeerkatConstants.ONE_MINUTE_IN_MILLIS);
		String[] types = new String[1];
		types[0] = monType;
		List<MonData> datas = service.getAllMonDataList(types, time, now);

		Map previousValues = new HashMap<>();
		for (MonData data : datas) {
			MonDataViewModel viewmodel = new MonDataViewModel();
			viewmodel.setMonDt(data.getMonDt());
			Map<String, Double> value = new HashMap<>();
			for (Server server : servers) {
				if (data.getServerId() == server.getId()) {
					value.put(server.getName(), data.getMonValue());
				} else {
					value.put(server.getName(), -1D);
				}
			}
			viewmodel.setValue(value);
			results.add(viewmodel);
		}
		results = this.updateData(results, servers);
		json.setList(results);
		json.setTotal(results.size());
		return json;
	}

	@RequestMapping(value = "/{monType}/{serverId}/{minsAgo}", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getDetailServerMonData(GridJsonResponse json, @PathVariable String monType, @PathVariable Integer serverId,
			@PathVariable Integer minsAgo) {
		Date now = new Date();
		if (minsAgo <= 0) {
			minsAgo = (int) MeerkatConstants.MONITORING_MINUTE_INTERVAL;
		}
		Date time = new Date(now.getTime() - minsAgo * MeerkatConstants.ONE_MINUTE_IN_MILLIS);
		String[] types = new String[1];
		types[0] = monType;
		List<MonDataViewModel> results = new ArrayList<>();
		Server s = svrService.getServer(serverId);
		if (s != null) {
			List<MonDataViewModel> datas = service.getMonDataList(types, serverId, time, now);
			for (MonDataViewModel data : datas) {
				MonDataViewModel viewmodel = new MonDataViewModel();
				viewmodel.setMonDt(data.getMonDt());
				Map<String, Double> value = new HashMap<>();
				value.put(s.getName(), data.getValue().get(types[0]));
				viewmodel.setValue(value);
				results.add(viewmodel);
			}
		}
		json.setList(results);
		json.setTotal(results.size());
		return json;
	}

	@RequestMapping(value = "/diskmon", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getMonDiskData(GridJsonResponse jsonRes, Integer serverId) {

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

	private List<MonDataViewModel> updateData(List<MonDataViewModel> origin, List<Server> servers) {
		for (int i = 0; i < origin.size(); i++) {
			MonDataViewModel result = origin.get(i);
			Map<String, Double> value = result.getValue();
			for (Server server : servers) {
				if (Double.parseDouble(value.get(server.getName()).toString()) == -1D) {
					Double nextValue = this.getNextValue(i, server.getName(), origin);
					Double prevValue = this.getPreviousValue(i, server.getName(), origin);
					if (i == 0) {
						value.put(server.getName(), nextValue);
					} else {
						Double avgValue = 0D;
						if (nextValue != -1D && prevValue != -1D) {
							avgValue = (nextValue + prevValue) / 2;
						} else {
							if (nextValue == -1D) {
								avgValue = prevValue;
							} else {
								avgValue = nextValue;
							}
						}
						value.put(server.getName(), avgValue);
					}
					if (i == origin.size() - 1) {
						value.put(server.getName(), prevValue);
					}

				}
			}
			result.setValue(value);
		}

		return origin;
	}

	private Double getNextValue(int index, String key, List<MonDataViewModel> data) {
		int i = index;
		while (i < data.size()) {
			Double value = data.get(i).value.get(key);
			if (value != -1) {
				return value;
			}
			i++;
		}
		return -1D;
	}

	private Double getPreviousValue(int index, String key, List<MonDataViewModel> data) {
		int i = index;
		while (i >= 0) {
			Double value = data.get(i).value.get(key);
			if (value != -1) {
				return value;
			}
			i--;
		}
		return -1D;
	}
}
// end of MonDataController.java
