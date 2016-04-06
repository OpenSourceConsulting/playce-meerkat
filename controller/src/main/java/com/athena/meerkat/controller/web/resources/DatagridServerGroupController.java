package com.athena.meerkat.controller.web.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.entities.DatagridServerGroup;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.resources.services.DataGridServerGroupService;
import com.athena.meerkat.controller.web.resources.services.ServerService;

@Controller
@RequestMapping("/res/datagrid")
public class DatagridServerGroupController {
	@Autowired
	private DataGridServerGroupService service;
	@Autowired
	private ServerService serverService;

	@RequestMapping(value = "/group/list", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse list(GridJsonResponse json) {
		List<DatagridServerGroup> result = service.getAll();
		json.setList(result);
		json.setTotal(result.size());
		return json;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getByGroup(GridJsonResponse json, Integer groupId) {
		DatagridServerGroup group = service.getGroup(groupId);
		if (group != null) {
			List<Server> list = serverService.getListByGroupId(groupId);
			json.setList(list);
			json.setTotal(list.size());
		}
		return json;
	}
}
