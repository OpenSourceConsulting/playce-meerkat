package com.athena.meerkat.controller.web.resources.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athena.meerkat.common.util.JSONUtil;
import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeRepository;
import com.athena.meerkat.controller.web.entities.CommonCode;
import com.athena.meerkat.controller.web.entities.DatagridServer;
import com.athena.meerkat.controller.web.entities.DatagridServerGroup;
import com.athena.meerkat.controller.web.entities.DatagridServerPK;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.resources.repositories.DatagridServerGroupRepository;
import com.athena.meerkat.controller.web.resources.repositories.DatagridServersRepository;

@Service
public class DataGridServerGroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataGridServerGroupService.class);

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
		return commonCodeRepo.findByGropId(MeerkatConstants.CODE_GROP_DATAGRID_SEVER_TYPE);
	}

	public DatagridServerGroup save(DatagridServerGroup group) {
		return groupRepo.save(group);
	}

	@Transactional
	public void delete(DatagridServerGroup group) {

		//List<DatagridServer> dServers = group.getDatagridServers();
		//List<ClusteringConfiguration> configs = group.getClusteringConfigurations();

		//datagridServerRepo.deleteInBatch(dServers);
		//clusteringConfService.deleteClusteringConfig(configs);

		groupRepo.delete(group);

	}

	public List<DatagridServer> getDatagridServers(int groupId) {
		return datagridServerRepo.findByDatagridServerGroupId(groupId);
	}

	public List<Server> getServerList(Integer groupId) {

		List<Server> servers = new ArrayList<Server>();

		List<DatagridServer> dgServers = datagridServerRepo.findByDatagridServerGroupId(groupId);

		for (DatagridServer datagridServer : dgServers) {

			Server server = datagridServer.getServer();
			server.setPort(datagridServer.getPort());

			servers.add(server);
		}

		return servers;
	}

	/**
	 * <pre>
	 * dolly.properties 의 infinispan.client.hotrod.server_list property value 반환.
	 * </pre>
	 * 
	 * @param groupId
	 * @return
	 */
	public String getServerListPropertyValue(Integer groupId) {
		StringBuffer sb = new StringBuffer();

		List<Server> serverList = getServerList(groupId);
		for (Server server : serverList) {

			if (sb.length() > 0) {
				sb.append(";");
			}

			sb.append(server.getSshIPAddr() + ":" + server.getPort());
		}

		LOGGER.debug("session server list property : {}", sb.toString());

		LOGGER.debug("session server list property : {}", sb.toString());

		LOGGER.debug("session server list property : {}", sb.toString());

		return sb.toString();
	}

	public DatagridServer getDatagridServer(DatagridServerPK id) {
		return datagridServerRepo.findOne(id);
	}

	public void remove(List<DatagridServer> datagridServers) {
		datagridServerRepo.delete(datagridServers);
	}

	public void remove(DatagridServer datagridServer) {
		datagridServerRepo.delete(datagridServer);
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

	public List<DatagridServerGroup> getNotEmptyGroups() {
		List<DatagridServerGroup> all = groupRepo.findAll();
		List<DatagridServerGroup> result = new ArrayList<DatagridServerGroup>();
		for (DatagridServerGroup group : all) {
			if (group.getDatagridServers() != null && group.getDatagridServers().size() > 0) {
				result.add(group);
			}
		}
		return result;
	}
}
