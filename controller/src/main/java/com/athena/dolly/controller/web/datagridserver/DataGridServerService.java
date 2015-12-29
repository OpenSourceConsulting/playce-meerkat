package com.athena.dolly.controller.web.datagridserver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.dolly.controller.ServiceResult;
import com.athena.dolly.controller.ServiceResult.Status;

@Service
public class DataGridServerService {
	@Autowired
	DataGridServerRepository repo;

	@Autowired
	DatagridServerGroupRepository groupRepo;

	public List<DatagridServerGroup> getGroupList() {
		List<DatagridServerGroup> list = groupRepo.findAll();
		return list;
	}
	
	public DatagridServerGroup getGroup(int id){
		return groupRepo.getOne(id);
	}

}
