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
 * BongJin Kwon		2016. 4. 6.		First Draft.
 */
package com.athena.meerkat.agent.monitoring.websocket;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompDecoder;
import org.springframework.messaging.simp.stomp.StompEncoder;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.athena.meerkat.agent.MeerkatAgentConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
public class StompWebSocketHandler extends TextWebSocketHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StompWebSocketHandler.class);
	
	public static final byte[] EMPTY_PAYLOAD = new byte[0]; 
	private static final Charset UTF_8 = Charset.forName("UTF-8"); 
	
	private final StompEncoder encoder = new StompEncoder();
	private final StompDecoder decoder = new StompDecoder();
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private ArrayNode instanceConfigs;//DomainTomcatConfiguration array
	
	private TextMessage subscribeMsg;
	
	private StompWebSocketClient webSocketClient;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public StompWebSocketHandler(String subscriptionId, StompWebSocketClient webSocketClient, String topic) {
		subscribeMsg = StompTextMessageBuilder.create(StompCommand.SUBSCRIBE).headers("id:" + subscriptionId, "destination:" + topic).build();
		this.webSocketClient = webSocketClient;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		LOGGER.debug("Connection estableshed!!.");
		
		sendSubscription(session);
		
	}
	
	private void sendSubscription(WebSocketSession session) throws Exception  {
		
		session.sendMessage(subscribeMsg);
		/*
		StompHeaderAccessor subheaders = StompHeaderAccessor.create(StompCommand.SUBSCRIBE); 
		subheaders.setSubscriptionId("sub-0"); 
		subheaders.setDestination("/topic/greetings"); 
		  
		StompEncoder encoder = new StompEncoder();
		byte[] bytes = encoder.encode(MessageBuilder.withPayload(EMPTY_PAYLOAD).setHeaders(subheaders).build());
		session.sendMessage(new TextMessage(new String(bytes, UTF_8)));
		*/
		LOGGER.debug(">>> SUBSCRIBE: {}", getHeader(subscribeMsg).getDestination());
	}
	
	protected StompHeaderAccessor getHeader(TextMessage message) {
		ByteBuffer payload = ByteBuffer.wrap(message.getPayload().getBytes(UTF_8));
		List<Message<byte[]>> messages = this.decoder.decode(payload);
		
		return StompHeaderAccessor.wrap(messages.get(0));
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		LOGGER.debug("<<<<<: {}", message.getPayload());
		
		ByteBuffer payload = ByteBuffer.wrap(message.getPayload().getBytes(UTF_8));
		List<Message<byte[]>> messages = this.decoder.decode(payload);

		for (Message<byte[]> msg : messages) {
			StompHeaderAccessor headers = StompHeaderAccessor.wrap(msg);
			
			if (StompCommand.CONNECTED.equals(headers.getCommand())) {
				
				LOGGER.info("<< Connected!!");

			} else if (StompCommand.MESSAGE.equals(headers.getCommand())) {
				
				String content = new String(msg.getPayload());
				
				JsonNode node = mapper.readTree(content);
				JsonNode dataNode = node.get("data"); // data of SimpleJsonResponse
				
				
				if (dataNode instanceof ArrayNode) {
					instanceConfigs = (ArrayNode)dataNode;
					LOGGER.debug("MESSAGE instance size : {}", instanceConfigs.size());
					
				}else if (dataNode instanceof ObjectNode) {
					ObjectNode cmdObj = (ObjectNode)dataNode;
					
					JsonNode cmd = cmdObj.get("cmd");
					
					if (cmd == null) {
						LOGGER.warn("###### cmd is null.");
					} 
					
					if ("add_tomcat_instnace".equals(cmd.asText())) {
						LOGGER.debug("cmd is 'add_tomcat_instnace'");
						
						addInstanceConfig(cmdObj);
					} else if ("remove_tomcat_instnace".equals(cmd.asText())) {
						LOGGER.debug("cmd is 'remove_tomcat_instnace'");
						
						removeInstanceConfig(cmdObj);
					}
				}
				
				
				//this.stompMessageHandler.handleMessage(message);
			} else if (StompCommand.RECEIPT.equals(headers.getCommand())) {
				
				LOGGER.debug("RECEIPT!!");
				
				//this.stompMessageHandler.handleReceipt(headers.getReceiptId());
			} else if (StompCommand.ERROR.equals(headers.getCommand())) {
				
				LOGGER.debug("ERROR!!");
				
				//this.stompMessageHandler.handleError(message);
			} else {
				LOGGER.debug("Unhandled message " + message);
			}
		}
		
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		LOGGER.debug("Connection closed!!");
		this.webSocketClient.connect();
	}
	
	public synchronized ArrayNode getInstanceConfigs() {
		return instanceConfigs;
	}
	
	private synchronized void addInstanceConfig(ObjectNode cmdObj){
		
		ObjectNode instanceConfig = (ObjectNode)cmdObj.get("tomcatInstanceConfig");
		
		this.instanceConfigs.add(instanceConfig);
	}
	
	public synchronized void removeInstanceConfig(ObjectNode cmdObj){
		int removalInstanceId = cmdObj.get("tomcatInstanceId").asInt();
		removeInstanceConfig(removalInstanceId);
	}
	
	private void removeInstanceConfig(int removalInstanceId){
		boolean removed = false;
		for (Iterator<JsonNode> iterator = this.instanceConfigs.elements(); iterator.hasNext();) {
			JsonNode instanceConfig = iterator.next();
			
			int tomcatInstanceId = instanceConfig.get(MeerkatAgentConstants.JSON_KEY_TOMCAT_INSTANCE_ID).asInt();
			
			if (removalInstanceId == tomcatInstanceId) {
				iterator.remove();
				removed = true;
				LOGGER.info("## removed tomcatInstanceId : {}", removalInstanceId);
				break;
			}
		}
		
		if(removed == false) {
			LOGGER.warn("## remove fail. not found tomcatInstanceId : {}", removalInstanceId);
		}
	}
	
}
//end of AgentWebSocketHandler.java