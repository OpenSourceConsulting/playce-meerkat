package com.athena.meerkat.controller.web.datasource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.tomcat.instance.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.instance.TomcatInstanceService;

@Controller
@RequestMapping("/datasource")
public class DatasourceController {

	@Autowired
	private DatasourceService service;
	@Autowired
	private TomcatInstanceService tomcatService;

	@RequestMapping("/add")
	@ResponseBody
	public boolean add() {
		// for testing. Later, get the information from UI form
		String name = "Data source 1";
		String dbType = "MySQL";
		String userName = "root";
		String password = "admin";
		int timeout = 1500;
		int maxConnectionPool = 10;
		int minConnectionPool = 1;
		String jdbcUrl = "jdbc://localhost:3306/test";

		ServiceResult result = service.add(name, dbType, userName, password,
				timeout, maxConnectionPool, minConnectionPool, jdbcUrl);
		if (result.getStatus() == Status.DONE) {
			return true;
		}
		return false;
	}

	@RequestMapping("/test")
	@ResponseBody
	public boolean testConnection() {
		String jdbcUrl = "jdbc://localhost:3306/test";
		String userName = "root";
		String password = "admin";
		String dbType = "MySQL";
		ServiceResult result = service.testConnection(jdbcUrl, userName,
				password, dbType);
		if (result.getStatus() == Status.FAILED) {
			return false;
		}
		return true;
	}

	@RequestMapping("/search")
	@ResponseBody
	public List<Datasource> search() {
		String keyword = "test";
		ServiceResult result = service.search(keyword);
		if (result.getStatus() == Status.FAILED) {
			return null;
		}
		return (List<Datasource>) result.getReturnedVal();
	}

	@RequestMapping("/edit")
	@ResponseBody
	public boolean edit() {
		// for testing. Later, get the information from UI form
		int id = 1;
		String name = "Data source 1";
		String dbType = "MySQL";
		String userName = "root";
		String password = "admin";
		int timeout = 1500;
		int maxConnectionPool = 10;
		int minConnectionPool = 1;
		String jdbcUrl = "jdbc://localhost:3306/test";
		ServiceResult result = service.edit(id, name, dbType, userName,
				password, timeout, maxConnectionPool, minConnectionPool,
				jdbcUrl);
		if (result.getStatus() == Status.DONE) {
			return true;
		}
		return false;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public boolean delete() {
		int id = 1;// get from form UI later
		ServiceResult result = service.delete(id);
		if (result.getStatus() == Status.FAILED) {
			return false;
		}
		return true;
	}

	@RequestMapping("/tomcat/link/list")
	@ResponseBody
	public SimpleJsonResponse getAvailableDatasource(SimpleJsonResponse json,
			int tomcatId) {
		TomcatInstance tomcat = tomcatService.findOne(tomcatId);
		if (tomcat == null) {
			json.setMsg("Tomcat does not exist.");
			json.setSuccess(false);
		} else {
			List<Datasource> datasources = service.getAll();
			List<Datasource> associatedDS = (List<Datasource>) tomcat
					.getDatasources();
			for (Datasource ds : datasources) {
				if (associatedDS.indexOf(ds) >= 0) {
					ds.setSelected(true);
				}
			}
			json.setData(datasources);
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping("/tomcat/link/save")
	@ResponseBody
	public SimpleJsonResponse saveLinking(SimpleJsonResponse json, String ids,
			int tomcatId, boolean isRestart) {
		TomcatInstance tomcat = tomcatService.findOne(tomcatId);
		if (tomcat == null) {
			json.setMsg("Tomcat does not exist.");
			json.setSuccess(false);
		} else {
			List<Datasource> datasources = new ArrayList<Datasource>();
			String[] idStrings = ids.split("#", 0);
			for (int i = 1; i < idStrings.length; i++) { // the first element is
															// empty
				Datasource ds = service.findOne(Integer.parseInt(idStrings[i]));
				if (ds != null) {
					datasources.add(ds);
				}
			}
			service.associateTomcat(tomcat, datasources);
			json.setSuccess(true);
			json.setData(tomcat.getDatasources());
		}

		return json;

	}

	@RequestMapping("/tomcat/link/remove")
	@ResponseBody
	public SimpleJsonResponse remove(SimpleJsonResponse json, int tomcatId,
			int dsId) {
		TomcatInstance tomcat = tomcatService.findOne(tomcatId);
		if (tomcat == null) {
			json.setMsg("Tomcat does not exist.");
			json.setSuccess(false);
		} else {
			Datasource ds = service.findOne(dsId);
			if (ds == null) {
				json.setMsg("Datasource does not exist.");
				json.setSuccess(false);
			} else {
				tomcat.removeDatasource(ds);
				tomcatService.save(tomcat);
				json.setData(tomcat.getDatasources());
				json.setSuccess(true);
			}
		}
		return json;
	}
}
