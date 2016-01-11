/* 
 * Copyright 2013 The Athena-Peacock Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 8. 20.		First Draft.
 */
package com.athena.meerkat.agent.netty;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.athena.meerkat.common.netty.MeerkatDatagram;

/**
 * <pre>
 * Meerkat Server로 초기 시스템 정보 및 모니터링 정보, Provisioning 관련 제어 메시지에 대한 처리 결과를 송신하기 위한 클래스
 * MeerkatClientHandler를 직접 호출하지 않기 위해 구현
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("meerkatTransmitter")
public class MeerkatTransmitter {

	@Inject
	@Named("meerkatClientHandler")
	private MeerkatClientHandler handler;

	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param datagram
	 * @throws Exception
	 */
	public void sendMessage(MeerkatDatagram<?> datagram) throws Exception {
		if (handler.isConnected()) {
			handler.sendMessage(datagram);
		} else {
			throw new Exception("Connection closed."); // NOPMD
		}
	}// end of sendMessage()

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return handler.isConnected();
	}

}
// end of MeerkatTransmitter.java