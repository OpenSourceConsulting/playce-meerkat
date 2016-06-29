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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.resources.services.ServerService;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bongjin Kwon
 * @version 1.0
 */
@Service
public class AgentProvisioningService extends AbstractProvisioningService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AgentProvisioningService.class);

		
	@Autowired
	private ServerService serverService;
	

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public AgentProvisioningService() {

	}

	
	public void installAgent(int serverId, int taskHistoryId, WebSocketSession session) {

		Server server = serverService.getServer(serverId);
		installAgent(server, taskHistoryId, session);
	}
	
	public void installAgent(Server server, int taskHistoryId, WebSocketSession session) {

		ProvisionModel pModel = new ProvisionModel(server, taskHistoryId);
		boolean success = runDefaultTargets(pModel, session, "deploy-agent");
		
		server.setAgentInstalled(success);
		serverService.save(server);
	}
	
	public void uninstallAgent(int serverId, int taskHistoryId, WebSocketSession session) {

		Server server = serverService.getServer(serverId);
		uninstallAgent(server, taskHistoryId, session);
	}
	
	public void uninstallAgent(Server server, int taskHistoryId, WebSocketSession session) {

		ProvisionModel pModel = new ProvisionModel(server, taskHistoryId);
		boolean success = runDefaultTargets(pModel, session, "remove-agent");
		
		if (success) {
			server.setAgentInstalled(false);
			serverService.save(server);
		}
	}
	
	public void reinstallAgent(int serverId, int taskHistoryId, WebSocketSession session) {

		Server server = serverService.getServer(serverId);
		reinstallAgent(server, taskHistoryId, session);
	}
	
	public void reinstallAgent(Server server, int taskHistoryId, WebSocketSession session) {

		ProvisionModel pModel = new ProvisionModel(server, taskHistoryId);
		boolean success = runDefaultTargets(pModel, session, "remove-agent", "deploy-agent");
		
		server.setAgentInstalled(success);
		serverService.save(server);
	}


}