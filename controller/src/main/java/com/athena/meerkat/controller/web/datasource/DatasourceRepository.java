package com.athena.meerkat.controller.web.datasource;

import java.util.List;

import javax.persistence.NamedQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatasourceRepository extends
		JpaRepository<Datasource, Integer> {
	Datasource findByNameOrJdbcUrl(String name, String jdbcUrl);
	List<Datasource> findByName(String name);
	List<Datasource> findByJdbcUrl(String jdbcUrl);
	//@NamedQuery(query="select d in ")
	//List<Datasource> findByTomcat_Id(int tomcatId);
}
