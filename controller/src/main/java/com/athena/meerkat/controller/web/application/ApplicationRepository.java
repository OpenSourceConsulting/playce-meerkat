package com.athena.meerkat.controller.web.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends
		JpaRepository<Application, Integer> {

}
