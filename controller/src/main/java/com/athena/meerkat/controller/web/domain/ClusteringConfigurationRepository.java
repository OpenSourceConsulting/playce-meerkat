package com.athena.meerkat.controller.web.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClusteringConfigurationRepository extends
		JpaRepository<ClusteringConfiguration, Integer> {
	List<ClusteringConfiguration> findByDomainAndRevision(int domain,
			int revision);

}
