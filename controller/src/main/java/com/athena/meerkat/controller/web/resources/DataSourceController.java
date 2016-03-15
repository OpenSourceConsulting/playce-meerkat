package com.athena.meerkat.controller.web.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.resources.services.DataSourceService;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;

@Controller
@RequestMapping("/datasource")
public class DataSourceController {

	@Autowired
	private DataSourceService service;
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
	public List<DataSource> search() {
		String keyword = "test";
		ServiceResult result = service.search(keyword);
		if (result.getStatus() == Status.FAILED) {
			return null;
		}
		return (List<DataSource>) result.getReturnedVal();
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
	public SimpleJsonResponse getAvailableDataSource(SimpleJsonResponse json,
			int tomcatId) {
		// TomcatInstance tomcat = tomcatService.findOne(tomcatId);
		// if (tomcat == null) {
		// json.setMsg("Tomcat does not exist.");
		// json.setSuccess(false);
		// } else {
		// List<DataSource> datasources = service.getAll();
		// List<DataSource> associatedDS = (List<DataSource>) tomcat
		// .getDataSources();
		// for (DataSource ds : datasources) {
		// if (associatedDS.indexOf(ds) >= 0) {
		// ds.setSelected(true);
		// }
		// }
		// json.setData(datasources);
		// json.setSuccess(true);
		// }
		return json;
	}

	@RequestMapping("/tomcat/link/list/all")
	@ResponseBody
	public SimpleJsonResponse getAvailableDataSource(SimpleJsonResponse json) {
		List<DataSource> datasources = service.getAll();
		json.setData(datasources);
		json.setSuccess(true);
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
			List<DataSource> datasources = new ArrayList<DataSource>();
			String[] idStrings = ids.split("#", 0);
			for (int i = 1; i < idStrings.length; i++) { // the first element is
															// empty
				DataSource ds = service.findOne(Integer.parseInt(idStrings[i]));
				if (ds != null) {
					datasources.add(ds);
				}
			}
			// service.associateTomcat(tomcat, datasources);
			json.setSuccess(true);
			// json.setData(tomcat.getDataSources());
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
			DataSource ds = service.findOne(dsId);
			if (ds == null) {
				json.setMsg("DataSource does not exist.");
				json.setSuccess(false);
			} else {
				// tomcat.removeDataSource(ds);
				tomcatService.save(tomcat);
				// json.setData(tomcat.getDataSources());
				json.setSuccess(true);
			}
		}
		return json;
	}
}
