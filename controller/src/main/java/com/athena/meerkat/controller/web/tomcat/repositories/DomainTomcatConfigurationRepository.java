package com.athena.meerkat.controller.web.tomcat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatDomain;

@Repository
public interface DomainTomcatConfigurationRepository extends
		JpaRepository<DomainTomcatConfiguration, Integer> {
	DomainTomcatConfiguration findByTomcatDomain(TomcatDomain td);

	DomainTomcatConfiguration findByTomcatDomain_Id(int domainId);
}
