package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.ClusteringConfiguration;
import com.athena.meerkat.controller.web.entities.ClusteringConfigurationVersion;
import com.athena.meerkat.controller.web.entities.DatagridServerGroup;
import com.athena.meerkat.controller.web.entities.TomcatDomain;

@Repository
public interface ClusteringConfigurationRepository extends JpaRepository<ClusteringConfiguration, Integer> {

	List<ClusteringConfiguration> findByName(String name);

	@Query(value = "select distinct ccv from DatagridServerGroup dsg inner join dsg.clusteringConfigurations cc inner join cc.clusteringConfigurationVersion ccv where dsg.id = :groupId group by ccv.version")
	List<ClusteringConfigurationVersion> getVersionsByServerGroup(@Param("groupId") int groupId);

	List<ClusteringConfiguration> findByDatagridServerGroupAndClusteringConfigurationVersion_Id(DatagridServerGroup group, Integer version);

	List<ClusteringConfiguration> findByDatagridServerGroup_IdAndClusteringConfigurationVersion_IdAndNameContaining(Integer groupId, int versionId,
			String keyword);
}
