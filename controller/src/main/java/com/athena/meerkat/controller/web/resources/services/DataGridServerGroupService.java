package com.athena.meerkat.controller.web.resources.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.web.entities.DatagridServerGroup;
import com.athena.meerkat.controller.web.resources.repositories.DatagridServerGroupRepository;

@Service
public class DataGridServerGroupService {
	@Autowired
	DatagridServerGroupRepository groupRepo;

	//
	// public List<DatagridServerGroup> getAvailableGroupList() {
	// List<DatagridServerGroup> list = groupRepo.findByDomainIdIsNull();
	// return list;
	// }
	//
	public List<DatagridServerGroup> getAll() {
		List<DatagridServerGroup> list = groupRepo.findAll();
		return list;
	}

	public DatagridServerGroup getGroup(int id) {
		return groupRepo.findOne(id);
	}
	//
	// public boolean saveGroup(DatagridServerGroup group) {
	// return groupRepo.save(group) == null ? false : true;
	// }
}
