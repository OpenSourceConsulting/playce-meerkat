package com.athena.meerkat.controller.web.resources.repositories;

import java.util.List;

import javax.persistence.NamedQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.TomcatInstance;

public interface DataSourceRepository extends
		JpaRepository<DataSource, Integer> {
	DataSource findByNameOrJdbcUrl(String name, String jdbcUrl);

	List<DataSource> findByName(String name);

	List<DataSource> findByJdbcUrl(String jdbcUrl);

	@Query("select ti from DataSource ds inner join ds.tomcatDomains td inner join td.tomcatInstances ti where  ds.id = ?1")
	List<TomcatInstance> getAssocicatedTomcatInstances(int datasourceId);

	@Query("select count(ti.Id) from DataSource ds inner join ds.tomcatDomains td inner join td.tomcatInstances ti where  ds.id = ?1")
	int getAssocicatedTomcatInstancesNo(int datasourceId);

}
