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
 * Sang-cheon Park	2013. 8. 1.		First Draft.
 */
package com.athena.meerkat.agent.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.meerkat.common.constant.MeerkatConstant;

/**
 * <pre>
 * agent.conf 파일을 로드하여 주어진 key에 해당하는 value 값을 조회하기 위해 사용하는 유틸 클래스.
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class AgentConfigUtil {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AgentConfigUtil.class);

	private static Properties prop;
	public static Exception exception;

	static {
		try {
			String configFile = PropertyUtil
					.getProperty(MeerkatConstant.CONFIG_FILE_KEY);
			prop = new Properties();
			prop.load(new BufferedInputStream(new FileInputStream(new File(
					configFile))));
		} catch (FileNotFoundException e) {
			LOGGER.error("agent.conf file does not exist.", e);
			exception = e;
		} catch (IOException e) {
			LOGGER.error("agent.conf file cannot read.", e);
			exception = e;
		} catch (Exception e) {
			LOGGER.error("Unhandled exception has occurred.", e);
			exception = e;
		}
	}

	/**
	 * <pre>
	 * 주어진 key에 해당하는 Config 값을 반환한다.
	 * </pre>
	 * 
	 * @param key
	 * @return value
	 */
	public static String getConfig(String key) {
		return prop.getProperty(key);
	}// end of getConfig()

}
// end of AgentConfigUtil.java