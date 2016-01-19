package com.athena.meerkat.controller.web.datagridserver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;

@Service
public class DataGridServerService {
	@Autowired
	DataGridServerRepository repo;

	@Autowired
	DatagridServerGroupRepository groupRepo;

	public List<DatagridServerGroup> getAvailableGroupList() {
		List<DatagridServerGroup> list = groupRepo.findByDomainIdIsNull();
		return list;
	}

	public DatagridServerGroup getGroup(int id) {
		return groupRepo.findOne(id);
	}

	public boolean saveGroup(DatagridServerGroup group) {
		return groupRepo.save(group) == null ? false : true;
	}
}
