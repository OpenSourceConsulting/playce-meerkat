package com.athena.meerkat.controller.web.resources.repositories;

import java.util.List;

import javax.persistence.NamedQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.TomcatInstance;

public interface DataSourceRepository extends
		JpaRepository<DataSource, Integer> {
	DataSource findByNameContainingOrJdbcUrlContaining(String name,
			String jdbcUrl);

	List<DataSource> findByName(String name);

	List<DataSource> findByNameContaining(String name);

	List<DataSource> findByJdbcUrl(String jdbcUrl);

	@Query("select ti from DataSource ds inner join ds.tomcatDomains td inner join td.tomcatInstances ti where  ds.id = ?1")
	List<TomcatInstance> getAssocicatedTomcatInstances(int datasourceId);

	@Query("select count(ti.id) from DataSource ds inner join ds.tomcatDomains td inner join td.tomcatInstances ti where  ds.id = ?1")
	int getAssocicatedTomcatInstancesNo(int datasourceId);

	@Query("select a from DataSource a where a.id not in (select t.datasourceId from TomcatDomainDatasource t where t.tomcatDomainId = ?1)")
	List<DataSource> listNotAssigned(int domainId);
	
	@Query("select a from DataSource a inner join a.tomcatDomains t where t.id = ?1")
	List<DataSource> listDomainLinked(int domainId);

	@Query("select a from DataSource a where a.id in (select t.datasourceId from TomcatDomainDatasource t where t.tomcatDomainId = ?1)")
	List<DataSource> getDatasourcesByDomainId(int domainId);

}
