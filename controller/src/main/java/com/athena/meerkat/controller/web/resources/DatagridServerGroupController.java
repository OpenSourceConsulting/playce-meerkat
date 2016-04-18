package com.athena.meerkat.controller.web.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.CommonCode;
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

	@RequestMapping(value = "/list/selected", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getSelectedServersByGroup(GridJsonResponse json,
			Integer groupId) {
		DatagridServerGroup group = service.getGroup(groupId);
		if (group != null) {
			List<Server> allServers = serverService.getList();
			List<Server> list = serverService.getListByGroupId(groupId);
			for (Server s : allServers) {
				if (list.contains(s)) {
					s.setSelected(true);
				}
			}
			json.setList(allServers);
			json.setTotal(allServers.size());
		}
		return json;
	}

	@RequestMapping(value = "/group/get", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getServerGroup(SimpleJsonResponse json, Integer id) {
		DatagridServerGroup group = service.getGroup(id);
		json.setData(group);
		return json;
	}

	@RequestMapping(value = "/group/types", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getServerGroupTypes(GridJsonResponse json) {
		List<CommonCode> list = service.getSessionServerGroupTypes();
		json.setList(list);
		json.setTotal(list.size());

		return json;
	}

	@RequestMapping(value = "/group/delete", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse deleteGroup(SimpleJsonResponse json, Integer id) {
		DatagridServerGroup group = service.getGroup(id);
		if(group != null){
			List<Server> servers = group.getServers();
			for(Server s: servers){
				s.setDatagridServerGroup(null);
			}
			serverService.saveList(servers);
			service.delete(group);
		}
		return json;
	}

	@RequestMapping(value = "/group/save", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse saveGroup(SimpleJsonResponse json, Integer id,
			String name, Integer typeCdId, String serverIds) {
		DatagridServerGroup group = new DatagridServerGroup();
		if (id > 0) {
			group = service.getGroup(id);
		}
		if (group != null) {
			group.setName(name);
			group.setTypeCdId(typeCdId);
		}
		List<Server> servers = new ArrayList<>();
		String[] idStrings = serverIds.split("#", 0);
		for (int i = 1; i < idStrings.length; i++) { // the first element is
														// empty
			Server s = serverService.retrieve(Integer.parseInt(idStrings[i]));
			if (s != null) {
				servers.add(s);
			}
		}
		if (id > 0) { // edit case
			List<Server> removedServers = new ArrayList<>();
			List<Server> currentServers = group.getServers();
			for (Server s : currentServers) {
				if (!servers.contains(s)) {
					s.setDatagridServerGroup(null);
					removedServers.add(s);
				}
			}
			serverService.saveList(removedServers);
		}
		// }
		group.setServers(servers);
		group = service.save(group);
		json.setData(group.getId());
		for (Server s : servers) {
			s.setDatagridServerGroup(group);
		}
		serverService.saveList(servers);

		return json;
	}
}
