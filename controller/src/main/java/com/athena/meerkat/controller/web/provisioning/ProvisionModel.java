/* 
 * Copyright (C) 2012-2015 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * BongJin Kwon		2016. 4. 21.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatInstance;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
public class ProvisionModel {
	
	private static final String CONFIG_OP_ADD = "+";
	private static final String CONFIG_OP_SET = "=";
	
	private DomainTomcatConfiguration tomcatConfig;
	private TomcatInstance tomcatInstance;
	private List<TomcatConfigFile> confFiles;
	private List<DataSource> dsList = new ArrayList<DataSource>();
	
	private boolean isFirstInstall;
	
	private Map<String, String> propsMap;// additional properties.
	
	private boolean lastTask;
	
	
	public ProvisionModel(DomainTomcatConfiguration tomcatConfig, TomcatInstance tomcatInstance, List<DataSource> dsList) {
		this.tomcatConfig = tomcatConfig;
		this.tomcatInstance = tomcatInstance;
		
		if (dsList != null) {
			this.dsList = dsList;
		}
		
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param tomcatConfig
	 * @param tomcatInstance
	 * @param isFirstInstall 첫 설치 여부.
	 */
	public ProvisionModel(DomainTomcatConfiguration tomcatConfig, TomcatInstance tomcatInstance, List<DataSource> dsList, boolean isFirstInstall) {
		this(tomcatConfig, tomcatInstance, dsList);
		this.isFirstInstall = isFirstInstall;
	}

	public boolean isLastTask() {
		return lastTask;
	}

	public void setLastTask(boolean lastTask) {
		this.lastTask = lastTask;
	}

	public Map<String, String> getPropsMap() {
		return propsMap;
	}

	public void setPropsMap(Map<String, String> propsMap) {
		this.propsMap = propsMap;
	}
	
	public void addProps(String key, String value) {
		if (this.propsMap == null) {
			this.propsMap = new HashMap<String, String>();
		}
		
		this.propsMap.put(key, value);
	}

	public DomainTomcatConfiguration getTomcatConfig() {
		return tomcatConfig;
	}

	public void setTomcatConfig(DomainTomcatConfiguration tomcatConfig) {
		this.tomcatConfig = tomcatConfig;
	}

	public TomcatInstance getTomcatInstance() {
		return tomcatInstance;
	}

	public void setTomcatInstance(TomcatInstance tomcatInstance) {
		this.tomcatInstance = tomcatInstance;
	}

	public List<TomcatConfigFile> getConfFiles() {
		return confFiles;
	}

	public void setConfFiles(List<TomcatConfigFile> confFiles) {
		this.confFiles = confFiles;
	}
	
	public void addConfFile(TomcatConfigFile confFile) {
		
		if (confFiles == null) {
			confFiles = new ArrayList<TomcatConfigFile>();
		}
		confFiles.add(confFile);
	}
	
	public List<DataSource> getDsList() {
		return dsList;
	}

	public void setDsList(List<DataSource> dsList) {
		this.dsList = dsList;
	}
	
	public String getConfigOP() {
		if (isFirstInstall) {
			return CONFIG_OP_ADD;
		}else {
			return CONFIG_OP_SET;
		}
	}
	
	public TomcatConfigFile getContextXmlConfig() {
		
		if (confFiles == null) {
			return null;
		}
		
		TomcatConfigFile tcFile = null;
		for (TomcatConfigFile tomcatConfigFile : confFiles) {
			if (MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML_CD == tomcatConfigFile.getFileTypeCdId()) {
				tcFile = tomcatConfigFile;
				break;
			}
		}
		
		return tcFile;
	}

}
//end of ProvisionModel.java