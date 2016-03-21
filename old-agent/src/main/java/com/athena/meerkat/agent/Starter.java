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
 * Sang-cheon Park	2013. 7. 30.		First Draft.
 */
package com.athena.meerkat.agent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import com.athena.meerkat.agent.util.AgentConfigUtil;
import com.athena.meerkat.agent.util.MeerkatAgentIDGenerator;
import com.athena.meerkat.agent.util.PropertyUtil;
import com.athena.meerkat.common.constant.MeerkatConstant;

/**
 * <pre>
 * Meerkat Agent 구동을 위한 Starter 클래스로써 다음과 같은 작업을 수행한다.
 * 	<ul>
 * 		<li>config/context.properties 파일에 명시된 ${meerkat.agent.config.file.name} 파일로부터 설정 정보를 load 한다.</li>
 * 		<li>Agent ID 확인을 위해 ${meerkat.agent.agent.file.name} 파일 존재 여부를 확인하며, 해당 파일이 없을 경우 신규 Agent ID를 생성하고 파일에 저장한다.</li>
 * 		<li>Spring Context 파일을 load 함으로써 초기 시스템 정보 및 Quartz에 의한 1분 단위 시스템 모니터링을 수행하고 서버로 전송한다.</li>
 * 	</ul>
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class Starter {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		int rand = (int) (Math.random() * 100) % 50;
		System.setProperty("random.seconds", Integer.toString(rand));
		
		String configFile = null;
		
		try {
			configFile = PropertyUtil.getProperty(MeerkatConstant.CONFIG_FILE_KEY);
		} catch (Exception e) {
			LOGGER.error("Agent starting error", e);
		} finally {
			if (StringUtils.isEmpty(configFile)) {
				configFile = "/meerkat/agent/config/agent.conf";
			}
		}
		
		/**
		 * ${meerkat.agent.config.file.name} 파일을 load 중 에러가 발생했는지의 여부를 검사하고 에러 발생 시 프로그램을 종료한다.
		 */
		String errorMsg = "\n\"" + configFile + "\" file does not exist or cannot read.\n"
						+ "Please check \"" + configFile + "\" file exists and can read.";
		
		Assert.isTrue(AgentConfigUtil.exception == null, errorMsg);
		Assert.notNull(AgentConfigUtil.getConfig(MeerkatConstant.SERVER_IP), "ServerIP cannot be empty.");
		Assert.notNull(AgentConfigUtil.getConfig(MeerkatConstant.SERVER_PORT), "ServerPort cannot be empty.");
		
		/**
		 * Agent ID 확인을 위해 ${meerkat.agent.agent.file.name} 파일 존재 여부를 확인하며, 
		 * 해당 파일이 없을 경우 신규 Agent ID를 생성하고 파일에 저장한다.
		 */
		String agentFile = null;
		String agentId = null;
		
		try {
			agentFile = PropertyUtil.getProperty(MeerkatConstant.AGENT_ID_FILE_KEY);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (StringUtils.isEmpty(agentFile)) {
				agentFile = "/meerkat/agent/.agent";
			}
		}
		
		if (StringUtils.isEmpty(agentFile)) {
			agentFile = "/meerkat/agent/.agent";
		}
		
		File file = new File(agentFile);
		boolean isNew = false;
		
		if(file.exists()) {
			try {
				agentId = IOUtils.toString(file.toURI());
				
				// 파일 내에 agent ID가 비어있거나 agent ID의 길이가 36이 아닐 경우 신규로 생성한다.
				if(StringUtils.isEmpty(agentId) || agentId.length() != 36) {
					throw new IOException();  // NOPMD
				}
			} catch (IOException e) {
				LOGGER.error(agentFile + " file cannot read or saved invalid agent ID.", e);
	            
	            agentId = MeerkatAgentIDGenerator.generateId();
	            isNew = true;
			}
		} else {
			agentId = MeerkatAgentIDGenerator.generateId();
			isNew = true;
		}
		
		if(isNew) {
			LOGGER.info("New Agent-ID({}) be generated.", agentId);
            
            try {
    			file.setWritable(true);
				OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(file));
				output.write(agentId);
				file.setReadOnly();
				IOUtils.closeQuietly(output);
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("UnsupportedEncodingException has occurred : ", e);
			} catch (FileNotFoundException e) {
				LOGGER.error("FileNotFoundException has occurred : ", e);
			} catch (IOException e) {
				LOGGER.error("IOException has occurred : ", e);
			}
		}
		
		// Spring Application Context Loading
		LOGGER.debug("Starting application context...");
        AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/context-*.xml");
        applicationContext.registerShutdownHook();
	}//end of main()

}
//end of Starter.java