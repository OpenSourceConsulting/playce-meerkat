package com.athena.meerkat.controller.web.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * Represent the machine information
 * 
 * @author Tran Ho
 */
@Entity
@Table(name = "server")
public class Server implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	@Column(name = "is_vm")
	private boolean isVm;
	@Column(name = "jvm")
	private String jvmVersion;
	@Column(name = "ssh_port")
	private int sshPort;
	@Column(name = "ssh_ipaddr")
	private String sshIpaddr;
	@Column(name = "state")
	private int state;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "server")
	@JsonManagedReference
	private Collection<NetworkInterface> networkInterfaces;

	@OneToMany(mappedBy = "server", fetch = FetchType.LAZY)
	@JsonManagedReference
	private Collection<TomcatInstance> tomcatInstances;

	@OneToMany(mappedBy = "server", fetch = FetchType.LAZY)
	@JsonManagedReference
	private Collection<SshAccount> sshAccounts;

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
	public Server(String mName, String mSSHIpaddr, String mSSHUserName,
			String mSSHPassword, int mSSHPort, String mDescription) {
	}

	/**
	 * Default constructor: Do nothing
	 */
	public Server() {

	}

	public Server(int _id, String _name) {
		Id = _id;
		name = _name;
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

	public boolean isVM() {
		return isVm;
	}

	public void isVM(boolean is_vm) {
		this.isVm = is_vm;
	}

	public int getId() {
		return Id;
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

	public int getTomcatInstanceNo() {
		if (tomcatInstances != null) {
			return tomcatInstances.size();
		}
		return 0;
	}

	public Collection<SshAccount> getSshAccounts() {
		return sshAccounts;
	}

	public void setSshAccounts(Collection<SshAccount> sshAccounts) {
		this.sshAccounts = sshAccounts;
	}

	public int getSshPort() {
		return sshPort;
	}

	public void setSshPort(int sshPort) {
		this.sshPort = sshPort;
	}

	public String getSshIpaddr() {
		return sshIpaddr;
	}

	public void setSshIpaddr(String sshIpaddr) {
		this.sshIpaddr = sshIpaddr;
	}

}
