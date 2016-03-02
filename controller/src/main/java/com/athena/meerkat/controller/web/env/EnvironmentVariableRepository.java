package com.athena.meerkat.controller.web.env;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.machine.Machine;

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
	List<EnvironmentVariable> findByMachine(Machine machine);

}
