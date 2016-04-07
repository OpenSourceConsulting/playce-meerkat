package com.athena.meerkat.controller.web.resources.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.web.entities.ClusteringConfigurationVersion;
import com.athena.meerkat.controller.web.entities.DatagridServerGroup;
import com.athena.meerkat.controller.web.resources.repositories.DatagridServerGroupRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.ClusteringConfigurationVersionRepository;

@Service
public class DataGridServerGroupService {
	@Autowired
	DatagridServerGroupRepository groupRepo;
	@Autowired
	ClusteringConfigurationVersionRepository clusteringConfigVerRepo;

	public List<DatagridServerGroup> getAll() {
		List<DatagridServerGroup> list = groupRepo.findAll();
		return list;
	}

	public DatagridServerGroup getGroup(int id) {
		return groupRepo.findOne(id);
	}

	public ClusteringConfigurationVersion getLatestClusteringConfVersion(
			Integer groupId) {
		List<ClusteringConfigurationVersion> latestVersion = clusteringConfigVerRepo
				.findFirstClusteringVersionByDatagridGroupId(groupId);
		if (latestVersion.size() == 0) {
			return null;
		}
		return latestVersion.get(0);

	}
}
