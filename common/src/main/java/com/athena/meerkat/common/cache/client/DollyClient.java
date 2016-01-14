/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2014. 12. 23.		First Draft.
 */
package com.athena.dolly.common.cache.client;

import java.util.Enumeration;
import java.util.List;

import com.athena.dolly.common.cache.SessionKey;
import com.athena.dolly.common.stats.DollyStats;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public interface DollyClient {

	/**
	 * <pre>
	 * Cache에서 주어진 Key에 해당하는 Data를 조회한다.
	 * </pre>
	 * @param cacheKey
	 * @return
	 */
	public Object get(String cacheKey);
	
	/**
	 * <pre>
	 * Cache에서 Map 형태의 Data를 조회하기 위해 사용하며, 주어진 Cache Key / Data Key에 해당하는 Data를 조회한다. 
	 * </pre>
	 * @param cacheKey
	 * @param dataKey
	 * @return
	 */
	public Object get(String cacheKey, String dataKey);
	
	/**
	 * <pre>
	 * Cache에 주어진 Key / Value로 저장한다. 
	 * </pre>
	 * @param cacheKey
	 * @param value
	 * @throws Exception
	 */
	public void put(String cacheKey, Object value) throws Exception;
	
	/**
	 * <pre>
	 * Cache에서 Map 형태의 Data를 저정하기 위해 사용하며, 주어진 Cache Key / Data Key에 해당하는 Data를 저장한다. 
	 * </pre>
	 * @param cacheKey
	 * @param dataKey
	 * @param value
	 * @throws Exception
	 */
	public void put(String cacheKey, String dataKey, Object value) throws Exception;
	
	/**
	 * <pre>
	 * Cache에서 주어진 Key에 해당하는 Data를 제거한다.
	 * </pre>
	 * @param cacheKey
	 * @throws Exception
	 */
	public void remove(String cacheKey) throws Exception;
	
	/**
	 * <pre>
	 * Cache에서 Map 형태의 Data를 제거하기 위해 사용하며, 주어진 Cache Key / Data Key에 해당하는 Data를 제거한다. 
	 * </pre>
	 * @param cacheKey
	 * @param dataKey
	 * @throws Exception
	 */
	public void remove(String cacheKey, String dataKey) throws Exception;
	
	/**
	 * <pre>
	 * Map 형태의 Data에서 key 목록을 조회한다.
	 * </pre>
	 * @param cacheKey
	 * @return
	 */
	public Enumeration<String> getValueNames(String cacheKey);
	
	/**
	 * <pre>
	 * Cache에 저장된 Cache Key를 조회한다.
	 * </pre>
	 * @return
	 */
	public List<SessionKey> getKeys(String viewName);
	
	/**
	 * <pre>
	 * Cache를 중지한다.
	 * </pre>
	 */
	public void destory();
	
	/**
	 * <pre>
	 * Cache 통계정보를 조회한다.
	 * </pre>
	 * @return
	 */
	public DollyStats getStats();
	
	/**
	 * <pre>
	 * Debug용으로써 Cache 내에 존재하는 Data를 출력한다.
	 * </pre>
	 */
	public void printAllCache();
	
	/**
	 * <pre>
	 * 캐시 서버로의 연결 상태를 체크한다.
	 * </pre>
	 */
	public void healthCheck();
}
//end of DollyClient.java