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
 * BongJin Kwon		2016. 4. 12.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning.log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketSessionMock implements WebSocketSession {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketSessionMock.class);

	public WebSocketSessionMock() {
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public URI getUri() {
		return null;
	}

	@Override
	public HttpHeaders getHandshakeHeaders() {
		return null;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Principal getPrincipal() {
		return null;
	}

	@Override
	public InetSocketAddress getLocalAddress() {
		return null;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return null;
	}

	@Override
	public String getAcceptedProtocol() {
		return null;
	}

	@Override
	public void setTextMessageSizeLimit(int messageSizeLimit) {

	}

	@Override
	public int getTextMessageSizeLimit() {
		return 0;
	}

	@Override
	public void setBinaryMessageSizeLimit(int messageSizeLimit) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBinaryMessageSizeLimit() {
		return 0;
	}

	@Override
	public List<WebSocketExtension> getExtensions() {
		return null;
	}

	@Override
	public void sendMessage(WebSocketMessage<?> message) throws IOException {
		LOGGER.debug(message.toString());

	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public void close() throws IOException {
		LOGGER.debug("session was closed!!");

	}

	@Override
	public void close(CloseStatus status) throws IOException {

	}

}
//end of WebSocketSessionMock.java