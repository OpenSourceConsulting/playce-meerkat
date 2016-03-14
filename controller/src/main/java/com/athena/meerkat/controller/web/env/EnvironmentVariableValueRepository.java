package com.athena.meerkat.controller.web.env;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athena.meerkat.controller.web.machine.Machine;

public interface EnvironmentVariableValueRepository extends
		JpaRepository<EnvironmentVariableValue, Integer> {
	List<EnvironmentVariableValue> findByMachine(Machine machine);
}
