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
 * Sang-cheon Park	2013. 7. 26.		First Draft.
 */
package com.athena.meerkat.agent.util;

import java.util.UUID;

/**
 * <pre>
 * Meerkat Agent의 ID 생성을 위한 제너레이터 클래스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public final class MeerkatAgentIDGenerator {
	
	/**
	 * <pre>
	 * UUID 생성 후 마지막 12자리는 해당 Agent System의 Mac Address로 치환한다.
	 * </pre>
	 * @return
	 */
	public static String generateId() {
		String id = null;
		
		String uuid = UUID.randomUUID().toString();
		String hardware = null;
		
		try {
			// Agent의 mac address 목록 중 가장 큰 값을 하나 가져온다.
			//hardware = MacAddressUtil.getMacAddressList().get(0);
			hardware = MacAddressUtil.getMacAddress("eth0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (hardware != null && hardware.length() == 12) {
			// 생성된 UUID의 맨 뒤 12자리를 잘라내고 가져온 mac address를 추가한다.
			id = uuid.substring(0, 24) + hardware;
		} else {
			id = uuid;
		}
		
		return id;
	}//end of generateId()
}
//end of MeerkatAgentIDGenerator.java