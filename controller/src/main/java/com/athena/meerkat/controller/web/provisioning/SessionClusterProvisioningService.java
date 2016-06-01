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
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.resources.services.DataGridServerGroupService;
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
public class SessionClusterProvisioningService extends AbstractProvisioningService implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionClusterProvisioningService.class);


	@Autowired
	private TomcatDomainService domainService;

	@Autowired
	private TomcatInstanceService instanceService;
	

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public SessionClusterProvisioningService() {
		
	}
	
	

	@Transactional
	public boolean configureSessionClustering(int domainId, WebSocketSession session) {

		boolean isSuccess = true;
		
		int sessionGroupId = domainService.getDomain(domainId).getDataGridServerGroupId();
		
		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		List<DataSource> dsList = domainService.getDatasources(domainId);

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return false;
		}

		List<TomcatConfigFile> confFiles = new ArrayList<TomcatConfigFile>();
		confFiles.add(configFileService.getLatestConfVersion(tomcatConfig.getTomcatDomain(), null, MeerkatConstants.CONFIG_FILE_TYPE_SERVER_XML_CD));
		confFiles.add(configFileService.getLatestConfVersion(tomcatConfig.getTomcatDomain(), null, MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML_CD));

		List<TomcatInstance> list = instanceService.getTomcatListByDomainId(domainId);

		if (list != null && list.size() > 0) {

			int count = 1;
			for (TomcatInstance tomcatInstance : list) {
				
				ProvisionModel pModel = new ProvisionModel(tomcatConfig, tomcatInstance, dsList, true);
				pModel.setConfFiles(confFiles);
				pModel.setLastTask(count == list.size());
				
				if (sessionGroupId > 0) {
					pModel.setSessionServerGroupId(sessionGroupId);
					addDollyDefaultProperties(pModel, sessionGroupId);
				}
				
				isSuccess = doConfigureSessionClustering(pModel, session) && isSuccess;
				count++;
			}
			
		} else {
			LOGGER.warn("tomcat instances is empty!!");
			isSuccess = false;
		}
		
		return isSuccess;
	}

	private boolean doConfigureSessionClustering(ProvisionModel pModel, WebSocketSession session) {

		boolean isSuccess = true;
		File jobDir = generateBuildProperties(pModel, session);

		try {

			/*
			 * 1. make job dir & copy install.xml with default.xml.
			 */
			copyCmds("installDolly.xml", jobDir);


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


	@Override
	public void afterPropertiesSet() throws Exception {
		
		initService();

		//domainService.setProvService(this);

	}

	
}