package com.athena.meerkat.controller.web.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClusteringConfigurationRepository extends
		JpaRepository<ClusteringConfiguration, Integer> {
	List<ClusteringConfiguration> findByDomain_IdAndRevision(int Id,
			int revision);

	List<ClusteringConfiguration> findByName(String name);

}
