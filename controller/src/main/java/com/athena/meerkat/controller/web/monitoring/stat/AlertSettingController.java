package com.athena.meerkat.controller.web.monitoring.stat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.CommonCode;
import com.athena.meerkat.controller.web.entities.MonAlertConfig;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.resources.services.ServerService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;

@Controller
@RequestMapping("/monitor/alert")
public class AlertSettingController {

	@Autowired
	private AlertSettingService alertService;
	@Autowired
	private TomcatDomainService domainService;
	@Autowired
	private CommonCodeHandler commonHandler;
	@Autowired
	private ServerService serverService;

	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getAlert(SimpleJsonResponse json, Integer alertId) {
		MonAlertConfig alert = alertService.getAlertConfig(alertId);
		json.setData(alert);
		return json;
	}

	@RequestMapping(value = "/setting/changeStatus", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse changeStatus(SimpleJsonResponse json, Integer alertId, boolean status) {
		MonAlertConfig alert = alertService.getAlertConfig(alertId);
		if (alert != null) {
			alert.setStatus(status);

			alert = alertService.saveAlertSetting(alert);
			json.setData(alert.isStatus());
		}
		return json;
	}

	@RequestMapping(value = "/setting/changeAllStatus", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse changeAllStatus(SimpleJsonResponse json, String objType, Integer objId, boolean status) {
		List<MonAlertConfig> alertSettings = new ArrayList<>();
		if (objType.equals(MeerkatConstants.OBJ_TYPE_DOMAIN)) {
			TomcatDomain td = domainService.getDomain(objId);
			if (td != null) {
				alertSettings = td.getMonAlertConfigs();
			}
		} else {
			Server server = serverService.getServer(objId);
			if (server != null) {
				alertSettings = server.getMonAlertConfigs();
			}
		}

		for (MonAlertConfig alert : alertSettings) {
			alert.setStatus(status);
		}

		alertService.saveAllAlertSettings(alertSettings);

		return json;
	}

	@RequestMapping(value = "/setting/operator/list")
	@ResponseBody
	public GridJsonResponse getAllAlertOperators(GridJsonResponse json) {

		List<CommonCode> list = commonHandler.getCodes(MeerkatConstants.CODE_GROP_ALERT_THRESHOLD_OPERATOR);
		json.setList(list);
		json.setTotal(list.size());
		return json;
	}

	@RequestMapping(value = "/setting/save", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse saveAlertSetting(SimpleJsonResponse json, MonAlertConfig alert) {
		MonAlertConfig dbAlertConf = alertService.getAlertConfig(alert.getId());
		if (alert.getThresholdValue() < 0 || alert.getThresholdValue() > 100) {
			json.setMsg("Threshold value should be 0 to 100");
			json.setSuccess(false);
			return json;
		}
		if (dbAlertConf != null) {
			dbAlertConf.setAlertItemCdId(alert.getAlertItemCdId());
			dbAlertConf.setThresholdOpCdId(alert.getThresholdOpCdId());
			dbAlertConf.setThresholdValue(alert.getThresholdValue());
			dbAlertConf.setStatus(false);
		}

		alertService.saveAlertSetting(dbAlertConf);
		return json;
	}
}
