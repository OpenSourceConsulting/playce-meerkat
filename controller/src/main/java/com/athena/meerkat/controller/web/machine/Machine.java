package com.athena.meerkat.controller.web.machine;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.athena.meerkat.controller.web.datagridserver.DatagridServer;
import com.athena.meerkat.controller.web.env.EnvironmentVariable;
import com.athena.meerkat.controller.web.tomcat.instance.TomcatInstance;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Represent the machine information
 * 
 * @author Tran Ho
 */
@Entity
@Table(name = "machine")
public class Machine {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int Id;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "host_name")
	private String hostName;
	@Column(name = "os_name", nullable = false)
	private String osName;
	@Column(name = "os_version", nullable = false)
	private String osVersion;
	@Column(name = "cpu_clock_speed", nullable = false)
	private float cpuClockSpeed;
	@Column(name = "cpu_clock_unit", nullable = false)
	private String cpuClockUnit;
	@Column(name = "memory_size", nullable = false)
	private float memorySize;
	@Column(name = "memory_size_unit", nullable = false)
	private String memorySizeUnit;
	@Column(name = "cpu_core", nullable = false)
	private int cpuCore;
	@Column(name = "os_arch", nullable = false)
	private String osArch;
	@Column(name = "origin_date", nullable = false)
	private Date originDate;
	@Column(name = "last_shutdown")
	private Date lastShutdown;
	@Column(name = "description")
	private String description;
	@Column(name = "disk_size")
	private float diskSize;
	@Column(name = "disk_size_unit")
	private String diskSizeUnit;
	@Column(name = "ssh_port")
	private int sshPort;
	@Column(name = "ssh_username")
	private String sshUsername;
	@Column(name = "ssh_password")
	private String sshPassword;
	@Column(name = "is_vm")
	private boolean isVm;
	@Column(name = "jvm")
	private String jvmVersion;
	@Column(name = "state")
	private int state;
	@Column(name = "ssh_ipaddr")
	private String sshIPAddr;

	@Column(name = "machine_server_type")
	private int machineServerType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "datagrid_server_id")
	@JsonManagedReference
	private DatagridServer datagridServer;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "machine")
	// using this annotation to prevent Infinite recursion json mapping
	@JsonManagedReference
	private Collection<Disk> disks;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "machine")
	@JsonManagedReference
	private Collection<Memory> memories;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "machine")
	@JsonManagedReference
	private Collection<NetworkInterface> networkInterfaces;

	@OneToMany(mappedBy = "machine")
	@JsonManagedReference
	private Collection<TomcatInstance> tomcatInstances;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "machine")
	private Collection<EnvironmentVariable> environmentVariables;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String host_name) {
		this.hostName = host_name;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String os_name) {
		this.osName = os_name;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String os_version) {
		this.osVersion = os_version;
	}

	public float getCpuClockSpeed() {
		return cpuClockSpeed;
	}

	public void setCpuClockSpeed(float cpu_clock_speed) {
		this.cpuClockSpeed = cpu_clock_speed;
	}

	public String getCpuClockUnit() {
		return cpuClockUnit;
	}

	public void setCpuClockUnit(String cpu_clock_unit) {
		this.cpuClockUnit = cpu_clock_unit;
	}

	public float getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(float memory_size) {
		this.memorySize = memory_size;
	}

	public String getMemoryUnit() {
		return memorySizeUnit;
	}

	public void setMemoryUnit(String memory_unit) {
		this.memorySizeUnit = memory_unit;
	}

	public int getCpuCore() {
		return cpuCore;
	}

	public void setCpuCore(int cpu_core) {
		this.cpuCore = cpu_core;
	}

	public String getOsArch() {
		return osArch;
	}

	public void setOsArch(String os_arch) {
		this.osArch = os_arch;
	}

	public Date getOriginDate() {
		return originDate;
	}

	public void setOriginDate(Date origin_date) {
		this.originDate = origin_date;
	}

	public Date getLastShutdown() {
		return lastShutdown;
	}

	public void setLastShutdown(Date last_shutdown) {
		this.lastShutdown = last_shutdown;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getDiskSize() {
		return diskSize;
	}

	public void setDiskSize(float disk_size) {
		this.diskSize = disk_size;
	}

	public String getDiskUnit() {
		return diskSizeUnit;
	}

	public void setDiskUnit(String disk_unit) {
		this.diskSizeUnit = disk_unit;
	}

	public int getSshPort() {
		return sshPort;
	}

	public void setSshPort(int ssh_port) {
		this.sshPort = ssh_port;
	}

	public String getSshUsername() {
		return sshUsername;
	}

	public void setSshUsername(String ssh_username) {
		this.sshUsername = ssh_username;
	}

	public String getSshPassword() {
		return sshPassword;
	}

	public void setSshPassword(String ssh_password) {
		this.sshPassword = ssh_password;
	}

	public String getJvmVersion() {
		return jvmVersion;
	}

	public void setJvmVersion(String jvm_version) {
		this.jvmVersion = jvm_version;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	/**
	 * Constructor with required information
	 * 
	 * @param mName
	 *            Name of machine
	 * @paramm mSSHIpaddr ssh ip address
	 * @param mSSHUserName
	 *            ssh username
	 * @param mSSHPassword
	 *            ssh password
	 * @param mSSHPort
	 *            ssh port
	 * @param mDescription
	 *            description of machine
	 */
	public Machine(String mName, String mSSHIpaddr, String mSSHUserName,
			String mSSHPassword, int mSSHPort, String mDescription) {
		name = mName;
		description = mDescription;
		sshIPAddr = mSSHIpaddr;
		sshUsername = mSSHUserName;
		sshPassword = mSSHPassword;
		sshPort = mSSHPort;
	}

	/**
	 * Default constructor: Do nothing
	 */
	public Machine() {

	}

	public Collection<Disk> getDisks() {
		return disks;
	}

	public void setDisks(Collection<Disk> disks) {
		this.disks = disks;
	}

	// memory
	public boolean addMemory(Memory m) {
		if (getMemories() != null) {
			return getMemories().add(m);
		}
		return false;
	}

	public boolean removeMemory(Memory m) {
		if (getMemories() != null) {
			return getMemories().remove(m);
		}
		return false;
	}

	// network interfaces
	public boolean addNetworkInterface(NetworkInterface ni) {
		if (getNetworkInterfaces() != null) {
			return getNetworkInterfaces().add(ni);
		}
		return false;
	}

	public boolean removeNetworkInterface(NetworkInterface ni) {
		if (getNetworkInterfaces() != null) {
			return getNetworkInterfaces().remove(ni);
		}
		return false;
	}

	// disks
	public boolean addDisk(Disk d) {
		if (disks != null) {
			return disks.add(d);
		}
		return false;
	}

	public boolean removeDisk(Disk d) {
		if (disks != null) {
			return disks.remove(d);
		}
		return false;
	}

	public String getSSHIPAddr() {
		return sshIPAddr;
	}

	public void setSSHIPAddr(String ssh_ipaddr) {
		this.sshIPAddr = ssh_ipaddr;
	}

	public boolean isVM() {
		return isVm;
	}

	public void isVM(boolean is_vm) {
		this.isVm = is_vm;
	}

	public int getId() {
		return Id;
	}

	public Collection<Memory> getMemories() {
		return memories;
	}

	public void setMemories(Collection<Memory> memories) {
		this.memories = memories;
	}

	public Collection<NetworkInterface> getNetworkInterfaces() {
		return networkInterfaces;
	}

	public void setNetworkInterfaces(
			Collection<NetworkInterface> networkInterfaces) {
		this.networkInterfaces = networkInterfaces;
	}

	public Collection<TomcatInstance> getTomcatInstances() {
		return tomcatInstances;
	}

	public void setTomcatInstances(Collection<TomcatInstance> tomcatInstances) {
		this.tomcatInstances = tomcatInstances;
	}

	// public DatagridServer getDatagridServer() {
	// return datagridServer;
	// }
	//
	// public void setDatagridServer(DatagridServer datagridServer) {
	// this.datagridServer = datagridServer;
	// }

	public Collection<EnvironmentVariable> getEnvironmentVariables() {
		return environmentVariables;
	}

	public void setEnvironmentVariables(
			Collection<EnvironmentVariable> environmentVariables) {
		this.environmentVariables = environmentVariables;
	}

	/**
	 * @return the datagridServer
	 */
	public DatagridServer getDatagridServer() {
		return datagridServer;
	}

	/**
	 * @param datagridServer
	 *            the datagridServer to set
	 */
	public void setDatagridServer(DatagridServer datagridServer) {
		this.datagridServer = datagridServer;
	}

	/**
	 * @return the machineServerType
	 */
	public int getMachineServerType() {
		return machineServerType;
	}

	/**
	 * @param machineServerType
	 *            the machineServerType to set
	 */
	public void setMachineServerType(int machineServerType) {
		this.machineServerType = machineServerType;
	}

	public int getTomcatInstanceNo() {
		if (tomcatInstances != null) {
			return tomcatInstances.size();
		}
		return 0;
	}
}
