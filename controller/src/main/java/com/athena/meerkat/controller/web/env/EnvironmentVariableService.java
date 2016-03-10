package com.athena.meerkat.controller.web.env;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.machine.Machine;
import com.athena.meerkat.controller.web.revision.Revision;

@Service
public class EnvironmentVariableService {
	@Autowired
	private EnvironmentVariableRepository envRepo;
	private EnvironmentVariableValueRepository envValueRepo;

	public List<EnvironmentVariableValue> getByMachine(Machine machine) {
		return envValueRepo.findByMachine(machine);
	}

	public List<Revision> getEVRevisions(Machine machine) {
		return machine.getRevisions(MeerkatConstants.REVISION_ENV_CONFIG_TYPE);
	}
}
