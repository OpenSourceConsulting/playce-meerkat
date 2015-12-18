package com.athena.dolly.controller.web.machine;

import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.OMGVMCID;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import com.athena.dolly.controller.DollyConstants;
import com.athena.dolly.controller.ServiceResult;
import com.athena.dolly.controller.ServiceResult.Status;
import com.athena.dolly.controller.common.SSHManager;

/**
 * This is an service that is used for processing machine information
 * 
 * @author Tran Ho
 */

@Service
public class MachineService implements InitializingBean {
	// public class MachineService {
	@Autowired
	private MachineRepository machineRepo;

	public MachineService() {

	}

	/**
	 * Add new machine. Based on the provided SSH information, machine info will
	 * be collected.
	 * 
	 * @param m
	 * @return
	 */
	public ServiceResult add(String name, String description, String sshAddr,
			int sshPort, String sshUserName, String sshPassword) {
		// check existing
		List<Machine> existingMachines = machineRepo.findByNameOrSshIPAddr(
				name, sshAddr);
		if (existingMachines != null) {
			if (existingMachines.size() > 0) {
				return new ServiceResult(Status.FAILED, "Duplicate machine");
			}
		}

		try {
			Machine m = new Machine(name, sshAddr, sshUserName, sshPassword,
					sshPort, description);
			ServiceResult result = getMachineInfoBySSH(m);
			if (result.getStatus() != Status.DONE) {
				return result;
			}
			machineRepo.save((Machine) result.getReturnedVal());
		} catch (Exception e) {
			return new ServiceResult(Status.ERROR, e.getMessage());
		}
		return new ServiceResult(Status.DONE, "Machine is added.");
	}

	/**
	 * Retrieve list of machines
	 * 
	 * @param page
	 *            : page number
	 * @param size
	 *            : Number of records will be retrieved
	 * @return ServiceStatus
	 */
	public ServiceResult retrieve(int page, int size) {
		Page<Machine> machines = machineRepo
				.findAll(new PageRequest(page, size));
		return new ServiceResult(Status.DONE, "Done", machines);
	}

	public ServiceResult retrieve(int id) {
		return new ServiceResult(Status.DONE, "", machineRepo.findOne(id));
	}

	public ServiceResult getList() {
		
		List<Machine> list = machineRepo.findAll();
		return new ServiceResult(Status.DONE, "", list);
	}

	/**
	 * Collect information of machine using SSH component
	 * 
	 * @param sshIPAddr
	 *            machine ip address
	 * @param sshPort
	 *            ssh port
	 * @param sshUserName
	 *            ssh username
	 * @param sshPassword
	 *            ssh password
	 */

	private ServiceResult getMachineInfoBySSH(Machine m) {
		String sshCommand = "";
		String sshOutput = "";

		// test connection
		SSHManager sshMng = new SSHManager(m.getSshUsername(),
				m.getSshPassword(), m.getSSHIPAddr(), "", m.getSshPort());

		if (sshMng.connect() != null) {
			return new ServiceResult(Status.FAILED,
					"SSH Connection is failed.", null);
		}
		// parse machine information
		// get hostname info
		sshCommand = "hostname";
		sshOutput = sshMng.sendCommand(sshCommand).trim();
		m.setHostName(sshOutput);
		// get os name and version
		sshCommand = "lsb_release -i | sed -e 's/^[a-z A-Z]*://'";
		sshOutput = sshMng.sendCommand(sshCommand).trim();
		m.setOsName(sshOutput);

		sshCommand = "lsb_release -r | sed -e 's/^[a-z A-Z]*://'";
		sshOutput = sshMng.sendCommand(sshCommand).trim();
		m.setOsVersion(sshOutput);
		// get number of cpu
		sshCommand = "nproc";
		sshOutput = sshMng.sendCommand(sshCommand).trim();
		m.setCpuCore(Integer.parseInt(sshOutput));
		// get cpu speed
		sshCommand = "lscpu | grep 'CPU MHz' | sed -e 's/^[a-z A-Z :]*//'";
		sshOutput = sshMng.sendCommand(sshCommand).trim();
		m.setCpuClockSpeed(Float.parseFloat(sshOutput));
		// cpu clock measurement unit
		m.setCpuClockUnit(DollyConstants.DEFAULT_CPU_MEASUREMENT_UNIT);
		// total memory
		sshCommand = "cat /proc/meminfo | grep 'MemTotal' | sed -e 's/^[a-z A-Z]*://' -e 's/[a-z A-Z]*$//'";
		sshOutput = sshMng.sendCommand(sshCommand).trim();
		m.setMemorySize(Float.parseFloat(sshOutput));
		// memory measurement unit
		sshCommand = "cat /proc/meminfo | grep 'MemTotal' | sed -e 's/^[a-z A-Z: 0-9]*[ ]//'";
		sshOutput = sshMng.sendCommand(sshCommand).trim();
		m.setMemoryUnit(sshOutput);
		// OS architecture
		sshCommand = "arch";
		sshOutput = sshMng.sendCommand(sshCommand).trim();
		m.setOsArch(sshOutput);
		// check whether it is VM or not.

		sshCommand = "virt-what";
		sshOutput = sshMng.sendCommand(sshCommand).trim();
		m.isVM(sshOutput.equals("") ? false : true);

		// check java version
		sshCommand = "java -version 2>&1 | grep version | sed -e 's/java version \"//' -e 's/.$//'";
		sshOutput = sshMng.sendCommand(sshCommand).trim();
		// get network interfaces info
		// sshCommand =
		// "ifconfig | sed  -e 's/[ ].*HWaddr/|/' -e 's/inet addr:/|/' -e 's/Bcast:.*Mask:/|/' -e 's/inet6 addr: /|/' -e 's/Scope:.*/|/'";
		// sshOutput = sshMng.sendCommand(sshCommand);
		// extract information

		return new ServiceResult(Status.DONE, "", m);
	}

	/**
	 * Edit machine information. Update name, description and using SSH account
	 * to re-collect machine info.
	 * 
	 * @param machineId
	 *            Machine Id need to be edited
	 * @param name
	 *            New name of machine
	 * @param description
	 *            New description
	 * 
	 * @param sshAddr
	 *            SSH IP address
	 * @param sshPort
	 *            SSH port
	 * 
	 * @param sshUserName
	 *            SSH username
	 * @param sshPassword
	 *            SSH password
	 * @return
	 */
	public ServiceResult edit(int machineId, String name, String description,
			String sshAddr, int sshPort, String sshUserName, String sshPassword) {
		boolean isChanged = false;
		Machine m = machineRepo.getOne(machineId);
		if (m == null) {
			return new ServiceResult(Status.FAILED, "Machine does not exist.");
		}
		if (!m.getName().equals(name)) {
			isChanged = true;
			m.setName(name);
		}
		if (!m.getDescription().equals(description)) {
			isChanged = true;
			m.setDescription(description);
		}
		if (m.getSshPort() != sshPort) {
			isChanged = true;
			m.setSshPort(sshPort);
		}
		if (!m.getSshUsername().equals(sshUserName)) {
			isChanged = true;
			m.setSshUsername(sshUserName);
		}
		if (!m.getSshPassword().equals(sshPassword)) {
			isChanged = true;
			m.setSshPassword(sshPassword);
		}
		if (!m.getSSHIPAddr().equals(sshAddr)) {
			m.setSSHIPAddr(sshAddr);
			try {
				return getMachineInfoBySSH(m);
			} catch (Exception e) {
				return new ServiceResult(Status.ERROR, e.getMessage());
			}
		}

		if (!isChanged) {
			return new ServiceResult(Status.FAILED, "Nothing is changed.");
		}
		return new ServiceResult(Status.DONE, "Updated", m);

	}

	/**
	 * Remove machine out of machine list
	 * 
	 * @param machineId
	 *            Machine Id
	 * @return Service Result
	 */
	public ServiceResult remove(int machineId) {
		Machine m = machineRepo.getOne(machineId);
		if (m == null) {
			return new ServiceResult(Status.FAILED, "Machine does not exist");
		}
		machineRepo.delete(m);
		return new ServiceResult(Status.DONE, "Removed");
	}

	/**
	 * Count machine in db
	 */
	public ServiceResult count() {
		return new ServiceResult(Status.DONE, "", machineRepo.count());
	}

	// for testing bean creation
	@Override
	public void afterPropertiesSet() throws Exception {
		System.err.println("=====================================");
		System.err.println("=====================================");
		System.err.println("=====================================");
		System.err.println("repo : " + machineRepo);
		System.err.println("=====================================");
		System.err.println("=====================================");
		System.err.println("=====================================");
	}
}
