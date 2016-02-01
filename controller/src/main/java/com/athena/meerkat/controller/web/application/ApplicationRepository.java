package com.athena.meerkat.controller.web.application;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends
		JpaRepository<Application, Integer> {
	List<Application> findByDisplayNameAndTomcat_Domain_Id(String name, int domainId);
}
