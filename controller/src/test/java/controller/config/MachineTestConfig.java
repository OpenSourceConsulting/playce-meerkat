package controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.athena.dolly.controller.web.machine.MachineRepository;
import com.athena.dolly.controller.web.machine.MachineService;

@Configuration
public class MachineTestConfig {

	@Bean
	public MachineService machineService() {
		MachineService service = new MachineService();
		return service;
	}
}
