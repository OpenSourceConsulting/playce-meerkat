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

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Component
public class StompWebSocketClient implements InitializingBean{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StompWebSocketClient.class);
	
	public static final byte[] EMPTY_PAYLOAD = new byte[0]; 
	private static final Charset UTF_8 = Charset.forName("UTF-8"); 
	
	@Value("${meerkat.agent.server.topic}")
	private String topic;
	
	@Value("${meerkat.agent.server.endpoint}")
	private String endpoint;
	
	@Value("${meerkat.agent.server.app.init}")
	private String appInitDestination;
	
	@Value("${meerkat.agent.server.app.dest}")
	private String appDestination;
	
	@Value("${meerkat.agent.jmx.app.dest}")
	private String appJmxDestination;
	
	@Value("${meerkat.agent.server.app.fs}")
	private String appFSDestination;
	
	@Value("${meerkat.agent.reconnect.interval:10000}")
	private long reconnectInterval;
	
	@Value("${meerkat.agent.server.id:0}")
    private String serverId;
	
	private String initDestHeader;
	private String jmxDestHeader;
	private String serverDestHeader;
	private String fsDestHeader;

	private WebSocketSession session;
	
	private StompWebSocketHandler webSocketHandler;
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public StompWebSocketClient() {
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		webSocketHandler = new StompWebSocketHandler("subs_" + serverId, this, topic);
		connect();
	}
	
	public ArrayNode getInstanceConfigs(){
		return webSocketHandler.getInstanceConfigs();
	}
	
	public void connect() throws Exception {
		
		StandardWebSocketClient wsClient = new StandardWebSocketClient();
		
		LOGGER.info("connecting to {}", endpoint);
		
		doConnect(wsClient);
		
		Assert.notNull(session);
		Assert.isTrue(session.isOpen(), "server connection fail.");
		Assert.notNull(appInitDestination);
		Assert.notNull(appDestination);
		
		this.initDestHeader = "destination:" + appInitDestination;;
		this.serverDestHeader = "destination:" + appDestination;
		this.jmxDestHeader = "destination:" + appJmxDestination;
		this.fsDestHeader = "destination:" + appFSDestination;
		
		LOGGER.info("connected");
	}
	
	protected void doConnect(StandardWebSocketClient wsClient) {
		
		try{
			this.session = wsClient.doHandshake(webSocketHandler, endpoint).get();
		}catch(Exception e) {
			LOGGER.error(e.toString());
			try{
				Thread.sleep(reconnectInterval);
			}catch(InterruptedException ex){
				//ignore.
			}
			doConnect(wsClient);
		}
	}
	
	/**
	 * <pre>
	 * send server init data(message)
	 * </pre>
	 * @param message
	 * @throws IOException
	 */
	public void sendInitMessage(String message) throws IOException {
		TextMessage txtMessage = StompTextMessageBuilder.create(StompCommand.SEND).headers(this.initDestHeader).body(message).build();
		session.sendMessage(txtMessage); 
		
		LOGGER.debug("send init >> {}", message);
	}
	
	/**
	 * <pre>
	 * send server monitoring data(message)
	 * </pre>
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException {
		TextMessage txtMessage = StompTextMessageBuilder.create(StompCommand.SEND).headers(this.serverDestHeader).body(message).build();
		
		try {
			session.sendMessage(txtMessage); 
		} catch (IllegalArgumentException e) {
			if(e.getMessage().endsWith("after connection closed.")) {
				disconnect();
			}
			throw e;
		}
		
		LOGGER.debug("send server >> {}", message);
	}
	
	/**
	 * <pre>
	 * send tomcat instance monitoring data(message)
	 * - instance status
	 * - jmx monitoring data
	 * </pre>
	 * @param message
	 * @throws IOException
	 */
	public void sendJmxMessage(String message) throws IOException {
		TextMessage txtMessage = StompTextMessageBuilder.create(StompCommand.SEND).headers(this.jmxDestHeader).body(message).build();
		
		try {
			session.sendMessage(txtMessage); 
		} catch (IllegalArgumentException e) {
			if(e.getMessage().endsWith("after connection closed.")) {
				disconnect();
			}
			throw e;
		}
		
		LOGGER.debug("send jmx >> {}", message);
	}
	
	public void sendFSMessage(String message) throws IOException {
		TextMessage txtMessage = StompTextMessageBuilder.create(StompCommand.SEND).headers(this.fsDestHeader).body(message).build();
		
		try {
			session.sendMessage(txtMessage); 
		} catch (IllegalArgumentException e) {
			if(e.getMessage().endsWith("after connection closed.")) {
				disconnect();
			}
			throw e;
		}
		
		LOGGER.debug("send fs >> {}", message);
	}
	
	public void disconnect() throws IOException {
		TextMessage msg = StompTextMessageBuilder.create(StompCommand.DISCONNECT).build();
		this.session.sendMessage(msg);
		this.session.close();
		this.session = null;
	}
	
	/**
	 * <pre>
	 * Return whether the connection is still open.
	 * </pre>
	 * @return
	 */
	public boolean isOpen() {
		return this.session != null && this.session.isOpen();
	}

}
//end of StompClient.java