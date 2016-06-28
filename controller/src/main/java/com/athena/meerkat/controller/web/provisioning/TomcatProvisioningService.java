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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketSession;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.util.FileUtil;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.TaskHistory;
import com.athena.meerkat.controller.web.entities.TaskHistoryDetail;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.monitoring.jmx.MonJmxService;
import com.athena.meerkat.controller.web.resources.services.ServerService;
import com.athena.meerkat.controller.web.tomcat.services.ApplicationService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatConfigFileService;
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

	@Autowired
	private ApplicationService appService;
	
	@Autowired
	private TomcatConfigFileService confFileService;
	
	@Autowired
	private ServerService serverService;
	
	@Value("${meerkat.jdbc.driver.mysql}")
	private String mysqlDriverFile;
	

	@Autowired
	private MonJmxService monJmxService;

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
	 * 
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

	/**
	 * <pre>
	 * 실패 task 를 재실행한다.
	 * </pre>
	 * @param taskHistoryDetailId
	 */
	public void rework(int taskHistoryDetailId) {
		TaskHistoryDetail taskDetail = taskService.getTaskHistoryDetail(taskHistoryDetailId);

		TaskHistory taskHistory = taskDetail.getTaskHistory();

		if (taskDetail.getTomcatDomainId() == 0 || taskHistory.getId() == 0) {
			throw new IllegalArgumentException("domain id and task history id must not be zero(0).");
		}

		if (MeerkatConstants.TASK_CD_TOMCAT_INSTANCE_INSTALL == taskHistory.getTaskCdId()) {
			installSingleTomcatInstance(taskDetail.getTomcatInstance(), taskHistory.getId());
			
		} else if (MeerkatConstants.TASK_CD_TOMCAT_INSTANCE_UNINSTALL == taskHistory.getTaskCdId()) {
			
			uninstallTomcatInstance(taskDetail.getTomcatInstanceId(), taskHistory.getId());
			
		} else if (MeerkatConstants.TASK_CD_TOMCAT_CONFIG_UPDATE == taskHistory.getTaskCdId()) {
			
			updateTomcatInstanceConfig(taskDetail.getTomcatInstance().getId(), taskHistory.getId(), true);
			
		} else if (MeerkatConstants.TASK_CD_SERVER_XML_UPDATE == taskHistory.getTaskCdId()) {
			
			TomcatConfigFile confFile = confFileService.getLatestServerXmlFile(taskDetail.getTomcatDomainId());
			
			updateXml(taskDetail.getTomcatInstance().getId(), confFile.getId(), taskHistory.getId());
			
		} else if (MeerkatConstants.TASK_CD_CONTEXT_XML_UPDATE == taskHistory.getTaskCdId()) {
			
			TomcatConfigFile confFile = confFileService.getLatestContextXmlFile(taskDetail.getTomcatDomainId());
			
			updateXml(taskDetail.getTomcatInstance().getId(), confFile.getId(), taskHistory.getId());
			
		} else if (MeerkatConstants.TASK_CD_WAR_DEPLOY == taskHistory.getTaskCdId()) {
			
			TomcatApplication app = appService.getApplicationByTask(taskHistory.getId());
			
			deployWar(taskDetail.getTomcatInstance().getId(), taskHistory.getId(), app.getId());
			
		} else if (MeerkatConstants.TASK_CD_WAR_UNDEPLOY == taskHistory.getTaskCdId()) {
			
			TomcatApplication app = appService.getApplicationByTask(taskHistory.getId());
			
			undeployWar(taskDetail.getTomcatInstance().getId(), taskHistory.getId(), app);
		} else if (MeerkatConstants.TASK_CD_JDBC_MYSQL_INSTALL == taskHistory.getTaskCdId()) {
			
			installJar(taskDetail.getTomcatInstance().getId(), mysqlDriverFile, taskHistory.getId());
		}

	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param domainId
	 * @param taskHistoryId
	 * @param tomcatInstance
	 */
	public void installSingleTomcatInstance(TomcatInstance tomcatInstance, int taskHistoryId) {

		Assert.notNull(tomcatInstance, "tomcatInstance must not be null.");

		List<TomcatInstance> singleList = new ArrayList<TomcatInstance>();
		singleList.add(tomcatInstance);

		installTomcatInstances(tomcatInstance.getDomainId(), taskHistoryId, singleList);
	}

	public void installTomcatInstances(int domainId, int taskHistoryId, List<TomcatInstance> list) {

		int sessionGroupId = domainService.getDomain(domainId).getDataGridServerGroupId();

		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		List<DataSource> dsList = domainService.getDatasources(domainId);

		if (tomcatConfig == null) {
			LOGGER.warn("### tomcat config is not set!!");
			return;
		}

		List<TomcatConfigFile> confFiles = new ArrayList<TomcatConfigFile>();
		confFiles.add(configFileService.getLatestConfVersion(tomcatConfig.getTomcatDomain(), null, MeerkatConstants.CONFIG_FILE_TYPE_SERVER_XML_CD));
		confFiles.add(configFileService.getLatestConfVersion(tomcatConfig.getTomcatDomain(), null, MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML_CD));

		if (list == null) {
			list = instanceService.getTomcatListWillInstallByDomainId(domainId);
		}

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				
				monJmxService.requestTomcatInstanceAdding(tomcatInstance.getServer(), tomcatInstance.getId(), tomcatConfig);

				ProvisionModel pModel = new ProvisionModel(taskHistoryId, tomcatConfig, tomcatInstance, dsList, true);
				pModel.setConfFiles(confFiles);
				pModel.setLastTask(count == list.size());

				if (sessionGroupId > 0) {
					pModel.setSessionServerGroupId(sessionGroupId);
					addDollyDefaultProperties(pModel, sessionGroupId);
				}

				doInstallTomcatInstance(pModel, null);
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

			Server server = pModel.getServer();
			if (server.isAgentInstalled() == false) {
				/*
				 * 2. install agent
				 */
				isSuccess = ProvisioningUtil.runDefaultTarget(commanderDir, jobDir, "deploy-agent") && isSuccess;
				
				if (isSuccess) {
					server.setAgentInstalled(isSuccess);
					serverService.save(server);
				}
			}
			

			/*
			 * 3. send cmd.xml and run cmd.xml .
			 */
			isSuccess = ProvisioningUtil.sendCommand(commanderDir, jobDir) && isSuccess;

			if (isSuccess) {
				
				/*
				 * 4. update server.xml & context.xml
				 */
				isSuccess = ProvisioningUtil.runDefaultTarget(commanderDir, jobDir, "update-config") && isSuccess;

				/*
				 * 5. install dolly agent
				 */
				if (pModel.getSessionServerGroupId() > 0) {
					isSuccess = ProvisioningUtil.runDefaultTarget(commanderDir, jobDir, "send-script") && isSuccess;
				}
				
			}
			

			if (isSuccess) {
				instanceService.saveState(pModel.getTomcatInstance(), MeerkatConstants.TOMCAT_STATUS_INSTALLED);
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

	public void updateTomcatInstanceConfig(int tomcatInstanceId, int taskHistoryId, boolean changeRMI) {

		TomcatInstance tomcatInstance = instanceService.findOne(tomcatInstanceId);

		List<TomcatInstance> singleList = new ArrayList<TomcatInstance>();
		singleList.add(tomcatInstance);

		updateTomcatInstanceConfig(tomcatInstance.getDomainId(), taskHistoryId, changeRMI, singleList);
	}

	/**
	 * <pre>
	 * update ${catalina.base}/conf/catalina.properties
	 * - with updating rmi config in server.xml
	 * </pre>
	 * 
	 * @param domainId
	 * @param changeRMI
	 *            JMX 설정변경 여부. true 이면 server.xml파일의 jmx 설정을 변경하고 추가 업데이트 시킨다.
	 * @param session
	 */
	public void updateTomcatInstanceConfig(int domainId, int taskHistoryId, boolean changeRMI, List<TomcatInstance> list) {

		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		// List<Tomcat> TomcatInstanceService

		if (list == null) {
			list = instanceService.getTomcatListByDomainId(domainId);
		}

		TomcatConfigFile confFile = configFileService.getLatestServerXmlFile(domainId);

		//final String targetName = "update-" + configFileService.getFileTypeName(confFile.getFileTypeCdId(), 0);

		AdditionalTask addTask = null;

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}

		if (changeRMI) {
			updateJMXConfig(domainId, tomcatConfig);

			addTask = new AdditionalTask() {

				@Override
				public void runTask(File jobDir) throws Exception {
					ProvisioningUtil.runDefaultTarget(commanderDir, jobDir, "update-server.xml");
				}

			};
		}

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {

				ProvisionModel pModel = new ProvisionModel(taskHistoryId, tomcatConfig, tomcatInstance, null);
				pModel.addConfFile(confFile);
				pModel.setLastTask(count == list.size());

				sendCommand(pModel, "updateTomcatConfig.xml", null, addTask);
				count++;
			}
		} else {
			LOGGER.warn("tomcat instances is empty!!");
		}

	}

	/**
	 * <pre>
	 * server.xml 의  jmx 설정을 update 한다.
	 * </pre>
	 * 
	 * @param domainId
	 * @param tomcatConfig
	 */
	private void updateJMXConfig(int domainId, DomainTomcatConfiguration tomcatConfig) {

		TomcatConfigFile serverXmlFile = configFileService.getLatestServerXmlFile(domainId);
		String serverXml = configFileService.getConfigFileContents(serverXmlFile.getFilePath());
		serverXmlFile.setContent(serverXml);

		entityManager.detach(serverXmlFile);
		serverXmlFile.setId(0);//for insert.
		serverXmlFile.increaseVersion();

		configFileService.saveConfigXmlFile(serverXmlFile, tomcatConfig);
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

	public void deployWar(int tomcatInstanceId, int taskHistoryId, int applicationId) {
		TomcatInstance tomcatInstance = instanceService.findOne(tomcatInstanceId);

		TomcatApplication app = appService.getApplication(applicationId);

		List<TomcatInstance> singleList = new ArrayList<TomcatInstance>();
		singleList.add(tomcatInstance);

		deployWar(tomcatInstance.getDomainId(), taskHistoryId, app.getWarPath(), app.getContextPath(), singleList);

	}

	public void deployWar(int domainId, int taskHistoryId, String warFilePath, String contextPath, List<TomcatInstance> list) {

		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		// List<Tomcat> TomcatInstanceService

		if (list == null) {
			list = instanceService.getTomcatListByDomainId(domainId);
		}

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {

				ProvisionModel pModel = new ProvisionModel(taskHistoryId, tomcatConfig, tomcatInstance, null);
				pModel.addProps("warFilePath", configFileService.getFileFullPath(warFilePath));
				pModel.addProps("warFileName", FileUtil.getFileName(warFilePath));
				pModel.addProps("contextPath", contextPath);
				pModel.setLastTask(count == list.size());

				runCommand(pModel, "deployWar.xml", null);
				count++;
			}
		} else {
			LOGGER.warn("tomcat instances is empty!!");
		}

	}

	public void undeployWar(int tomcatInstanceId, int taskHistoryId, TomcatApplication app) {
		TomcatInstance tomcatInstance = instanceService.findOne(tomcatInstanceId);
		List<TomcatInstance> singleList = new ArrayList<TomcatInstance>();
		singleList.add(tomcatInstance);
		undeployWar(app.getTomcatDomain().getId(), taskHistoryId, app.getContextPath(), singleList);
	}

	public void undeployWar(int domainId, int taskHistoryId, String contextPath, List<TomcatInstance> list) {
		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		if (list == null) {
			list = instanceService.getTomcatListByDomainId(domainId);
		}
		if (list != null && list.size() > 0) {
			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				ProvisionModel pModel = new ProvisionModel(taskHistoryId, tomcatConfig, tomcatInstance, null);

				pModel.addProps("contextPath", contextPath);
				pModel.setLastTask(count == list.size());
				runCommand(pModel, "unDeployWar.xml", null);
				count++;
			}
		} else {
			LOGGER.warn("tomcat instances is empty!!");
		}
	}

	public void updateXml(int tomcatInstanceId, int configFileId, int taskHistoryId) {
		TomcatInstance tomcatInstance = instanceService.findOne(tomcatInstanceId);

		List<TomcatInstance> singleList = new ArrayList<TomcatInstance>();
		singleList.add(tomcatInstance);

		updateXml(tomcatInstance.getDomainId(), configFileId, taskHistoryId, singleList);
	}

	/**
	 * <pre>
	 * server.xml or context.xml 파일을 tomcat instance 에 적용한다.
	 * </pre>
	 * 
	 * @param domainId
	 * @param configFileId
	 *            적용할 TomcatConfigFile id
	 * @param session
	 */
	public void updateXml(int domainId, int configFileId, int taskHistoryId, List<TomcatInstance> list) {

		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		// List<Tomcat> TomcatInstanceService

		if (list == null) {
			list = instanceService.getTomcatListByDomainId(domainId);
		}

		TomcatConfigFile confFile = configFileService.getTomcatConfigFileById(configFileId);

		String targetName = "update-" + configFileService.getFileTypeName(confFile.getFileTypeCdId(), 0);

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {

				ProvisionModel pModel = new ProvisionModel(taskHistoryId, tomcatConfig, tomcatInstance, null);
				pModel.addConfFile(confFile);
				pModel.setLastTask(count == list.size());

				runDefaultTarget(pModel, targetName, null);
				count++;
			}
		} else {
			LOGGER.warn("tomcat instances is empty!!");
		}

	}

	public void installJar(int tomcatInstanceId, String installJarName, int taskHistoryId) {
		TomcatInstance tomcatInstance = instanceService.findOne(tomcatInstanceId);

		List<TomcatInstance> singleList = new ArrayList<TomcatInstance>();
		singleList.add(tomcatInstance);

		installJar(tomcatInstance.getDomainId(), installJarName, taskHistoryId, singleList);
	}

	/**
	 * <pre>
	 * install jar libs in catalina.base/lib
	 * </pre>
	 * 
	 * @param domainId
	 * @param session
	 */
	public void installJar(int domainId, String installJarName, int taskHistoryId, List<TomcatInstance> list) {

		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);

		if (list == null) {
			list = instanceService.getTomcatListByDomainId(domainId);
		}

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {

				ProvisionModel pModel = new ProvisionModel(taskHistoryId, tomcatConfig, tomcatInstance, null);
				pModel.addProps("install.jar.name", installJarName);
				pModel.setLastTask(count == list.size());

				sendCommand(pModel, "installLibs.xml", null);
				count++;
			}
		} else {
			LOGGER.warn("tomcat instances is empty!!");
		}

	}
	
	public void uninstallTomcatInstance(int tomcatInstanceId, int taskHistoryId) {
		TomcatInstance tomcatInstance = instanceService.findOne(tomcatInstanceId);

		List<TomcatInstance> singleList = new ArrayList<TomcatInstance>();
		singleList.add(tomcatInstance);
		
		uninstallTomcatInstanceList(tomcatInstance.getDomainId(), taskHistoryId, singleList);
	}
	
	public void uninstallTomcatInstanceList(int domainId, int taskHistoryId, List<TomcatInstance> list) {

		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);

		if (list == null) {
			list = instanceService.getTomcatListByDomainId(domainId);
		}
		
		taskService.createTaskDetails(list, MeerkatConstants.TASK_CD_TOMCAT_INSTANCE_UNINSTALL);

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				
				monJmxService.requestTomcatInstanceRemoving(tomcatInstance.getServer(), tomcatInstance.getId());

				ProvisionModel pModel = new ProvisionModel(taskHistoryId, tomcatConfig, tomcatInstance, null);
				pModel.setLastTask(count == list.size());

				runCommand(pModel, "uninstallTomcatInstance.xml", null);
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