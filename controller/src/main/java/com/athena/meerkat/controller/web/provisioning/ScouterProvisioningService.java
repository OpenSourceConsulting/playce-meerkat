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
 * BongJin Kwon		2016. 3. 24.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.FileHandler;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.provisioning.util.ProvisioningUtil;
import com.athena.meerkat.controller.web.provisioning.util.ScpUtil;
import com.athena.meerkat.controller.web.provisioning.util.TargetHost;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bongjin Kwon
 * @version 1.0
 */
@Service
public class ScouterProvisioningService extends AbstractProvisioningService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScouterProvisioningService.class);


	@Autowired
	private TomcatDomainService domainService;

	@Autowired
	private TomcatInstanceService instanceService;
	
	@Value("${meerkat.scouter.jar.name}")
	private String scouterAgentJarName;
	
	@Value("${meerkat.scouter.agent.install.path}")
	private String scouterAgentInstallPath;
	
	@Autowired
	private FileHandler fileHandler;
	

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public ScouterProvisioningService() {
		
	}
	
	/**
	 * <pre>
	 * 첫번째 tomcat instance 에 설치된 agent 설정파일을 가져온다.
	 * 없으면 default 설정을 반환.
	 * </pre>
	 * @param domainId
	 * @return
	 */
	public Map<String, String> getAgentConfig(int domainId) {
		
		Map<String, String> configMap = new HashMap<String, String>();
		String scouterAgentConfigs = null;
		
		TomcatDomain domain = domainService.getDomain(domainId);
		
		List<TomcatInstance> tomcats = domain.getTomcatInstances();
		
		if (domain.getScouterAgentInstallPath() != null) {
			
			configMap.put("scouterAgentInstallPath", domain.getScouterAgentInstallPath());
		} else {
			configMap.put("scouterAgentInstallPath", scouterAgentInstallPath);
		}
		
		if (domain.getScouterAgentInstallPath() != null && tomcats.size() > 0) {
			
			
			try {
				scouterAgentConfigs = getInstalledAgentConfig(tomcats.get(0), domain.getScouterAgentInstallPath());
			} catch (IOException e) {
				LOGGER.error(e.toString(), e);
				throw new RuntimeException(e);
			}
			
			configMap.put("isDefault", "false");
			
			
		} else {
			/*
			 * default config
			 */
			scouterAgentConfigs = fileHandler.readFileToString("classpath:/scouterconfig/scouterAgent.conf");
			
			configMap.put("isDefault", "true");
		}
		
		configMap.put("scouterAgentConfigs", scouterAgentConfigs);
		
		
		return configMap;
		
	}
	
	public void installSingleScouterAgent(int tomcatInstanceId, String scouterAgentInstallPath, String scouterAgentConfigs, int taskHistoryId, boolean isDefault) {
		
		TomcatInstance tomcatInstance = instanceService.findOne(tomcatInstanceId);

		List<TomcatInstance> singleList = new ArrayList<TomcatInstance>();
		singleList.add(tomcatInstance);
		
		installScouterAgent(tomcatInstance.getDomainId(), scouterAgentInstallPath, scouterAgentConfigs, taskHistoryId, singleList, isDefault);
	}
	

	public boolean installScouterAgent(int domainId, String scouterAgentInstallPath, String scouterAgentConfigs, int taskHistoryId, List<TomcatInstance> list, boolean isDefault) {

		boolean isSuccess = true;
		/*
		TomcatDomain domain = domainService.getDomain(domainId);
		domain.setScouterAgentInstallPath(scouterAgentInstallPath);
		domainService.save(domain);
		*/
		
		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		//List<DataSource> dsList = domainService.getDatasources(domainId);

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return false;
		}


		if (list == null) {
			list = instanceService.getTomcatListByDomainId(domainId);
		}

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				
				String scouterConfigFileName = tomcatInstance.getName() + ".conf";
				
				ProvisionModel pModel = new ProvisionModel(taskHistoryId, tomcatConfig, tomcatInstance, null, true);
				pModel.setLastTask(count == list.size());
				pModel.addProps("agent.install.path", scouterAgentInstallPath);
				pModel.addProps("scouter.jar.name", scouterAgentJarName);
				pModel.addProps("upload.files", scouterConfigFileName);
				pModel.addProps("upload.dir", scouterAgentInstallPath);

				
				isSuccess = doIntallScouterAgent(pModel, scouterConfigFileName, scouterAgentConfigs, isDefault) && isSuccess;
				count++;
			}
			
		} else {
			LOGGER.warn("tomcat instances is empty!!");
			isSuccess = false;
		}
		
		return isSuccess;
	}

	private boolean doIntallScouterAgent(ProvisionModel pModel, String scouterConfigFileName, String scouterAgentConfigs, boolean isDefault) {

		boolean isSuccess = true;
		File jobDir = generateBuildProperties(pModel, null);

		try {

			/*
			 * 1. copy cmdFile with default.xml.
			 */
			if (isDefault) {
				copyCmds("installScouterAgent.xml", jobDir);
			} else {
				copyCmds("updateScouterAgentConfig.xml", jobDir);
			}
			
			
			/*
			 * 2. make scouter agent configuraton file
			 */
			makeAgentConfigurationFile(jobDir, scouterConfigFileName, scouterAgentConfigs);
			
			
			/*
			 * 3. upload scouter agent configuraton file
			 */
			ProvisioningUtil.runDefaultTarget(commanderDir, jobDir, "upload-files");


			/*
			 * 4. send cmd.xml and run cmd.xml .
			 */
			isSuccess = ProvisioningUtil.sendCommand(commanderDir, jobDir) && isSuccess;
			
			if (isSuccess) {
				updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_SUCCESS);
			} else {
				updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_FAIL);
			}


		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			//throw new RuntimeException(e);
			updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_FAIL);

		} finally {
			LOGGER.debug(LOG_END);
			MDC.remove("jobPath");
			MDC.remove("serverIp");
		}
		
		return isSuccess;
	}

	public void uninstallSingleScouterAgent(int tomcatInstanceId, int taskHistoryId) {
		
		TomcatInstance tomcatInstance = instanceService.findOne(tomcatInstanceId);

		List<TomcatInstance> singleList = new ArrayList<TomcatInstance>();
		singleList.add(tomcatInstance);
		
		unintallScouterAgent(tomcatInstance.getDomainId(), taskHistoryId, singleList);
	}
	
	public boolean unintallScouterAgent(int domainId, int taskHistoryId, List<TomcatInstance> list) {

		boolean isSuccess = true;
		
		TomcatDomain domain = domainService.getDomain(domainId);
		domain.setScouterAgentInstallPath(null);
		domainService.save(domain);

		
		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		//List<DataSource> dsList = domainService.getDatasources(domainId);

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return false;
		}

		if (list == null) {
			list = instanceService.getTomcatListByDomainId(domainId);
		}

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				
				ProvisionModel pModel = new ProvisionModel(taskHistoryId, tomcatConfig, tomcatInstance, null, true);
				pModel.setLastTask(count == list.size());
				
		
				isSuccess = doUnintallScouterAgent(pModel, null) && isSuccess;
				count++;
			}
			
		} else {
			LOGGER.warn("tomcat instances is empty!!");
			isSuccess = false;
		}
		
		return isSuccess;
	}
	
	private boolean doUnintallScouterAgent(ProvisionModel pModel, WebSocketSession session) {

		boolean isSuccess = true;
		File jobDir = generateBuildProperties(pModel, session);

		try {

			/*
			 * 1. copy cmdFile with default.xml.
			 */
			copyCmds("unInstallScouterAgent.xml", jobDir);


			/*
			 * 2. send cmd.xml and run cmd.xml .
			 */
			isSuccess = ProvisioningUtil.sendCommand(commanderDir, jobDir) && isSuccess;
			
			if (isSuccess) {
				updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_SUCCESS);
			} else {
				updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_FAIL);
			}


		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			//throw new RuntimeException(e);
			
			updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_FAIL);

		} finally {
			LOGGER.debug(LOG_END);
			MDC.remove("jobPath");
			MDC.remove("serverIp");
		}
		
		return isSuccess;
	}
	
	private void makeAgentConfigurationFile(File jobDir, String scouterConfigFileName, String scouterAgentConfigs) throws IOException {
		
		FileUtils.writeStringToFile(new File(jobDir.getAbsolutePath() + File.separator + scouterConfigFileName), scouterAgentConfigs, "UTF-8");
	}
	
	private String getInstalledAgentConfig(TomcatInstance tomcatInstance, String scouterAgentInstallPath) throws IOException{
		
		String downFileName = tomcatInstance.getName() + ".conf";
		String remoteDownFile = scouterAgentInstallPath + "/scouter/agent.java/" + downFileName;
		File todirFile = new File(commanderDir.getAbsolutePath() + File.separator + "temp");
		
		if (todirFile.exists() == false) {
			todirFile.mkdir();
			LOGGER.info("create {}", todirFile.getAbsolutePath());
		}
		
		ScpUtil.download(new TargetHost(tomcatInstance.getServer()), remoteDownFile, todirFile.getAbsolutePath());
		
		return FileUtils.readFileToString(new File(todirFile.getAbsolutePath() + File.separator + downFileName));
		
	}

	
}