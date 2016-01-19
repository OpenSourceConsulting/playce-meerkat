package com.athena.meerkat.controller.web.env;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentVariableService {
	@Autowired
	private EnvironmentVariableRepository envRepo;
}
