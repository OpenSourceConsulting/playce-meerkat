package com.athena.dolly.controller.web.machine;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.dolly.controller.DollyConstants;
import com.athena.dolly.controller.ServiceResult;
import com.athena.dolly.controller.ServiceResult.Status;
import com.athena.dolly.controller.common.SSHManager;
import com.athena.dolly.controller.web.common.model.ExtjsGridParam;
import com.athena.dolly.controller.web.common.model.GridJsonResponse;
import com.athena.dolly.controller.web.common.model.SimpleJsonResponse;

@Controller
@RequestMapping("/machine")
// public class MachineController implements InitializingBean {
public class MachineController {
	@Autowired
	private MachineService service;

	@RequestMapping("/add")
	@ResponseBody
	public String add() {
		String mName = "Example";// retrive from form
		String ip4Addr = "192.168.0.88";// retrieve from form;
		String sshUserName = "root";// retrieve from form;
		String sshPassword = "test123";// retrieve from form;
		int sshPort = DollyConstants.DEFAULT_SSH_PORT;
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
	public SimpleJsonResponse get() {
		int id = 1;
		SimpleJsonResponse res = new SimpleJsonResponse();
		Machine m = service.retrieve(id);
		res.setData(m);
		return res;
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
}
