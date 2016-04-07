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
 * Sang-cheon Park	2013. 7. 29.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning.util;

/**
 * <pre>
 * 현재 시스템의 OS 타입을 판별하기 위한 유틸 클래스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public final class OSUtil {

	private static String operatingSystem = System.getProperty("os.name").toLowerCase();

	/**
	 * <pre>
	 * 현재 시스템의 OS 타입을 판별한다.
	 * </pre>
	 * @return
	 */
	public static OSType getOSName() {
		if (isWindows()) {
			return OSType.WINDOWS;
		} else if (isLinux()) {
			return OSType.LINUX;
		} else if (isUnix()) {
			return OSType.UNIX;
		} else if (isSolaris()) {
			return OSType.SUN;
		} else if (isMac()) {
			return OSType.MAC;
		} else {
			return OSType.ETC;
		}
	}//end of getOSName()

	/**
	 * <pre>
	 * WINDOWS 여부
	 * </pre>
	 * @return
	 */
	public static boolean isWindows() {
		return (operatingSystem.indexOf("win") >= 0);
	}//end of isWindows()

	/**
	 * <pre>
	 * LINUX 여부
	 * </pre>
	 * @return
	 */
	public static boolean isLinux() {
		return (operatingSystem.indexOf("linux") >= 0);
	}//end of isLinux()

	/**
	 * <pre>
	 * UNIX 여부
	 * </pre>
	 * @return
	 */
	public static boolean isUnix() {
		return (operatingSystem.indexOf("aix") >= 0 || operatingSystem.indexOf("hp") >= 0 || operatingSystem.indexOf("irix") > 0);
	}//end of isUnix()

	/**
	 * <pre>
	 * SUN OS 여부
	 * </pre>
	 * @return
	 */
	public static boolean isSolaris() {
		return (operatingSystem.indexOf("solaris") >= 0 || operatingSystem.indexOf("sunos") >= 0);
	}//end of isSolaris()

	/**
	 * <pre>
	 * MAC OS 여부
	 * </pre>
	 * @return
	 */
	public static boolean isMac() {
		return (operatingSystem.indexOf("mac") >= 0);
	}//end of isMac()
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @author Sang-cheon Park
	 * @version 1.0
	 */
	public enum OSType {
		WINDOWS,
		LINUX,
		UNIX,
		MAC,
		SUN,
		ETC;
		
	    public String value() {
	        return name();
	    }

	    public static OSType fromValue(String value) {
	        return valueOf(value);
	    }
	}
	//end of OSType

}
//end of OSUtil.java