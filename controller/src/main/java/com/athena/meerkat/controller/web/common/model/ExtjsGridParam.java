/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Bong-Jin Kwon	2013. 9. 25.		First Draft.
 */
package com.athena.meerkat.controller.web.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *  Extjs GridPanel 의 request parameter Model
 *   - Extjs GridPanel 목록 조회 처리 Controller 에서 사용함. 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
public class ExtjsGridParam {
	
	private int page;
	private int start;
	private int limit;
	private String search;//검색어
	private Map<String, Object> exParams = new HashMap<String, Object>();

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public ExtjsGridParam() {
		// TODO Auto-generated constructor stub
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	
	public void addExParam(String key, Object val){
		this.exParams.put(key, val);
	}

	public Map<String, Object> getExParams() {
		return exParams;
	}
	

}
//end of ExtjsGridParam.java