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
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.provisioning.util.ProvisioningUtil;
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
	

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public ScouterProvisioningService() {
		
	}
	
	

	public boolean installScouterAgent(int domainId, String scouterAgentInstallPath, String scouterAgentConfigs) {

		boolean isSuccess = true;
		
		TomcatDomain domain = domainService.getDomain(domainId);
		domain.setScouterAgentInstallPath(scouterAgentInstallPath);
		domainService.save(domain);
		
		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		//List<DataSource> dsList = domainService.getDatasources(domainId);

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return false;
		}


		List<TomcatInstance> list = instanceService.getTomcatListByDomainId(domainId);

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				
				String scouterConfigFileName = tomcatInstance.getName() + ".conf";
				
				ProvisionModel pModel = new ProvisionModel(tomcatConfig, tomcatInstance, null, true);
				pModel.setLastTask(count == list.size());
				pModel.addProps("agent.install.path", scouterAgentInstallPath);
				pModel.addProps("scouter.jar.name", scouterAgentJarName);
				pModel.addProps("upload.files", scouterConfigFileName);
				pModel.addProps("upload.dir", scouterAgentInstallPath);

				
				isSuccess = doIntallScouterAgent(pModel, scouterConfigFileName, scouterAgentConfigs) && isSuccess;
				count++;
			}
			
		} else {
			LOGGER.warn("tomcat instances is empty!!");
			isSuccess = false;
		}
		
		return isSuccess;
	}

	private boolean doIntallScouterAgent(ProvisionModel pModel, String scouterConfigFileName, String scouterAgentConfigs) {

		boolean isSuccess = true;
		File jobDir = generateBuildProperties(pModel, null);

		try {

			/*
			 * 1. copy cmdFile with default.xml.
			 */
			copyCmds("installScouterAgent.xml", jobDir);
			
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


		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			throw new RuntimeException(e);

		} finally {
			LOGGER.debug(LOG_END);
			MDC.remove("jobPath");
			MDC.remove("serverIp");
		}
		
		return isSuccess;
	}

	
	public boolean unintallScouterAgent(int domainId, WebSocketSession session) {

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

		List<TomcatInstance> list = instanceService.getTomcatListByDomainId(domainId);

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				
				ProvisionModel pModel = new ProvisionModel(tomcatConfig, tomcatInstance, null, true);
				pModel.setLastTask(count == list.size());
				
		
				isSuccess = doUnintallScouterAgent(pModel, session) && isSuccess;
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


		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			throw new RuntimeException(e);

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

	
}