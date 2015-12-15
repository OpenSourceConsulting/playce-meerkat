/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Bong-Jin Kwon	2015. 1. 9.		First Draft.
 */
package com.athena.dolly.controller.web.tomcat.instance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.athena.dolly.controller.ServiceResult;
import com.athena.dolly.controller.ServiceResult.Status;
import com.athena.dolly.controller.common.SSHManager;
import com.athena.dolly.controller.web.common.model.ExtjsGridParam;
import com.athena.dolly.controller.web.common.model.GridJsonResponse;
import com.athena.dolly.controller.web.common.model.SimpleJsonResponse;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 2.0
 */
@RestController
@RequestMapping("/tomcat")
public class TomcatInsanceController {

	@Autowired
	private TomcatInstanceService service;

	public TomcatInsanceController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "/instance/list", method = RequestMethod.GET)
	public GridJsonResponse getList(GridJsonResponse res,
			ExtjsGridParam gridParam) {

		// service.save(new TomcatInstance("inst11", "1234"));
		// service.save(new TomcatInstance("inst22", "2222"));

		Page<TomcatInstance> page = service.getList(new PageRequest(gridParam
				.getPage() - 1, gridParam.getLimit()));

		res.setTotal((int) page.getTotalElements());
		res.setList(page.getContent());

		return res;
	}

	@RequestMapping(value = "/instance", method = { RequestMethod.POST,
			RequestMethod.PUT })
	public SimpleJsonResponse save(SimpleJsonResponse res, TomcatInstance inst) {
		TomcatInstance result = service.save(inst);

		service.loadTomcatConfig(result);

		res.setData(result);

		return res;
	}

	@RequestMapping(value = "/instance/{instId}", method = RequestMethod.GET)
	public TomcatInstance get(@PathVariable Long instId) {
		return service.getOne(instId);
	}

	@RequestMapping(value = "/instance/{instId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Long instId) {
		service.delete(instId);
	}

	@RequestMapping(value = "/sshtest", method = RequestMethod.POST)
	public SimpleJsonResponse sshConnectionTest(SimpleJsonResponse res,
			TomcatInstance inst) {

		// SSHManager sshMng = new SSHManager(inst.getSshUsername(),
		// inst.getSshPassword(), inst.getIpAddr(), "", inst.getSshPort());

		// String errorMsg = sshMng.connect();
		//
		// try {
		// if (errorMsg != null) {
		// res.setSuccess(false);
		// res.setMsg(errorMsg);
		// }
		// } finally {
		// sshMng.close();
		// }

		return res;
	}

	@RequestMapping(value = "/instance/start", method = RequestMethod.POST)
	@ResponseBody
	public boolean startTomcat(int id) {
		return service.start(id);
	}

	@RequestMapping(value = "/instance/restart", method = RequestMethod.POST)
	@ResponseBody
	public boolean restartTomcat(int id) {
		return service.restart(id);
	}

	@RequestMapping(value = "/instance/stop", method = RequestMethod.POST)
	@ResponseBody
	public boolean stopTomcat(int id) {
		return service.stop(id);
	}

	@RequestMapping("/list")
	List<TomcatInstance> getTomcatInstance() {
		ServiceResult result = service.getAll();
		if (result.getStatus() == Status.DONE) {
			List<TomcatInstance> tomcats = (List<TomcatInstance>) result
					.getReturnedVal();
			return tomcats;
		}
		return null;
	}
}
