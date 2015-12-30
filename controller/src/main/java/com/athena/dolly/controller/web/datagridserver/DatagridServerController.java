package com.athena.dolly.controller.web.datagridserver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.dolly.controller.ServiceResult;

@Controller
@RequestMapping("/datagrid")
public class DatagridServerController {
	@Autowired
	private DataGridServerService service;

	@RequestMapping("/group/list")
	@ResponseBody
	public List<DatagridServerGroup> list() {
		return service.getAvailableGroupList();
	}
}
