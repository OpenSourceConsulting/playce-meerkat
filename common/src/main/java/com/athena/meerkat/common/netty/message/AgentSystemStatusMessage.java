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

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class AgentSystemStatusMessage extends AbstractMessage {

	private static final long serialVersionUID = 1L;

	/**
	 * The percentage of time that the CPU or CPUs were not processing any
	 * commands and the system did not have an outstanding disk I/O request.
	 */
	private String idleCpu;
	/**
	 * Percentage of CPU utilization while executing at all(system, kernel,
	 * user, application).
	 */
	private String combinedCpu;
	/** Total system memory */
	private String totalMem;
	/** Total free system memory */
	private String freeMem;
	/** Total used system memory */
	private String usedMem;
	/** Percentage of Disk utilization per mounted nodes */
	private String diskUsage;
	/** Whole disks total size */
	private String totalDisk;
	/** whole disks used size */
	private String usedDisk;

	public AgentSystemStatusMessage() {
		super(MessageType.SYSTEM_STATUS);
	}

	/**
	 * @return the idleCpu
	 */
	public String getIdleCpu() {
		return idleCpu;
	}

	/**
	 * @param idleCpu
	 *            the idleCpu to set
	 */
	public void setIdleCpu(String idleCpu) {
		this.idleCpu = idleCpu;
	}

	/**
	 * @return the combinedCpu
	 */
	public String getCombinedCpu() {
		return combinedCpu;
	}

	/**
	 * @param combinedCpu
	 *            the combinedCpu to set
	 */
	public void setCombinedCpu(String combinedCpu) {
		this.combinedCpu = combinedCpu;
	}

	/**
	 * @return the totalMem
	 */
	public String getTotalMem() {
		return totalMem;
	}

	/**
	 * @param totalMem
	 *            the totalMem to set
	 */
	public void setTotalMem(String totalMem) {
		this.totalMem = totalMem;
	}

	/**
	 * @return the freeMem
	 */
	public String getFreeMem() {
		return freeMem;
	}

	/**
	 * @param freeMem
	 *            the freeMem to set
	 */
	public void setFreeMem(String freeMem) {
		this.freeMem = freeMem;
	}

	/**
	 * @return the usedMem
	 */
	public String getUsedMem() {
		return usedMem;
	}

	/**
	 * @param usedMem
	 *            the usedMem to set
	 */
	public void setUsedMem(String usedMem) {
		this.usedMem = usedMem;
	}

	/**
	 * @return the diskUsage
	 */
	public String getDiskUsage() {
		return diskUsage;
	}

	/**
	 * @param diskUsage
	 *            the diskUsage to set
	 */
	public void setDiskUsage(String diskUsage) {
		this.diskUsage = diskUsage;
	}

	/**
	 * @return the totalDisk
	 */
	public String getTotalDisk() {
		return totalDisk;
	}

	/**
	 * @param totalDisk
	 *            the totalDisk to set
	 */
	public void setTotalDisk(String totalDisk) {
		this.totalDisk = totalDisk;
	}

	/**
	 * @return the usedDisk
	 */
	public String getUsedDisk() {
		return usedDisk;
	}

	/**
	 * @param usedDisk
	 *            the usedDisk to set
	 */
	public void setUsedDisk(String usedDisk) {
		this.usedDisk = usedDisk;
	}

}
// end of AgentSystemStatus.java