/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2013. 8. 20.		First Draft.
 */
package com.athena.meerkat.controller.netty;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.athena.meerkat.common.netty.MeerkatDatagram;
import com.athena.meerkat.common.netty.message.AbstractMessage;
import com.athena.meerkat.common.netty.message.ProvisioningResponseMessage;

/**
 * <pre>
 * Peacock Server와 Peacock Agents 사이의 Provisioning 관련 제어 메시지를 송신하기 위한 클래스
 * PeacockServerHandler를 직접 호출하지 않기 위해 구현
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("meerkatTransmitter")
public class MeerkatTransmitter {

    @Inject
    @Named("meerkatServerHandler")
    private MeerkatServerHandler handler;
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param datagram
	 * @throws Exception 
	 */
    public ProvisioningResponseMessage sendMessage(MeerkatDatagram<AbstractMessage> datagram) throws Exception {
		return handler.sendMessage(datagram);
	}//end of sendMessage()
    
    /**
     * <pre>
     * 
     * </pre>
     * @param agentId
     * @return
     */
    public boolean isActive(String agentId) {
    	return handler.isActive(agentId);
    }//end of isActive()
    
    /**
     * <pre>
     * 
     * </pre>
     * @param agentId
     */
    public void channelClose(String agentId) {
    	handler.channelClose(agentId);
    }//end of channelClose()
}
//end of MeerkatTransmitter.java