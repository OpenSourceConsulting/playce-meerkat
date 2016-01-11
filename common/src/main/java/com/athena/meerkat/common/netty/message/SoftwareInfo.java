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
 * Sang-cheon Park	2014. 9. 29.		First Draft.
 */
package com.athena.meerkat.common.netty.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class SoftwareInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String version;
	private String startWorkingDir;
	private String startCommand;
	private String startArgs;
	private String stopWorkingDir;
	private String stopCommand;
	private String stopArgs;
	private List<String> installLocations;
	private List<ConfigInfo> configInfoList;

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
	 * @return the startWorkingDir
	 */
	public String getStartWorkingDir() {
		return startWorkingDir;
	}

	/**
	 * @param startWorkingDir the startWorkingDir to set
	 */
	public void setStartWorkingDir(String startWorkingDir) {
		this.startWorkingDir = startWorkingDir;
	}

	/**
	 * @return the startCommand
	 */
	public String getStartCommand() {
		return startCommand;
	}

	/**
	 * @param startCommand the startCommand to set
	 */
	public void setStartCommand(String startCommand) {
		this.startCommand = startCommand;
	}

	/**
	 * @return the startArgs
	 */
	public String getStartArgs() {
		return startArgs;
	}

	/**
	 * @param startArgs the startArgs to set
	 */
	public void setStartArgs(String startArgs) {
		this.startArgs = startArgs;
	}

	/**
	 * @return the stopWorkingDir
	 */
	public String getStopWorkingDir() {
		return stopWorkingDir;
	}

	/**
	 * @param stopWorkingDir the stopWorkingDir to set
	 */
	public void setStopWorkingDir(String stopWorkingDir) {
		this.stopWorkingDir = stopWorkingDir;
	}

	/**
	 * @return the stopCommand
	 */
	public String getStopCommand() {
		return stopCommand;
	}

	/**
	 * @param stopCommand the stopCommand to set
	 */
	public void setStopCommand(String stopCommand) {
		this.stopCommand = stopCommand;
	}

	/**
	 * @return the stopArgs
	 */
	public String getStopArgs() {
		return stopArgs;
	}

	/**
	 * @param stopArgs the stopArgs to set
	 */
	public void setStopArgs(String stopArgs) {
		this.stopArgs = stopArgs;
	}

	/**
	 * @return the installLocations
	 */
	public List<String> getInstallLocations() {
		if (installLocations == null) {
			installLocations = new ArrayList<String>();
		}
		return installLocations;
	}

	/**
	 * @param installLocations the installLocations to set
	 */
	public void setInstallLocations(List<String> installLocations) {
		this.installLocations = installLocations;
	}

	/**
	 * @return the configInfoList
	 */
	public List<ConfigInfo> getConfigInfoList() {
		if (configInfoList == null) {
			configInfoList = new ArrayList<ConfigInfo>();
		}
		
		return configInfoList;
	}

	/**
	 * @param configInfoList the configInfoList to set
	 */
	public void setConfigInfoList(List<ConfigInfo> configInfoList) {
		this.configInfoList = configInfoList;
	}
}
//end of SoftwareInfo.java