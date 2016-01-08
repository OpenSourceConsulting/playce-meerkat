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
 * Sang-cheon Park	2013. 9. 23.		First Draft.
 */
package com.athena.meerkat.common.core.action.support;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * config 하위의 프로퍼티 파일을 로드하여 주어진 key에 해당하는 value 값을 조회하기 위해 사용하는 유틸 클래스.
 * <b>Spring util:property를 사용한 프로퍼트 값 조회도 가능</b>
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class PropertyUtil {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PropertyUtil.class);

	private static Properties[] properties = new Properties[0];
	private static Exception exception;

	static {
		try {
			Properties prop = null;

			File file = new File(PropertyUtil.class.getResource("/config/")
					.getFile());
			File[] files = null;

			if (file != null && file.isDirectory()) {
				files = file.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".properties");
					}
				});

				if (files != null) {
					for (File f : files) {
						prop = new Properties();
						prop.load(new BufferedInputStream(
								new FileInputStream(f)));

						properties = (Properties[]) ArrayUtils.add(properties,
								prop);
					}
				}
			} else {
				// jar 배포 환경에서 java -jar로 agent를 실행할 경우..
				prop = new Properties();
				prop.load(PropertyUtil.class
						.getResourceAsStream("/config/context.properties"));

				properties = (Properties[]) ArrayUtils.add(properties, prop);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("Property file(s) are not exist.", e);
			exception = e;
		} catch (IOException e) {
			LOGGER.error("Property file(s) or directory cannot read.", e);
			exception = e;
		} catch (Exception e) {
			LOGGER.error("Unhandled exception has occurred.", e);
			exception = e;
		}
	}

	/**
	 * <pre>
	 * 주어진 key에 해당하는 property 값을 반환한다.
	 * 중복된 key가 존재할 경우 첫 번째 값을 반환한다.
	 * </pre>
	 * 
	 * @param key
	 * @return value
	 * @throws Exception
	 *             프로퍼티 파일을 정상적으로 load 하지 못한 상태
	 */
	public static String getProperty(String key) throws Exception {
		if (exception != null) {
			throw exception;
		}

		String value = null;
		for (Properties p : properties) {
			value = p.getProperty(key);
			if (value != null) {
				break;
			}
		}

		return value;
	}// end of getProperty()
}
// end of PropertyUtil.java