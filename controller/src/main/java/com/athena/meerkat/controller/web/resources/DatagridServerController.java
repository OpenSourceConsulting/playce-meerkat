package com.athena.meerkat.controller.web.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.resources.services.DataGridServerService;

@Controller
@RequestMapping("/res/datagrid")
public class DatagridServerController {
	@Autowired
	private DataGridServerService service;

	@RequestMapping(value = "/group/list", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse list(SimpleJsonResponse json) {
		// List<DatagridServerGroup> result = service.getAll();
		// json.setData(result);
		// json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getByGroup(SimpleJsonResponse json, int groupId) {
		// DatagridServerGroup group = service.getGroup(groupId);
		// if (group == null) {
		// json.setSuccess(false);
		// json.setMsg("Group does not exist.");
		// } else {
		// json.setData(group.getDatagridServers());
		// json.setSuccess(true);
		// }

		return json;
	}

}
