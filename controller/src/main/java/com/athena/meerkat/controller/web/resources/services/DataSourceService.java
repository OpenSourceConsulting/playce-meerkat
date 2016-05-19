package com.athena.meerkat.controller.web.resources.services;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.MeerkatConstants;
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

	public void delete(DataSource ds) {
		datasourceRepo.delete(ds);
	}

	public boolean testConnection(String jdbcUrl, String userName, String password, String dbType) {
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
			Connection conn = (Connection) DriverManager.getConnection(jdbcUrl, userName, password);
			if (conn == null) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			return false;
		}

	}

	public List<DataSource> getAll() {
		List<DataSource> list = datasourceRepo.findAll();
		for (DataSource ds : list) {
			ds.setTomcatInstancesNo(this.getAssocicatedTomcatInstancesNo(ds.getId()));
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

	public void associateTomcat(TomcatInstance tomcat, List<DataSource> datasources) {
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
		return commonRepo.findByGropId(MeerkatConstants.CODE_GROP_DB_TYPE);
	}

	public List<DataSource> getDatasources(String name) {
		return datasourceRepo.findByName(name);
	}

	public DataSource save(DataSource ds) {
		return datasourceRepo.save(ds);
	}

	public List<DataSource> search(String keyword) {
		List<DataSource> datasourcesByName = datasourceRepo.findByNameContaining(keyword);
		return datasourcesByName;
	}

	public List<DataSource> listNotAssigned(int domainId) {
		return datasourceRepo.listNotAssigned(domainId);
	}

}
