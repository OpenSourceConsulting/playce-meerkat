package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.ClusteringConfigurationVersion;

@Repository
public interface ClusteringConfigurationVersionRepository extends
		JpaRepository<ClusteringConfigurationVersion, Integer> {
	@Query(value = "select  v.* from clustering_configuration cc join clustering_conf_version v where cc.domain_id = ?1 and cc.clustering_conf_version_id = v.id order by v.version desc limit 1", nativeQuery = true)
	List<ClusteringConfigurationVersion> findFirstClusteringVersionByDomainId(
			int domainId);

	@Query(value = "select  v.* from clustering_configuration cc join clustering_conf_version v where cc.datagrid_server_group_id = ?1 and cc.clustering_conf_version_id = v.id order by v.version desc limit 1", nativeQuery = true)
	List<ClusteringConfigurationVersion> findFirstClusteringVersionByDatagridGroupId(
			int groupId);

}
