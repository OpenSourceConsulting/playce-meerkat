package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.resources.entities.Server;
import com.athena.meerkat.controller.web.tomcat.entities.EnvironmentVariable;

@Repository
public interface EnvironmentVariableRepository extends
		JpaRepository<EnvironmentVariable, Integer> {

	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param machineId
	 * @return
	 */
	//List<EnvironmentVariable> findByMachine(Machine machine);

}
