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
 * Sang-cheon Park	2013. 9. 23.		First Draft.
 */
package com.athena.meerkat.agent.util;

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