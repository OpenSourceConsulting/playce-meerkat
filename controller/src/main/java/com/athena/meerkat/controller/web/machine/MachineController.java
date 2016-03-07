package com.athena.meerkat.controller.web.machine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.common.MeerkatUtils;
import com.athena.meerkat.controller.common.SSHManager;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.env.EnvironmentVariable;
import com.athena.meerkat.controller.web.env.EnvironmentVariableService;

@Controller
@RequestMapping("/res/machine")
// public class MachineController implements InitializingBean {
public class MachineController {
	@Autowired
	private MachineService service;
	@Autowired
	private EnvironmentVariableService evService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add() {
		String mName = "Example";// retrive from form
		String ip4Addr = "192.168.0.88";// retrieve from form;
		String sshUserName = "root";// retrieve from form;
		String sshPassword = "test123";// retrieve from form;
		int sshPort = MeerkatConstants.DEFAULT_SSH_PORT;
		String desciprtion = "";
		ServiceResult status = service.add(mName, desciprtion, ip4Addr,
				sshPort, sshUserName, sshPassword);
		return status.getMessage();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<Machine> getList() {
		ServiceResult result = service.getList();
		if (result.getStatus() == Status.DONE) {
			List<Machine> list = (List<Machine>) result.getReturnedVal();
			return list;
		}
		return null;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse get(SimpleJsonResponse json, int id) {
		Machine m = service.retrieve(id);
		if (m != null) {
			json.setData(m);
			json.setSuccess(true);
		} else {
			json.setMsg("Tomcat server does not exist.");
			json.setSuccess(false);
		}
		return json;
	}

	// for testing bean creation
	// @Override
	// public void afterPropertiesSet() throws Exception {
	// // System.err.println("service : " + service);
	// }

	@RequestMapping(value = "/testConnection", method = RequestMethod.GET)
	@ResponseBody
	public boolean testMachineConnection(int id) {
		Machine machine = service.retrieve(id);
		if (machine == null) {
			return false;
		}
		SSHManager sshMng = new SSHManager(machine.getSshUsername(),
				machine.getSshPassword(), machine.getSSHIPAddr(), "",
				machine.getSshPort(), 1000);
		String errorMsg = sshMng.connect();
		if (errorMsg == null || errorMsg == "") {
			sshMng.close();
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/tomcatserver", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getTomcatServers(SimpleJsonResponse json) {
		List<Machine> tomcatServers = service
				.getListByType(MeerkatConstants.MACHINE_TOMCAT_SERVER_TYPE);
		if (tomcatServers != null) {
			json.setSuccess(true);
			json.setData(tomcatServers);
		} else {
			json.setSuccess(false);
			json.setMsg("Error occured.");
		}
		return json;
	}

	@RequestMapping(value = "/testssh", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse updateSSH(SimpleJsonResponse json,
			String sshIpAddr, int sshPort, String sshUserName,
			String sshPassword) {
		SSHManager sshMng = new SSHManager(sshUserName, sshPassword, sshIpAddr,
				"", sshPort, 1000);
		String errorMsg = sshMng.connect();
		if (errorMsg == null || errorMsg == "") {
			sshMng.close();
			json.setSuccess(true);
			json.setMsg("Ping successfully.");
		} else {
			json.setSuccess(false);
			json.setMsg("Connection fail");
		}
		return json;
	}

	@RequestMapping(value = "/updatessh", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse updateSSH(SimpleJsonResponse json,
			String sshIpAddr, int sshPort, String sshUserName,
			String sshPassword, int machineId) {
		Machine machine = service.retrieve(machineId);
		if (machine == null) {
			json.setMsg("Server does not exist.");
			json.setSuccess(false);
		} else {
			if (!MeerkatUtils.validateIPAddress(sshIpAddr)) {
				json.setMsg("IP Address is invalid.");
				json.setSuccess(false);
			} else {
				machine.setSSHIPAddr(sshIpAddr);
				machine.setSshPort(sshPort);
				machine.setSshUsername(sshUserName);
				machine.setSshPassword(sshPassword);
				if (service.save(machine) != null) {
					json.setMsg("Edit successfully.");
					json.setSuccess(true);
				} else {
					json.setMsg("Edit failed.");
					json.setSuccess(false);
				}
			}
		}
		return json;
	}

	@RequestMapping(value = "/evlist", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getEVList(SimpleJsonResponse json, int machineId) {
		Machine machine = service.retrieve(machineId);
		if (machine == null) {
			json.setSuccess(false);
			json.setMsg("Machine does not exist.");
		} else {
			List<EnvironmentVariable> list = evService.getByMachine(machine);
			json.setData(list);
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping(value = "/ev/revisions", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getEVRevision(SimpleJsonResponse json,
			int machineId) {
		Machine machine = service.retrieve(machineId);
		if (machine == null) {
			json.setSuccess(false);
			json.setMsg("Machine does not exist.");
		} else {
			// List<EnvRevision> revisions = new ArrayList<EnvRevision>();
			// revision = evService.getRevisions(machine);
			// json.setData(revisions);
			json.setSuccess(true);
		}
		return json;
	}
}
