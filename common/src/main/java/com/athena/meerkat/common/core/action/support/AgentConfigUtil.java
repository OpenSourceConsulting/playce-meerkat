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
 * Sang-cheon Park	2013. 8. 1.		First Draft.
 */
package com.athena.meerkat.common.core.action.support;

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