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
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompEncoder;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

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
	
	private String initDestHeader;
	private String destHeader;

	private WebSocketSession session;
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public StompWebSocketClient() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		connect();
	}
	
	public void connect() throws Exception {
		
		StandardWebSocketClient wsClient = new StandardWebSocketClient();
		
		LOGGER.info("connecting to {}", endpoint);
		
		this.session = wsClient.doHandshake(new StompWebSocketHandler(topic), endpoint).get();
		
		
		Assert.notNull(session);
		Assert.isTrue(session.isOpen(), "server connection fail.");
		Assert.notNull(appInitDestination);
		Assert.notNull(appDestination);
		
		this.initDestHeader = "destination:" + appInitDestination;;
		this.destHeader = "destination:" + appDestination;
		
		LOGGER.info("connected");
	}
	
	public void sendInitMessage(String message) throws IOException {
		TextMessage txtMessage = StompTextMessageBuilder.create(StompCommand.SEND).headers(this.initDestHeader).body(message).build();
		session.sendMessage(txtMessage); 
		
		LOGGER.debug("send >> {}", message);
	}
	
	public void sendMessage(String message) throws IOException {
		TextMessage txtMessage = StompTextMessageBuilder.create(StompCommand.SEND).headers(this.destHeader).body(message).build();
		session.sendMessage(txtMessage); 
		
		LOGGER.debug("send >> {}", message);
	}
	
	public void disconnect() throws IOException {
		TextMessage msg = StompTextMessageBuilder.create(StompCommand.DISCONNECT).build();
		this.session.sendMessage(msg);
		this.session.close();
		this.session = null;
	}

}
//end of StompClient.java