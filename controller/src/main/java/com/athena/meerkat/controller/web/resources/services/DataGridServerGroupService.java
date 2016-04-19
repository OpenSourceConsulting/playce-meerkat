package com.athena.meerkat.controller.web.resources.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeRepository;
import com.athena.meerkat.controller.web.entities.CommonCode;
import com.athena.meerkat.controller.web.entities.DatagridServerGroup;
import com.athena.meerkat.controller.web.resources.repositories.DatagridServerGroupRepository;

@Service
public class DataGridServerGroupService {
	@Autowired
	DatagridServerGroupRepository groupRepo;
	@Autowired
	CommonCodeRepository commonCodeRepo;

	public List<DatagridServerGroup> getAll() {
		List<DatagridServerGroup> list = groupRepo.findAll();
		return list;
	}

	public DatagridServerGroup getGroup(int id) {
		return groupRepo.findOne(id);
	}

	public List<CommonCode> getSessionServerGroupTypes() {
		return commonCodeRepo
				.findByGropId(MeerkatConstants.SESSION_SERVER_TYPE_GROUP_ID);
	}

	public DatagridServerGroup save(DatagridServerGroup group) {
		return groupRepo.save(group);
	}

	public void delete(DatagridServerGroup group) {
		groupRepo.delete(group);

	}
}
