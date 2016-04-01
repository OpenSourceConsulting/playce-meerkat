package com.athena.meerkat.controller.web.resources.services;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.web.common.code.CommonCodeRepository;
import com.athena.meerkat.controller.web.entities.CommonCode;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.resources.repositories.DataSourceRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstanceRepository;
import com.mysql.jdbc.Connection;

@Service
public class DataSourceService {
	@Autowired
	private DataSourceRepository datasourceRepo;
	@Autowired
	private TomcatInstanceRepository tomcatRepo;
	@Autowired
	private CommonCodeRepository commonRepo;

	public ServiceResult add(String name, String dbType, String userName,
			String pwd, int timeOut, int maxConnectionPool,
			int minConnectionPool, String jdbcUrl) {
		if (name.isEmpty() || dbType.isEmpty() || userName.isEmpty()
				|| pwd.isEmpty() || timeOut < 0 || maxConnectionPool < 0
				|| minConnectionPool < 0
				|| maxConnectionPool < minConnectionPool || jdbcUrl.isEmpty()) {
			return new ServiceResult(Status.FAILED, "Invalid data");
		}
		DataSource ds = datasourceRepo.findByNameContainingOrJdbcUrlContaining(
				name, jdbcUrl);
		if (ds != null) {
			return new ServiceResult(Status.FAILED, "Duplicated");
		}
		ds = new DataSource();
		ds.setName(name);
		// ds.setUserName(userName);
		ds.setPassword(pwd);
		ds.setTimeout(timeOut);
		ds.setMaxConnection(maxConnectionPool);
		ds.setMinConnectionPool(minConnectionPool);
		ds.setJdbcUrl(jdbcUrl);
		datasourceRepo.save(ds);
		return new ServiceResult(Status.DONE, "Done", ds);
	}

	public void delete(DataSource ds) {
		datasourceRepo.delete(ds);
	}

	public boolean testConnection(String jdbcUrl, String userName,
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
			return false;
		}
		try {
			Connection conn = (Connection) DriverManager.getConnection(jdbcUrl,
					userName, password);
			if (conn == null) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			return false;
		}

	}

	public ServiceResult edit(int id, String name, String dbType,
			String userName, String password, int timeout,
			int maxConnectionPool, int minConnectionPool, String jdbcUrl) {
		DataSource ds = datasourceRepo.findOne(id);
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

			// ds.setUserName(userName);
			ds.setPassword(password);
			ds.setTimeout(timeout);
			ds.setMaxConnection(maxConnectionPool);
			ds.setMinConnectionPool(minConnectionPool);
			ds.setJdbcUrl(jdbcUrl);
			datasourceRepo.save(ds);
			return new ServiceResult(Status.DONE, "Done");
		}
	}

	public List<DataSource> getAll() {
		List<DataSource> list = datasourceRepo.findAll();
		for (DataSource ds : list) {
			ds.setTomcatInstancesNo(this.getAssocicatedTomcatInstancesNo(ds
					.getId()));
		}
		return list;
	}

	public int getAssocicatedTomcatInstancesNo(int datasourceId) {
		return datasourceRepo.getAssocicatedTomcatInstancesNo(datasourceId);
	}

	public List<TomcatInstance> getAssocicatedTomcatInstances(int datasourceId) {
		return datasourceRepo.getAssocicatedTomcatInstances(datasourceId);
	}

	public DataSource findOne(int id) {
		return datasourceRepo.findOne(id);
	}

	public void associateTomcat(TomcatInstance tomcat,
			List<DataSource> datasources) {
		// for (DataSource ds : datasources) {
		// if (!ds.getTomcatInstances().contains(tomcat)) {
		// ds.associateTomcat(tomcat);
		// }
		//
		// }
		// List<DataSource> currentDataSources = (List<DataSource>) tomcat
		// .getDataSources();
		// List<DataSource> deletingDataSources = new ArrayList<DataSource>();
		// for (DataSource ds : currentDataSources) {
		// if (!datasources.contains(ds)) {
		// // remove from current list if user want to remove
		// deletingDataSources.add(ds);
		// } else {
		// // if user still keep this datasource, dont remove in the
		// // current list, ju st remove in the new list
		// datasources.remove(ds);
		// }
		// }
		// tomcat.removeDataSources(deletingDataSources);
		// tomcat.associateDataSources(datasources);
		// tomcatRepo.save(tomcat);
	}

	public List<CommonCode> getDBTypes() {
		return commonRepo
				.findByGropId(MeerkatConstants.CODE_GROP_DB_TYPE);
	}

	public List<DataSource> getDatasources(String name) {
		return datasourceRepo.findByName(name);
	}

	public DataSource save(DataSource ds) {
		return datasourceRepo.save(ds);
	}

	public List<DataSource> search(String keyword) {
		List<DataSource> datasourcesByName = datasourceRepo
				.findByNameContaining(keyword);
		return datasourcesByName;
	}
	
	public List<DataSource> listNotAssigned(int domainId) {
		return datasourceRepo.listNotAssigned(domainId);
	}
	
	public List<DataSource> listDomainLinked(int domainId) {
		return datasourceRepo.listDomainLinked(domainId);
	}
}
