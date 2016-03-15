package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.TomcatApplication;


@Repository
public interface ApplicationRepository extends
		JpaRepository<TomcatApplication, Integer> {
	//List<TomcatApplication> findByDisplayNameAndTomcat_Domain_Id(String name, int domainId);
}
