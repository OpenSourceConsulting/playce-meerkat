package com.athena.meerkat.controller.web.env;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvironmentVariableRepository extends
		JpaRepository<EnvironmentVariable, Integer> {

}
