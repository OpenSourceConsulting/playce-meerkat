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
 * BongJin Kwon		2016. 6. 30.		First Draft.
 */
package com.athena.meerkat.common.tomcat;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.athena.meerkat.common.util.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * <pre>
 * 톰캣 버전별 properties 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Component
public class TomcatVersionsProperties implements InitializingBean {
	
	@Value("${meerkat.tomcat.code.versions}")
	private String tomcatCodeVersions;
	
	@Value("${meerkat.tomcat.jmx.thread.attrs}")
	private String tomcatJmxThreadAttrs;
	
	@Value("${meerkat.tomcat.dbcp.maxative}")
	private String tomcatDBCPMaxActives;
	
	@Value("${meerkat.tomcat.dbcp.maxwait}")
	private String tomcatDBCPMaxWaits;
	
	private Map<Integer, Integer> tomcatCodeVersionMap;
	private Map<Integer, String> tomcatJmxThreadMap;
	private Map<Integer, String> tomcatDBCPMaxActiveMap;
	private Map<Integer, String> tomcatDBCPMaxWaitMap;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public TomcatVersionsProperties() {
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		Assert.hasLength(tomcatCodeVersions);
		Assert.hasLength(tomcatJmxThreadAttrs);
		Assert.hasLength(tomcatDBCPMaxActives);
		Assert.hasLength(tomcatDBCPMaxWaits);
		
		tomcatCodeVersionMap = JSONUtil.jsonToObj(tomcatCodeVersions, new TypeReference<Map<Integer, Integer>>(){});
		tomcatJmxThreadMap = JSONUtil.jsonToObj(tomcatJmxThreadAttrs, new TypeReference<Map<Integer, String>>(){});
		tomcatDBCPMaxActiveMap = JSONUtil.jsonToObj(tomcatDBCPMaxActives, new TypeReference<Map<Integer, String>>(){});
		tomcatDBCPMaxWaitMap = JSONUtil.jsonToObj(tomcatDBCPMaxWaits, new TypeReference<Map<Integer, String>>(){});
		
	}
	
	public Integer getTomcatMajorVersion(int tomcatVersionCd) {
		return this.tomcatCodeVersionMap.get(tomcatVersionCd);
	}

	public String getTomcatThreadAttr(int tomcatVersionCd) {
		
		Integer version = getTomcatMajorVersion(tomcatVersionCd);
		
		return this.tomcatJmxThreadMap.get(version);
	}

	public String getDBCPMaxActive(int tomcatVersionCd) {
		Integer version = getTomcatMajorVersion(tomcatVersionCd);
		
		return this.tomcatDBCPMaxActiveMap.get(version);
	}
	
	public String getDBCPMaxWait(int tomcatVersionCd) {
		Integer version = getTomcatMajorVersion(tomcatVersionCd);
		return this.tomcatDBCPMaxWaitMap.get(version);
	}


}
//end of TomcatJmxAttributesHandler.java