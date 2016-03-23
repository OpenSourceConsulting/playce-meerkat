package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatDomain;

@Repository
public interface TomcatConfigFileRepository extends
		JpaRepository<TomcatConfigFile, Integer> {
	List<TomcatConfigFile> findByTomcatDomainAndFileTypeCdId(
			TomcatDomain tomcatDomain, int fileTypeCdId);

}
