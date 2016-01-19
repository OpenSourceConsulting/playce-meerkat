package com.athena.dolly.controller.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;




import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.web.machine.Machine;
import com.athena.meerkat.controller.web.machine.MachineRepository;
import com.athena.meerkat.controller.web.machine.MachineService;

//@ContextConfiguration("file:./src/test/resources/TestContext.xml")
@ComponentScan(basePackages = { "com.athena.dolly.controller" })
//@RunWith(SpringJUnit4ClassRunner.class)
public class MachineTest {
	// @Autowired
	// private MachineService machineService;
	//
	// @Before
	// public void beforeTest() {
	// // service = new MachineService();
	// }
	@Autowired
	private MachineRepository machineRepo;

	// @Test
	// public void testAddMachine() {
	// // get number of record before adding
	// int beforeTestCount = (Integer) machineService.count().getReturnedVal();
	//
	// String testMachineName = "Testing machine";
	// String testMachineDescription = "This is a test machine";
	// String sshIPAddr = "192.168.0.88";
	// String sshUserName = "root";
	// String sshPassword = "test123";
	// int sshPort = 22;
	// ServiceResult result = machineService.add(testMachineName,
	// testMachineDescription, sshIPAddr, sshPort, sshUserName,
	// sshPassword);
	// Machine m = (Machine) result.getReturnedVal();
	// if (result.getStatus() != Status.DONE || m == null) {
	// assertTrue(false);
	// } else {
	// int afterTestCount = (Integer) machineService.count()
	// .getReturnedVal();
	// if (afterTestCount != beforeTestCount + 1) {
	// assertTrue(false);
	// } else {
	// assertEquals(testMachineName, m.getName());
	// assertEquals(testMachineDescription, m.getDescription());
	// assertEquals(sshIPAddr, m.getSSHIPAddr());
	// assertEquals(sshUserName, m.getSshUsername());
	// assertEquals(sshPassword, m.getSshPassword());
	// assertEquals(sshPort, m.getSshPort());
	// }
	// }
	// // test add duplicated machine
	// result = machineService.add(testMachineName, testMachineDescription,
	// sshIPAddr, sshPort, sshUserName, sshPassword);
	// assertTrue(result.getStatus() == Status.FAILED);
	// // remove the added machine
	// machineService.remove(m.getId());
	// }

	@Test
	public void test1() {
		//List<Machine> list = machineRepo.findAll();
	}

}
