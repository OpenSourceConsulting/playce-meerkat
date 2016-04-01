package com.athena.meerkat.controller.web.resources;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.ExitStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.CommonCode;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.resources.services.DataSourceService;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;
import com.athena.meerkat.controller.web.user.entities.User;

@Controller
@RequestMapping("/res/ds")
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

	@RequestMapping("/list")
	@ResponseBody
	public GridJsonResponse getAvailableDataSource(GridJsonResponse json) {
		List<DataSource> datasources = service.getAll();
		json.setList(datasources);
		json.setTotal(datasources.size());

		return json;
	}
	
	@RequestMapping(value = "/domain/link/list", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getDatasourceList(GridJsonResponse json, @RequestParam(value="domainId") int domainId) {
		List<DataSource> datasources = service.listDomainLinked(domainId);
		json.setList(datasources);
		json.setTotal(datasources.size());

		return json;
	}
	
	@RequestMapping("/listna")
	@ResponseBody
	public GridJsonResponse getAvailableDataSource(GridJsonResponse json, @RequestParam(value="domainId") int domainId) {
		
		List<DataSource> datasources = service.listNotAssigned(domainId);
		json.setList(datasources);
		json.setTotal(datasources.size());
		
		return json;
	}

	@RequestMapping("/dbtype/list")
	@ResponseBody
	public GridJsonResponse getDBTypes(GridJsonResponse json) {
		List<CommonCode> dbTypes = service.getDBTypes();
		json.setList(dbTypes);
		json.setTotal(dbTypes.size());
		json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/{dsId}/tomcatlist", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getAssociatedTomcatInstances(GridJsonResponse json,
			@PathVariable Integer dsId) {
		List<TomcatInstance> tomcatInstances = service
				.getAssocicatedTomcatInstances(dsId);
		json.setList(tomcatInstances);
		json.setTotal(tomcatInstances.size());
		json.setSuccess(true);
		return json;
	}

	@RequestMapping("/testConnection")
	@ResponseBody
	public SimpleJsonResponse testConnection(SimpleJsonResponse json,
			String jdbcUrl, String username, String password, String dbType) {
		// String jdbcUrl = "jdbc://localhost:3306/test";
		// String userName = "root";
		// String password = "admin";
		// String dbType = "MySQL";
		boolean result = service.testConnection(jdbcUrl, username, password,
				dbType);
		json.setSuccess(result);
		json.setMsg(result ? "Connection is OK." : "Fail connection.");
		return json;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse saveDS(SimpleJsonResponse json, DataSource ds) {
		List<DataSource> existingDSs = service.getDatasources(ds.getName());

		if (existingDSs.size() > 0 && ds.getId() == 0) {
			json.setSuccess(false);
			json.setMsg("Datasource name is duplicated.");
		}
		if (service.save(ds) != null) {
			json.setSuccess(true);
			json.setMsg("Datasource is updated successfully.");
		}

		return json;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse deleteDS(SimpleJsonResponse json, int id) {
		DataSource ds = service.findOne(id);
		if (ds == null) {
			json.setSuccess(false);
			json.setMsg("Datasource does not exist.");
		} else {
			service.delete(ds);
			json.setSuccess(true);
		}
		return json;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse search(GridJsonResponse json, String keyword) {
		List<DataSource> result = service.search(keyword);
		json.setSuccess(true);
		json.setTotal(result.size());
		json.setList(result);
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
