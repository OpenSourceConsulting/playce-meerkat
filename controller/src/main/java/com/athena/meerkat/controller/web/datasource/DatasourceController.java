package com.athena.meerkat.controller.web.datasource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;

@Controller
@RequestMapping("/datasource")
public class DatasourceController {

	@Autowired
	private DatasourceService service;

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

}
