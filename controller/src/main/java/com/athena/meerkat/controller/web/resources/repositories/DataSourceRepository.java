package com.athena.meerkat.controller.web.resources.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.DataSource;

public interface DataSourceRepository extends
		JpaRepository<DataSource, Integer> {
	DataSource findByNameOrJdbcUrl(String name, String jdbcUrl);

	List<DataSource> findByName(String name);

	List<DataSource> findByJdbcUrl(String jdbcUrl);
	// @NamedQuery(query="select d in ")
	// List<DataSource> findByTomcat_Id(int tomcatId);
}
