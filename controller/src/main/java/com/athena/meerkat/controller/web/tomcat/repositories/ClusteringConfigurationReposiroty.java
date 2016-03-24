package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.ClusteringConfiguration;
import com.athena.meerkat.controller.web.entities.ClusteringConfigurationVersion;
import com.athena.meerkat.controller.web.entities.TomcatDomain;

@Repository
public interface ClusteringConfigurationReposiroty extends
		JpaRepository<ClusteringConfiguration, Integer> {

	@Query("select distinct ccv from TomcatDomain td inner join td.clusteringConfigurations cc inner join cc.clusteringConfigurationVersion ccv where td.id = ?1")
	List<ClusteringConfigurationVersion> getVersions(int domainId);

	List<ClusteringConfiguration> findByTomcatDomainAndClusteringConfigurationVersion_Version(
			TomcatDomain td, Integer versionId);
}
