package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.ClusteringConfiguration;
import com.athena.meerkat.controller.web.entities.ClusteringConfigurationVersion;
import com.athena.meerkat.controller.web.entities.TomcatDomain;

@Repository
public interface ClusteringConfigurationReposiroty extends
		JpaRepository<ClusteringConfiguration, Integer> {

	@Query(value = "select distinct ccv from TomcatDomain td inner join td.clusteringConfigurations cc inner join cc.clusteringConfigurationVersion ccv where td.id = :domainId group by ccv.version")
	List<ClusteringConfigurationVersion> getVersions(
			@Param("domainId") int domainId);

	List<ClusteringConfiguration> findByTomcatDomainAndClusteringConfigurationVersion_Id(
			TomcatDomain td, Integer versionId);

	List<ClusteringConfiguration> findByName(String name);

	List<ClusteringConfiguration> findByTomcatDomain_IdAndClusteringConfigurationVersion_IdAndNameContaining(
			int domainId, int versionId, String keyword);
}
