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

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.athena.meerkat.controller.web.provisioning.TomcatProvisioningService;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
public class LogTailerListener extends TailerListenerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger("com.athena.meerkat.LogTailerListener");

	private Tailer tailer;
	private WebSocketSession session;
	private boolean isStop;
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public LogTailerListener(WebSocketSession session) {
		this.session = session;
	}
	
	@Override
	public void init(Tailer tailer) {
		this.tailer = tailer;
	}



	@Override
	public void handle(String line) {
		
		if(isStop){
			LOGGER.warn("trailer already was stopped!!");
			stop();
			return;
		}
			
		if(line == null) {
			LOGGER.warn("log is null!!");
		} else {
			try{
				this.session.sendMessage(new TextMessage(line));
			}catch(IOException e) {
				LOGGER.error(e.toString(),e);
			} finally {
				
				if(line.indexOf(TomcatProvisioningService.LOG_END) > -1){
					stop();
				}
			}
		}
	}

	@Override
	public void handle(Exception ex) {
		
		LOGGER.error(ex.toString(),ex);
	}
	
	protected void stop(){
		this.tailer.stop();
//		try{
//			this.session.close();
//		}catch(IOException e){
//			LOGGER.error(e.toString(), e);
//		}
		isStop = true;
		LOGGER.debug("tailer stop!!");
	}

	
	
}
//end of LogTailerListener.java