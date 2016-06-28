package com.athena.meerkat.controller.web.resources.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.web.entities.NetworkInterface;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.SshAccount;
import com.athena.meerkat.controller.web.monitoring.StompEventListener;
import com.athena.meerkat.controller.web.resources.repositories.NetworkInterfaceRepository;
import com.athena.meerkat.controller.web.resources.repositories.SSHAccountRepository;
import com.athena.meerkat.controller.web.resources.repositories.ServerRepository;

/**
 * This is an service that is used for processing machine information
 * 
 * @author Tran Ho
 */

@Service
public class ServerService {
	@Autowired
	private ServerRepository serverRepo;
	@Autowired
	private NetworkInterfaceRepository niRepo;

	@Autowired
	private SSHAccountRepository sshRepo;

	@Autowired
	private StompEventListener agentEventListener;

	public ServerService() {

	}

	// public ServiceResult retrieve(int id) {
	// return new ServiceResult(Status.DONE, "", machineRepo.findOne(id));
	// }
	public Server getServer(int id) {
		Server server = serverRepo.findOne(id);
		
		if(server != null) {
			server.setRunningAgent(agentEventListener.isRunningAgent(server.getId()));
		}
		
		return server;
	}

	public List<Server> getListByType(int type) {

		// List<Server> list = machineRepo.findByMachineServerType(type);
		// return list;
		return null;
	}

	public List<Server> getList() {

		Sort sort = new Sort("name");
		List<Server> list = serverRepo.findAll(sort);

		for (Server server : list) {
			server.setRunningAgent(agentEventListener.isRunningAgent(server.getId()));
		}

		return list;
	}

	public List<Server> getSimpleMachineList() {
		List<Server> list = getList();
		List<Server> simpleList = new ArrayList<Server>();
		for (Server m : list) {
			final String _name = m.getName();
			final int _id = m.getId();
			// simpleList.add(new Server(_id, _name));
		}
		return simpleList;
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param machine
	 */
	public Server save(Server machine) {
		return serverRepo.save(machine);
	}

	public Server getServerByName(String name) {
		return serverRepo.findByName(name);
	}

	public Server getServerBySSHIPAddress(String sshIPAddr) {
		return serverRepo.findBySshNi_ipv4(sshIPAddr);
	}

	public NetworkInterface getNiById(int sshNiId) {
		return niRepo.findOne(sshNiId);
	}

	public SshAccount getSSHAccount(Integer id) {
		return sshRepo.findOne(id);
	}

	public SshAccount getSSHAccountByUserName(String username) {
		return sshRepo.findByUsername(username);
	}

	public SshAccount saveSSHAccount(SshAccount account) {
		return sshRepo.save(account);
	}

	public NetworkInterface getNiByIp4(String sshIPAddr) {
		return niRepo.findByIpv4(sshIPAddr);
	}

	public NetworkInterface saveNI(NetworkInterface ni) {
		return niRepo.save(ni);
	}

	public SshAccount getSSHAccountByUserNameAndServerId(String sshUserName, int serverId) {
		return sshRepo.findByUsernameAndServer_Id(sshUserName, serverId);
	}

	public void deleteSSHAccount(SshAccount ssh) {
		sshRepo.delete(ssh);

	}

	public void saveList(List<Server> servers) {
		serverRepo.save(servers);

	}

	public void deleteServer(Server server) {
		serverRepo.delete(server);
	}

	public long getServerNo() {
		return serverRepo.count();
	}

	public List<Server> getList(Pageable p) {

		Page<Server> list = serverRepo.findAll(p);

		for (Server server : list) {
			server.setRunningAgent(agentEventListener.isRunningAgent(server.getId()));
		}

		return list == null ? null : list.getContent();
	}

	public long getAllServerCount() {
		return serverRepo.count();
	}

	public List<Server> searchByName(Pageable p, String keyword) {
		return serverRepo.findByNameContaining(p, keyword);
	}

	public long getCount(String keyword) {
		return serverRepo.countByNameContaining(keyword);
	}
}
