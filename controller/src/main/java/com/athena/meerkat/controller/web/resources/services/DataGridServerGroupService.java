package com.athena.meerkat.controller.web.resources.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeRepository;
import com.athena.meerkat.controller.web.common.util.JSONUtil;
import com.athena.meerkat.controller.web.entities.ClusteringConfiguration;
import com.athena.meerkat.controller.web.entities.CommonCode;
import com.athena.meerkat.controller.web.entities.DatagridServerGroup;
import com.athena.meerkat.controller.web.entities.DatagridServer;
import com.athena.meerkat.controller.web.entities.DatagridServerPK;
import com.athena.meerkat.controller.web.resources.repositories.DatagridServerGroupRepository;
import com.athena.meerkat.controller.web.resources.repositories.DatagridServersRepository;

@Service
public class DataGridServerGroupService {
	@Autowired
	DatagridServerGroupRepository groupRepo;
	@Autowired
	CommonCodeRepository commonCodeRepo;
	
	@Autowired
	private ClusteringConfigurationService clusteringConfService;
	
	@Autowired
	DatagridServersRepository datagridServerRepo;

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

	@Transactional
	public void delete(DatagridServerGroup group) {
		
		List<DatagridServer> dServers = group.getDatagridServers();
		List<ClusteringConfiguration> configs = group.getClusteringConfigurations();
		
		datagridServerRepo.deleteInBatch(dServers);
		clusteringConfService.deleteClusteringConfig(configs);
		
		
		groupRepo.delete(group);

	}
	
	public List<DatagridServer> getDatagridServers(int groupId) {
		return datagridServerRepo.findByDatagridServerGroupId(groupId);
	}
	
	public DatagridServer getDatagridServer(DatagridServerPK id) {
		return datagridServerRepo.findOne(id);
	}
	
	public void remove(List<DatagridServer> datagridServers) {
		datagridServerRepo.delete(datagridServers);
	}
	
	public void save(List<DatagridServer> datagridServers) {
		datagridServerRepo.save(datagridServers);
	}
	
	@Transactional
	public void saveDatagridServers(int groupId, String sessionServersJson, List<DatagridServer> removalServers) {
		
		List<DatagridServer> servers = JSONUtil.jsonToList(sessionServersJson, List.class, DatagridServer.class);
		
		for (DatagridServer datagridServer : servers) {
			datagridServer.setDatagridServerGroupId(groupId);
		}
		
		if (removalServers != null) { // edit case
			
			remove(removalServers);
		}
		
		save(servers);
	}
	
	
}
