package com.athena.meerkat.controller.web.resources;

import java.net.PasswordAuthentication;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.common.MeerkatUtils;
import com.athena.meerkat.controller.common.SSHManager;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.NetworkInterface;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.SshAccount;
import com.athena.meerkat.controller.web.resources.services.ServerService;
import com.athena.meerkat.controller.web.user.entities.User;
import com.athena.meerkat.controller.web.user.entities.UserRole;

@Controller
@RequestMapping("/res/server")
// public class MachineController implements InitializingBean {
public class ServerController {
	@Autowired
	private ServerService service;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getList(GridJsonResponse json) {
		List<Server> result = service.getList();
		json.setSuccess(true);
		json.setList(result);
		json.setTotal(result.size());
		return json;
	}

	@RequestMapping(value = "/simplelist", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getSimpleList(SimpleJsonResponse json) {
		List<Server> result = service.getSimpleMachineList();
		json.setSuccess(true);
		json.setData(result);
		return json;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse get(SimpleJsonResponse json, int id) {
		Server m = service.retrieve(id);
		if (m != null) {
			json.setData(m);
			json.setSuccess(true);
		} else {
			json.setMsg("Server does not exist.");
			json.setSuccess(false);
		}
		return json;
	}

	@RequestMapping(value = "/{serverId}/nis", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getNIs(GridJsonResponse json,
			@PathVariable Integer serverId) {
		Server m = service.retrieve(serverId);
		if (m != null) {
			json.setList((List<?>) m.getNetworkInterfaces());
			json.setTotal(m.getNetworkInterfaces().size());
			json.setSuccess(true);
		} else {
			json.setMsg("Server does not exist.");
			json.setSuccess(false);
		}
		return json;
	}

	@RequestMapping(value = "/{serverId}/sshAccounts", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getSSHAccounts(GridJsonResponse json,
			@PathVariable Integer serverId) {
		Server m = service.retrieve(serverId);
		if (m != null) {
			json.setList((List<?>) m.getSshAccounts());
			json.setTotal(m.getSshAccounts().size());
			json.setSuccess(true);
		} else {
			json.setMsg("Server does not exist.");
			json.setSuccess(false);
		}
		return json;
	}

	@RequestMapping(value = "/sshAcc", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getSSHAccount(SimpleJsonResponse json, Integer id) {
		SshAccount ssh = service.getSSHAccount(id);
		if (ssh == null) {
			json.setSuccess(false);
			json.setMsg("SSH Account does not exist.");
		} else {
			json.setData(ssh);
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping(value = "/delssh", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse delSSHAccount(SimpleJsonResponse json, Integer id) {
		SshAccount ssh = service.getSSHAccount(id);
		if (ssh == null) {
			json.setSuccess(false);
			json.setMsg("SSH Account does not exist.");
		} else {
			Server server = ssh.getServer();
			server.removeSSHAccount(ssh);
			ssh.setServer(null);
			service.save(server);
			service.deleteSSHAccount(ssh);
			json.setData(ssh);
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse saveSeverInfo(SimpleJsonResponse json,
			Server server, String sshIPAddr, String sshUserName,
			String sshPassword) {
		Server currentServer;
		if (server.getId() == 0) {
			Server existingServer = service.getServerByName(server.getName());
			if (existingServer != null) {
				json.setMsg("Server name is duplicated.");
				json.setSuccess(false);
				return json;
			}
			currentServer = server;
			currentServer = service.save(currentServer);

			NetworkInterface ni = new NetworkInterface();
			if(!MeerkatUtils.validateIPAddress(sshIPAddr)){
				json.setMsg("SSH IP Address is invalid.");
				json.setSuccess(false);
				return json;
			}
			ni.setIpv4(sshIPAddr);
			ni = service.saveNI(ni);
			ni.setServer(currentServer);

			currentServer.setSshNi(ni);
			currentServer.addNetworkInterface(ni);
			SshAccount sshAccount = service.getSSHAccountByUserNameAndServerId(
					sshUserName, server.getId());
			if (sshAccount == null) {
				sshAccount = new SshAccount();
				sshAccount.setUsername(sshUserName);
				sshAccount.setPassword(sshPassword);
				sshAccount = service.saveSSHAccount(sshAccount);
			}

			sshAccount.setServer(currentServer);
			server.addSshAccounts(sshAccount);
			service.saveNI(ni);
			service.saveSSHAccount(sshAccount);
			service.save(currentServer);

		} else { // edit case
			if (server.getName().trim() == "" || server.getSshPort() < 0
					|| server.getHostName() == "" || server.getSshNiId() <= 0) {
				json.setMsg("The input data is invalid.");
				json.setSuccess(false);
				return json;
			}
			currentServer = service.retrieve(server.getId());
			if (currentServer == null) {
				json.setMsg("Server does not exist.");
				json.setSuccess(false);
				return json;
			}
			currentServer.setName(server.getName());
			currentServer.setHostName(server.getHostName());
			currentServer.setSshPort(server.getSshPort());
			NetworkInterface ni = service.getNiById(server.getSshNiId());
			currentServer.setSshNi(ni);
			service.save(currentServer);
		}

		json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/testssh", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse testSSHConnection(SimpleJsonResponse json,
			String userID, String password, String ipAddr, int port) {
		SSHManager sshMng = new SSHManager(userID, password, ipAddr, "", port,
				1000);
		String errorMsg = sshMng.connect();
		if (errorMsg == null || errorMsg == "") {
			sshMng.close();
			json.setData(true);
			json.setSuccess(true);
			json.setMsg("SSH connection is successful.");
		} else {
			json.setSuccess(false);
			json.setMsg("SSH connection fail.");
		}
		return json;

	}

	@RequestMapping(value = "/updatessh", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse updateSSH(SimpleJsonResponse json,
			SshAccount account) {
		if (account.getId() == 0) {
			SshAccount existingAccount = service
					.getSSHAccountByUserNameAndServerId(account.getUsername(),
							account.getServerId());
			if (existingAccount != null) {
				json.setMsg("User ID is duplicated.");
				json.setSuccess(false);
			}
		}
		Server server = service.retrieve(account.getServerId());
		account.setServer(server);

		if (service.saveSSHAccount(account) != null) {
			json.setSuccess(true);
			json.setMsg("Update done");
		}
		return json;
	}

	// @RequestMapping(value = "/evlist", method = RequestMethod.GET)
	// @ResponseBody
	// public SimpleJsonResponse getEVList(SimpleJsonResponse json, int
	// machineId) {
	//
	// Machine machine = service.retrieve(machineId);
	//
	// if (machine == null) {
	// json.setSuccess(false);
	// json.setMsg("Machine does not exist.");
	// } else {
	// List<EnvironmentVariableValue> list = evService
	// .getByMachine(machine);
	// json.setData(list);
	// json.setSuccess(true);
	// }
	// return json;
	// }

	@RequestMapping(value = "/envrevisions", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getEVRevisions(SimpleJsonResponse json,
			int machineId) {

		// Machine machine = service.retrieve(machineId);
		//
		// if (machine == null) {
		// json.setSuccess(false);
		// json.setMsg("Machine does not exist.");
		// } else {
		// List<Revision> list = machine
		// .getRevisions(MeerkatConstants.REVISION_ENV_CONFIG_TYPE);
		// json.setData(list);
		// json.setSuccess(true);
		// }
		return json;
	}
}
