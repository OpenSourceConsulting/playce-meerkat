package com.athena.meerkat.controller.web.tomcat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.web.tomcat.repositories.EnvironmentVariableRepository;

@Service
public class EnvironmentVariableService {
	@Autowired
	private EnvironmentVariableRepository envRepo;
}
