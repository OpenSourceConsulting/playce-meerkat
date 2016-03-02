package com.athena.meerkat.controller.web.env;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.web.machine.Machine;

@Service
public class EnvironmentVariableService {
	@Autowired
	private EnvironmentVariableRepository envRepo;
	
	public List<EnvironmentVariable> getByMachine(Machine machine){
		return envRepo.findByMachine(machine);
	}
}
