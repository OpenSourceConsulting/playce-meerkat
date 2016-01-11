/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 7. 18.		First Draft.
 */
package com.athena.meerkat.common.netty.message;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class AgentInitialInfoMessage extends AbstractMessage {

	private static final long serialVersionUID = 1L;

	/** mac address */
	private Map<String, String> macAddrMap;
	/** os.name */
	private String osName;
	/** os.arch */
	private String osArch;
	/** os.version */
	private String osVersion;
	/** cpu clock */
	private int cpuClock;
	/** cpu num */
	private int cpuNum;
	/** cpu model */
	private String cpuModel;
	/** cpu vendor */
	private String cpuVendor;
	/** memory capacity */
	private long memSize;
	/** ip address */
	private String ipAddr;
	/** host name */
	private String hostName;
	/** package installed Y/N*/
	private String packageCollected = "Y";
	/** software installed Y/N*/
	private String softwareInstalled = "Y";
	/** machine id */
	private String machineId = null;
	
	public AgentInitialInfoMessage() {
		super(MessageType.INITIAL_INFO);
	}

	/**
	 * @return the macAddrMap
	 */
	public Map<String, String> getMacAddrMap() {
		return macAddrMap;
	}

	/**
	 * @param macAddrMap the macAddrMap to set
	 */
	public void setMacAddrMap(Map<String, String> macAddrMap) {
		this.macAddrMap = macAddrMap;
	}
	
	public String getMacAddr(String ipAddr) {
		return this.macAddrMap.get(ipAddr);
	}
	
	public void putMacAddr(String ipAddr, String macAddr) {
		if (this.macAddrMap == null) {
			this.macAddrMap = new HashMap<String, String>();
		}
		
		this.macAddrMap.put(ipAddr, macAddr);
	}

	/**
	 * @return the osName
	 */
	public String getOsName() {
		return osName;
	}

	/**
	 * @param osName the osName to set
	 */
	public void setOsName(String osName) {
		this.osName = osName;
	}

	/**
	 * @return the osArch
	 */
	public String getOsArch() {
		return osArch;
	}

	/**
	 * @param osArch the osArch to set
	 */
	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}

	/**
	 * @return the osVersion
	 */
	public String getOsVersion() {
		return osVersion;
	}

	/**
	 * @param osVersion the osVersion to set
	 */
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	/**
	 * @return the cpuClock
	 */
	public int getCpuClock() {
		return cpuClock;
	}

	/**
	 * @param cpuClock the cpuClock to set
	 */
	public void setCpuClock(int cpuClock) {
		this.cpuClock = cpuClock;
	}

	/**
	 * @return the cpuNum
	 */
	public int getCpuNum() {
		return cpuNum;
	}

	/**
	 * @param cpuNum the cpuNum to set
	 */
	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}

	/**
	 * @return the cpuModel
	 */
	public String getCpuModel() {
		return cpuModel;
	}

	/**
	 * @param cpuModel the cpuModel to set
	 */
	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}

	/**
	 * @return the cpuVendor
	 */
	public String getCpuVendor() {
		return cpuVendor;
	}

	/**
	 * @param cpuVendor the cpuVendor to set
	 */
	public void setCpuVendor(String cpuVendor) {
		this.cpuVendor = cpuVendor;
	}

	/**
	 * @return the memSize
	 */
	public long getMemSize() {
		return memSize;
	}

	/**
	 * @param memSize the memSize to set
	 */
	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}

	/**
	 * @return the ipAddr
	 */
	public String getIpAddr() {
		return ipAddr;
	}

	/**
	 * @param ipAddr the ipAddr to set
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the packageCollected
	 */
	public String getPackageCollected() {
		return packageCollected;
	}

	/**
	 * @param packageCollected the packageCollected to set
	 */
	public void setPackageCollected(String packageCollected) {
		this.packageCollected = packageCollected;
	}

	/**
	 * @return the softwareInstalled
	 */
	public String getSoftwareInstalled() {
		return softwareInstalled;
	}

	/**
	 * @param softwareInstalled the softwareInstalled to set
	 */
	public void setSoftwareInstalled(String softwareInstalled) {
		this.softwareInstalled = softwareInstalled;
	}

	/**
	 * @return the machineId
	 */
	public String getMachineId() {
		return machineId;
	}

	/**
	 * @param machineId the machineId to set
	 */
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	
}
//end of AgentInitialInfo.java