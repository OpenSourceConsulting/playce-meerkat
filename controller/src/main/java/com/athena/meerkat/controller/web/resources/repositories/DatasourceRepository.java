package com.athena.meerkat.controller.web.resources.repositories;

import java.util.List;

import javax.persistence.NamedQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.resources.entities.Datasource;

@Repository
public interface DatasourceRepository extends
		JpaRepository<Datasource, Integer> {
	Datasource findByNameOrJdbcUrl(String name, String jdbcUrl);
	List<Datasource> findByName(String name);
	List<Datasource> findByJdbcUrl(String jdbcUrl);
	//@NamedQuery(query="select d in ")
	//List<Datasource> findByTomcat_Id(int tomcatId);
}
