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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.util.FileUtil;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.services.TaskHistoryService;
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
public class TomcatProvisioningService extends AbstractProvisioningService implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatProvisioningService.class);


	@Autowired
	private TomcatDomainService domainService;

	@Autowired
	private TomcatInstanceService instanceService;
	

	@PersistenceContext
    private EntityManager entityManager;


	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public TomcatProvisioningService() {
		
	}
	
	
	/**
	 * <pre>
	 * for test. skip provisioning.
	 * </pre>
	 * @param domainId
	 * @param session
	 */
	@Async
	public void installTomcatInstance2(int domainId, WebSocketSession session) {

		try {
			Thread.sleep(10000);
			System.out.println("test ############################# test");
		} catch (Exception e) {

		}

	}

	
	public void installTomcatInstance(int domainId, int taskHistoryId, WebSocketSession session) {


		int sessionGroupId = domainService.getDomain(domainId).getDataGridServerGroupId();
		
		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		List<DataSource> dsList = domainService.getDatasources(domainId);

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}

		List<TomcatConfigFile> confFiles = new ArrayList<TomcatConfigFile>();
		confFiles.add(configFileService.getLatestConfVersion(tomcatConfig.getTomcatDomain(), null, MeerkatConstants.CONFIG_FILE_TYPE_SERVER_XML_CD));
		confFiles.add(configFileService.getLatestConfVersion(tomcatConfig.getTomcatDomain(), null, MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML_CD));

		List<TomcatInstance> list = instanceService.getTomcatListWillInstallByDomainId(domainId);

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				
				ProvisionModel pModel = new ProvisionModel(taskHistoryId, tomcatConfig, tomcatInstance, dsList, true);
				pModel.setConfFiles(confFiles);
				pModel.setLastTask(count == list.size());
				
				if (sessionGroupId > 0) {
					pModel.setSessionServerGroupId(sessionGroupId);
					addDollyDefaultProperties(pModel, sessionGroupId);
				}
				
				doInstallTomcatInstance(pModel, session);
				count++;
			}
			
		} else {
			LOGGER.warn("tomcat instances is empty!!");
			
		}
		
	}

	private void doInstallTomcatInstance(ProvisionModel pModel, WebSocketSession session) {

		boolean isSuccess = true;
		File jobDir = generateBuildProperties(pModel, session);

		try {

			/*
			 * 1. make job dir & copy install.xml with default.xml.
			 */
			copyCmds("install.xml", jobDir);
			
			if (pModel.getSessionServerGroupId() > 0) {
				copyAntScript("installDolly.xml", jobDir);
			}
			


			/*
			 * 2. deploy agent
			 */
			isSuccess = ProvisioningUtil.runDefaultTarget(commanderDir, jobDir, "deploy-agent") && isSuccess;

			/*
			 * 3. send cmd.xml and run cmd.xml .
			 */
			isSuccess = ProvisioningUtil.sendCommand(commanderDir, jobDir) && isSuccess;

			/*
			 * 4. update server.xml & context.xml
			 */
			isSuccess = ProvisioningUtil.runDefaultTarget(commanderDir, jobDir, "update-config") && isSuccess;

			instanceService.saveState(pModel.getTomcatInstance(), MeerkatConstants.TOMCAT_STATUS_INSTALLED);
			
			/*
			 * 5. install dolly agent
			 */
			if (pModel.getSessionServerGroupId() > 0) {
				isSuccess = ProvisioningUtil.runDefaultTarget(commanderDir, jobDir, "send-script") && isSuccess;
			}
			
			if (isSuccess) {
				updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_SUCCESS);
			} else {
				updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_FAIL);
			}
			

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_FAIL);
			
			//throw new RuntimeException(e);

		} finally {
			LOGGER.debug(LOG_END);
			MDC.remove("jobPath");
			MDC.remove("serverIp");
		}

	}

	/**
	 * <pre>
	 * update ${catalina.base}/conf/catalina.properties
	 * - with updating rmi config in server.xml 
	 * </pre>
	 * @param domainId
	 * @param changeRMI
	 * @param session
	 */
	@Transactional
	public void updateTomcatInstanceConfig(int domainId, boolean changeRMI, WebSocketSession session) {

		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		// List<Tomcat> TomcatInstanceService
		List<TomcatInstance> list = instanceService.getTomcatListByDomainId(domainId);
		
		TomcatConfigFile confFile = configFileService.getLatestServerXmlFile(domainId);
		
		final String targetName = "update-" + configFileService.getFileTypeName(confFile.getFileTypeCdId(), 0);
		
		AdditionalTask addTask = null;

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}
		
		if (changeRMI) {
			updateRMIConfig(domainId, tomcatConfig);
			
			addTask = new AdditionalTask(){

				@Override
				public void runTask(File jobDir) throws Exception {
					ProvisioningUtil.runDefaultTarget(commanderDir, jobDir, targetName);
				}
				
			};
		}
		

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				
				ProvisionModel pModel = new ProvisionModel(tomcatConfig, tomcatInstance, null);
				pModel.addConfFile(confFile);
				pModel.setLastTask(count == list.size());
				
				sendCommand(pModel, "updateTomcatConfig.xml", session, addTask);
				count++;
			}
		} else {
			LOGGER.warn("tomcat instances is empty!!");
		}

	}
	
	/**
	 * <pre>
	 * server.xml 의 rmi 설정을 update 한다.
	 * </pre>
	 * @param domainId
	 * @param tomcatConfig
	 */
	private void updateRMIConfig(int domainId, DomainTomcatConfiguration tomcatConfig) {
		
		TomcatConfigFile serverXmlFile = configFileService.getLatestServerXmlFile(domainId);
		String serverXml = configFileService.getConfigFileContents(serverXmlFile.getFilePath());
		serverXmlFile.setContent(serverXml);
		
		entityManager.detach(serverXmlFile);
		serverXmlFile.setId(0);//for insert.
		serverXmlFile.increaseVersion();
		
		configFileService.saveConfigFile(serverXmlFile, tomcatConfig);
	}

	@Transactional
	public void updateTomcatInstanceConfig(TomcatInstance tomcat, WebSocketSession session) {
		DomainTomcatConfiguration tomcatConfig = instanceService.getTomcatConfig(tomcat.getId());
		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}
		sendCommand(new ProvisionModel(tomcatConfig, tomcat, null), "updateTomcatConfig.xml", session);
	}

	public void startTomcatInstance(int instanceId, WebSocketSession session) {

		TomcatInstance tomcatInstance = instanceService.findOne(instanceId);
		tomcatInstance.setState(MeerkatConstants.TOMCAT_STATUS_STARTING);
		instanceService.save(tomcatInstance);// update state.
		
		
		DomainTomcatConfiguration tomcatConfig = instanceService.getTomcatConfig(tomcatInstance.getDomainId(), instanceId);

		runCommand(new ProvisionModel(tomcatConfig, tomcatInstance, null), "startTomcat.xml", session);
	}

	public void stopTomcatInstance(int instanceId, WebSocketSession session) {

		TomcatInstance tomcatInstance = instanceService.findOne(instanceId);
		tomcatInstance.setState(MeerkatConstants.TOMCAT_STATUS_STOPPING);
		instanceService.save(tomcatInstance);// update state.
		
		DomainTomcatConfiguration tomcatConfig = instanceService.getTomcatConfig(tomcatInstance.getDomainId(), instanceId);

		runCommand(new ProvisionModel(tomcatConfig, tomcatInstance, null), "stopTomcat.xml", session);
	}
	
	@Transactional
	public void deployWar(int domainId, String warFilePath, String contextPath, WebSocketSession session) {

		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		// List<Tomcat> TomcatInstanceService
		List<TomcatInstance> list = instanceService.getTomcatListByDomainId(domainId);

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				
				ProvisionModel pModel = new ProvisionModel(tomcatConfig, tomcatInstance, null);
				pModel.addProps("warFilePath", configFileService.getFileFullPath(warFilePath));
				pModel.addProps("warFileName", FileUtil.getFileName(warFilePath));
				pModel.addProps("contextPath", contextPath);
				pModel.setLastTask(count == list.size());
				
				
				runCommand(pModel, "deployWar.xml", session);
				count++;
			}
		} else {
			LOGGER.warn("tomcat instances is empty!!");
		}

	}
	
	/**
	 * <pre>
	 * server.xml or context.xml 파일을 tomcat instance 에 적용한다.
	 * </pre>
	 * @param domainId
	 * @param configFileId 적용할 TomcatConfigFile id
	 * @param session
	 */
	@Transactional
	public void updateXml(int domainId, int configFileId, WebSocketSession session) {

		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		// List<Tomcat> TomcatInstanceService
		List<TomcatInstance> list = instanceService.getTomcatListByDomainId(domainId);
		
		TomcatConfigFile confFile = configFileService.getTomcatConfigFileById(configFileId);
		
		String targetName = "update-" + configFileService.getFileTypeName(confFile.getFileTypeCdId(), 0);
		

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				
				ProvisionModel pModel = new ProvisionModel(tomcatConfig, tomcatInstance, null);
				pModel.addConfFile(confFile);
				pModel.setLastTask(count == list.size());
				
				runDefaultTarget(pModel, targetName, session);
				count++;
			}
		} else {
			LOGGER.warn("tomcat instances is empty!!");
		}

	}
	
	/**
	 * <pre>
	 * install jar libs in catalina.base/lib
	 * </pre>
	 * @param domainId
	 * @param session
	 */
	@Transactional
	public void installJar(int domainId, String installJarName, WebSocketSession session) {

		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		
		List<TomcatInstance> list = instanceService.getTomcatListByDomainId(domainId);
		
		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}
		

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				
				ProvisionModel pModel = new ProvisionModel(tomcatConfig, tomcatInstance, null);
				pModel.addProps("install.jar.name", installJarName);
				pModel.setLastTask(count == list.size());
				
				sendCommand(pModel, "installLibs.xml", session);
				count++;
			}
		} else {
			LOGGER.warn("tomcat instances is empty!!");
		}

	}

	

	@Override
	public void afterPropertiesSet() throws Exception {
		
		initService();

		domainService.setProvService(this);

	}

	
}