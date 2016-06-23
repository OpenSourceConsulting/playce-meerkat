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
 * BongJin Kwon		2016. 6. 23.		First Draft.
 */
package com.athena.meerkat.controller.web.monitoring;

import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

/**
 * <pre>
 * STOMP channel interceptor.
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
public class MonChannelInterceptor extends ChannelInterceptorAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MonChannelInterceptor.class);
	
	private static final String MDC_MON_KEY = "monitor";
	private static final String MDC_MON_VAL = "monitor";

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public MonChannelInterceptor() {

	}


	@Override
	public boolean preReceive(MessageChannel channel) {
		LOGGER.debug("========================== preReceive");
		//MDC.put(MDC_MON_KEY, MDC_MON_VAL);//not working
		return super.preReceive(channel);
	}


	@Override
	public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
		LOGGER.debug("========================== afterReceiveCompletion");
		//MDC.put(MDC_MON_KEY, MDC_MON_VAL);//not working
	}
	
	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		LOGGER.debug("========================== afterSendCompletion");
		//MDC.remove(MDC_MON_KEY);
	}

}
//end of MonChannelInterceptor.java