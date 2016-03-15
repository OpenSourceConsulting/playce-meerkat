package controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.athena.meerkat.controller.web.resources.repositories.ServerRepository;
import com.athena.meerkat.controller.web.resources.services.ServerService;

@Configuration
public class MachineTestConfig {

	@Bean
	public ServerService machineService() {
		ServerService service = new ServerService();
		return service;
	}
}
