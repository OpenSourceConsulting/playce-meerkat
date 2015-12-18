package com.athena.dolly.controller.web.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ApplicationController {

	@Autowired
	private ApplicationService service;
}
