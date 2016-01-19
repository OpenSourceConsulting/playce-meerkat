package com.athena.meerkat.controller.web.datasource;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.web.tomcat.instance.TomcatInstance;
import com.mysql.jdbc.Connection;

@Service
public class DatasourceService {
	@Autowired
	private DatasourceRepository datasourceRepo;

	public ServiceResult add(String name, String dbType, String userName,
			String pwd, int timeOut, int maxConnectionPool,
			int minConnectionPool, String jdbcUrl) {
		if (name.isEmpty() || dbType.isEmpty() || userName.isEmpty()
				|| pwd.isEmpty() || timeOut < 0 || maxConnectionPool < 0
				|| minConnectionPool < 0
				|| maxConnectionPool < minConnectionPool || jdbcUrl.isEmpty()) {
			return new ServiceResult(Status.FAILED, "Invalid data");
		}
		Datasource ds = datasourceRepo.findByNameOrJdbcUrl(name, jdbcUrl);
		if (ds != null) {
			return new ServiceResult(Status.FAILED, "Duplicated");
		}
		ds = new Datasource();
		ds.setName(name);
		ds.setDbType(dbType);
		ds.setUserName(userName);
		ds.setPassword(pwd);
		ds.setTimeout(timeOut);
		ds.setMaxConnection(maxConnectionPool);
		ds.setMinConnectionPool(minConnectionPool);
		ds.setJdbcUrl(jdbcUrl);
		datasourceRepo.save(ds);
		return new ServiceResult(Status.DONE, "Done", ds);
	}

	public ServiceResult delete(int datasource_id) {
		Datasource ds = datasourceRepo.findOne(datasource_id);
		if (ds == null) {
			return new ServiceResult(Status.FAILED, "Not exist");
		}
		datasourceRepo.delete(ds);
		return new ServiceResult(Status.DONE, "Deleted", true);
	}

	public ServiceResult testConnection(String jdbcUrl, String userName,
			String password, String dbType) {
		String driver = MeerkatConstants.MYSQL_DRIVER;
		if (dbType.toLowerCase().equals("mysql")) {
			driver = MeerkatConstants.MYSQL_DRIVER;
		} else if (dbType.toLowerCase().equals("oracle")) {
			driver = MeerkatConstants.ORACLE_DRIVER;
		}
		// test connection
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			return new ServiceResult(Status.FAILED, "No JDBC driver");
		}
		try {
			Connection conn = (Connection) DriverManager.getConnection(jdbcUrl,
					userName, password);
			if (conn == null) {
				return new ServiceResult(Status.FAILED, "Connection failed");
			}
			return new ServiceResult(Status.DONE, "Successful connection");
		} catch (SQLException e) {
			return new ServiceResult(Status.FAILED, "Connection failed");
		}

	}

	public ServiceResult search(String keyword) {
		List<Datasource> datasourcesByName = datasourceRepo.findByName(keyword);
		List<Datasource> datasourcesByJdbc = datasourceRepo
				.findByJdbcUrl(keyword);

		if (datasourcesByJdbc == null && datasourcesByName == null) {
			return new ServiceResult(Status.FAILED, "Null");
		}
		datasourcesByJdbc.addAll(datasourcesByName);
		return new ServiceResult(Status.DONE, "Done", datasourcesByJdbc);
	}

	public ServiceResult edit(int id, String name, String dbType,
			String userName, String password, int timeout,
			int maxConnectionPool, int minConnectionPool, String jdbcUrl) {
		Datasource ds = datasourceRepo.findOne(id);
		if (ds == null) {
			return new ServiceResult(Status.FAILED, "Not exist");
		} else {
			if (name.isEmpty() || dbType.isEmpty() || userName.isEmpty()
					|| password.isEmpty() || timeout < 0
					|| maxConnectionPool < 0 || minConnectionPool < 0
					|| maxConnectionPool < minConnectionPool
					|| jdbcUrl.isEmpty()) {
				return new ServiceResult(Status.FAILED, "Invalid data");
			}
			ds.setName(name);
			ds.setDbType(dbType);
			ds.setUserName(userName);
			ds.setPassword(password);
			ds.setTimeout(timeout);
			ds.setMaxConnection(maxConnectionPool);
			ds.setMinConnectionPool(minConnectionPool);
			ds.setJdbcUrl(jdbcUrl);
			datasourceRepo.save(ds);
			return new ServiceResult(Status.DONE, "Done");
		}
	}

	public ServiceResult getAll() {
		List<Datasource> list = datasourceRepo.findAll();
		return new ServiceResult(Status.DONE, "Done", list);

	}
}
