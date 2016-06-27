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
 * BongJin Kwon		2016. 6. 20.		First Draft.
 */
package com.athena.meerkat.controller.web.monitoring;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Component
public class StompEventListener implements ApplicationListener<ApplicationEvent> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StompEventListener.class);
	
	private Map<String, Integer> runningAgentCache = new HashMap<String, Integer>();//{sessionId : serverId}

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public StompEventListener() {
		
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		
		LOGGER.debug(event.toString());
		
		if (event instanceof SessionSubscribeEvent) {
			handleEvent((SessionSubscribeEvent)event);
		} else if (event instanceof SessionDisconnectEvent) {
			handleEvent((SessionDisconnectEvent)event);
		}
	}
	
	public Map<String, Integer> getRunningAgentCache() {
		return Collections.unmodifiableMap(runningAgentCache);
	}
	
	public boolean isRunningAgent(Integer serverId) {
		return runningAgentCache.containsValue(serverId);
	}
	
	public String getUserDestination(Integer serverId) {
		
		String userDestination = null;
		for (Entry<String, Integer> entry : runningAgentCache.entrySet()) {
			
			if(serverId.equals(entry.getValue())) {
				userDestination = "user" + entry.getKey();
				break;
			}
		}
		
		return userDestination;
	}
	
	
	private void handleEvent(SessionSubscribeEvent event) {
		//#### SessionSubscribeEvent[GenericMessage [payload=byte[0], headers={stompCommand=SUBSCRIBE, nativeHeaders={id=[subs1], destination=[/user/queue/agents]}, simpSessionAttributes={}, simpMessageType=SUBSCRIBE, simpSubscriptionId=subs1, simpDestination=/user/queue/agents, simpSessionId=1}]]
		
		GenericMessage msg = (GenericMessage)event.getMessage();
		
		MessageHeaders headers = msg.getHeaders();
		
		String subscriptionId = (String)headers.get("simpSubscriptionId");
		Integer serverId = getServerId(subscriptionId);
		String sessionId = (String)headers.get("simpSessionId");
		
		if (serverId != null) {
			runningAgentCache.put(sessionId, serverId);
			LOGGER.debug("save sessionId = {}, serverId = {}", sessionId, serverId);
		}
		
	}
	
	private void handleEvent(SessionDisconnectEvent event) {
		//#### SessionDisconnectEvent[sessionId=1, CloseStatus[code=1006, reason=null]]
		
		String sessionId = event.getSessionId();
		
		runningAgentCache.remove(sessionId);
		LOGGER.debug("remove sessionId = {}", sessionId);
		
	}
	
	private Integer getServerId(String subscriptionId) {
		if (subscriptionId.startsWith("subs_")) {
			return new Integer(subscriptionId.substring(5));
		} else {
			LOGGER.warn("@@@@ invalid subscription id : {}", subscriptionId);
			return null;
		}
	}

}
//end of StompEventListener.java