/**
 * 
 */
package com.athena.meerkat.controller.web.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Tran
 *
 */
@Entity
@Table
public class Server {
	@Id
	private int id;
	private String name;
	private String hostName;
	private int state;
	private String osName;
	private String osVersion;
	private Float cpuClockSpeed;
	private String cpuClockUnit;
	private int cpuCore;
	private String osArch;
	private java.sql.Date originDate;
	private java.sql.Date lastShutdown;
	private String description;
	private boolean isVm;
	private String jvm;
	private Double diskSize;
	private String diskSizeUnit;
	private Double memorySize;
	private String memorySizeUnit;
	private int datagridServerGroupId;

	/**
	 * 
	 */
	public Server() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the osName
	 */
	public String getOsName() {
		return osName;
	}

	/**
	 * @param osName
	 *            the osName to set
	 */
	public void setOsName(String osName) {
		this.osName = osName;
	}

	/**
	 * @return the osVersion
	 */
	public String getOsVersion() {
		return osVersion;
	}

	/**
	 * @param osVersion
	 *            the osVersion to set
	 */
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	/**
	 * @return the cpuClockSpeed
	 */
	public Float getCpuClockSpeed() {
		return cpuClockSpeed;
	}

	/**
	 * @param cpuClockSpeed
	 *            the cpuClockSpeed to set
	 */
	public void setCpuClockSpeed(Float cpuClockSpeed) {
		this.cpuClockSpeed = cpuClockSpeed;
	}

	/**
	 * @return the cpuClockUnit
	 */
	public String getCpuClockUnit() {
		return cpuClockUnit;
	}

	/**
	 * @param cpuClockUnit
	 *            the cpuClockUnit to set
	 */
	public void setCpuClockUnit(String cpuClockUnit) {
		this.cpuClockUnit = cpuClockUnit;
	}

	/**
	 * @return the cpuCore
	 */
	public int getCpuCore() {
		return cpuCore;
	}

	/**
	 * @param cpuCore
	 *            the cpuCore to set
	 */
	public void setCpuCore(int cpuCore) {
		this.cpuCore = cpuCore;
	}

	/**
	 * @return the osArch
	 */
	public String getOsArch() {
		return osArch;
	}

	/**
	 * @param osArch
	 *            the osArch to set
	 */
	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}

	/**
	 * @return the originDate
	 */
	public java.sql.Date getOriginDate() {
		return originDate;
	}

	/**
	 * @param originDate
	 *            the originDate to set
	 */
	public void setOriginDate(java.sql.Date originDate) {
		this.originDate = originDate;
	}

	/**
	 * @return the lastShutdown
	 */
	public java.sql.Date getLastShutdown() {
		return lastShutdown;
	}

	/**
	 * @param lastShutdown
	 *            the lastShutdown to set
	 */
	public void setLastShutdown(java.sql.Date lastShutdown) {
		this.lastShutdown = lastShutdown;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the sshPort
	 */


	/**
	 * @return the isVm
	 */
	public boolean getIsVm() {
		return isVm;
	}

	/**
	 * @param isVm
	 *            the isVm to set
	 */
	public void setIsVm(boolean isVm) {
		this.isVm = isVm;
	}

	/**
	 * @return the jvm
	 */
	public String getJvm() {
		return jvm;
	}

	/**
	 * @param jvm
	 *            the jvm to set
	 */
	public void setJvm(String jvm) {
		this.jvm = jvm;
	}

	

	/**
	 * @return the diskSize
	 */
	public Double getDiskSize() {
		return diskSize;
	}

	/**
	 * @param diskSize
	 *            the diskSize to set
	 */
	public void setDiskSize(Double diskSize) {
		this.diskSize = diskSize;
	}

	/**
	 * @return the diskSizeUnit
	 */
	public String getDiskSizeUnit() {
		return diskSizeUnit;
	}

	/**
	 * @param diskSizeUnit
	 *            the diskSizeUnit to set
	 */
	public void setDiskSizeUnit(String diskSizeUnit) {
		this.diskSizeUnit = diskSizeUnit;
	}

	/**
	 * @return the memorySize
	 */
	public Double getMemorySize() {
		return memorySize;
	}

	/**
	 * @param memorySize
	 *            the memorySize to set
	 */
	public void setMemorySize(Double memorySize) {
		this.memorySize = memorySize;
	}

	/**
	 * @return the memorySizeUnit
	 */
	public String getMemorySizeUnit() {
		return memorySizeUnit;
	}

	/**
	 * @param memorySizeUnit
	 *            the memorySizeUnit to set
	 */
	public void setMemorySizeUnit(String memorySizeUnit) {
		this.memorySizeUnit = memorySizeUnit;
	}

	/**
	 * @return the datagridServerGroupId
	 */
	public int getDatagridServerGroupId() {
		return datagridServerGroupId;
	}

	/**
	 * @param datagridServerGroupId
	 *            the datagridServerGroupId to set
	 */
	public void setDatagridServerGroupId(int datagridServerGroupId) {
		this.datagridServerGroupId = datagridServerGroupId;
	}

}
