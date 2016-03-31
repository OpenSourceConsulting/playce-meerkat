package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatInstConfig;

@Repository
public interface TomcatInstConfigRepository extends
		JpaRepository<TomcatInstConfig, Integer> {
	List<TomcatInstConfig> findByTomcatInstance_Id(int id);
}
