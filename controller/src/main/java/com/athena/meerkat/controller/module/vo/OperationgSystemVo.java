/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2014. 3. 31.		First Draft.
 */
package com.athena.meerkat.controller.module.vo;

import java.io.Serializable;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class OperationgSystemVo implements Serializable {

	private static final long serialVersionUID = 2801088346407306730L;
	
	private String name;
	private String version;
	private String arch;
	private Double systemLoadAverage;
	private Integer availableProcessors;
	
	private Long freePhysicalMemory;
	private Long processCpuTime;
	private Long committedVirtualMemorySize;
	private Long freeSwapSpaceSize;
	private Long totalPhysicalMemorySize;
	private Long totalSwapSpaceSize;
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the arch
	 */
	public String getArch() {
		return arch;
	}
	/**
	 * @param arch the arch to set
	 */
	public void setArch(String arch) {
		this.arch = arch;
	}
	
	/**
	 * @return the systemLoadAverage
	 */
	public Double getSystemLoadAverage() {
		return systemLoadAverage;
	}
	/**
	 * @param systemLoadAverage the systemLoadAverage to set
	 */
	public void setSystemLoadAverage(Double systemLoadAverage) {
		this.systemLoadAverage = systemLoadAverage;
	}
	
	/**
	 * @return the availableProcessors
	 */
	public int getAvailableProcessors() {
		return availableProcessors;
	}
	/**
	 * @param availableProcessors the availableProcessors to set
	 */
	public void setAvailableProcessors(int availableProcessors) {
		this.availableProcessors = availableProcessors;
	}
		
	
	
	/**
	 * @return the freePhysicalMemory
	 */
	public Long getFreePhysicalMemory() {
		return freePhysicalMemory;
	}
	/**
	 * @param freePhysicalMemory the freePhysicalMemory to set
	 */
	public void setFreePhysicalMemory(Long freePhysicalMemory) {
		this.freePhysicalMemory = freePhysicalMemory;
	}
	
	
	/**
	 * @return the processCpuTime
	 */
	public Long getProcessCpuTime() {
		return processCpuTime;
	}
	/**
	 * @param processCpuTime the processCpuTime to set
	 */
	public void setProcessCpuTime(Long processCpuTime) {
		this.processCpuTime = processCpuTime;
	}
	
	
	/**
	 * @return the committedVirtualMemorySize
	 */
	public Long getCommittedVirtualMemorySize() {
		return committedVirtualMemorySize;
	}
	/**
	 * @param committedVirtualMemorySize the committedVirtualMemorySize to set
	 */
	public void setCommittedVirtualMemorySize(Long committedVirtualMemorySize) {
		this.committedVirtualMemorySize = committedVirtualMemorySize;
	}
	
	
	/**
	 * @return the freeSwapSpaceSize
	 */
	public Long getFreeSwapSpaceSize() {
		return freeSwapSpaceSize;
	}
	/**
	 * @param freeSwapSpaceSize the freeSwapSpaceSize to set
	 */
	public void setFreeSwapSpaceSize(Long freeSwapSpaceSize) {
		this.freeSwapSpaceSize = freeSwapSpaceSize;
	}
	
	
	/**
	 * @return the totalPhysicalMemorySize
	 */
	public Long getTotalPhysicalMemorySize() {
		return totalPhysicalMemorySize;
	}
	/**
	 * @param totalPhysicalMemorySize the totalPhysicalMemorySize to set
	 */
	public void setTotalPhysicalMemorySize(Long totalPhysicalMemorySize) {
		this.totalPhysicalMemorySize = totalPhysicalMemorySize;
	}
	
	
	/**
	 * @return the totalSwapSpaceSize
	 */
	public Long getTotalSwapSpaceSize() {
		return totalSwapSpaceSize;
	}
	/**
	 * @param totalSwapSpaceSize the totalSwapSpaceSize to set
	 */
	public void setTotalSwapSpaceSize(Long totalSwapSpaceSize) {
		this.totalSwapSpaceSize = totalSwapSpaceSize;
	}

	
}
//end of MemoryVo.java