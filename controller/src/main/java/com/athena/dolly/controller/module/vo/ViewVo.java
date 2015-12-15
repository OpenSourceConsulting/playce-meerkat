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
 * Sang-cheon Park	2015. 1. 7.		First Draft.
 */
package com.athena.dolly.controller.module.vo;

import java.io.Serializable;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class ViewVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String viewName;
	private String map;
	private String reduce;
	
	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}
	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	/**
	 * @return the map
	 */
	public String getMap() {
		return map;
	}
	/**
	 * @param map the map to set
	 */
	public void setMap(String map) {
		this.map = map;
	}
	/**
	 * @return the reduce
	 */
	public String getReduce() {
		return reduce;
	}
	/**
	 * @param reduce the reduce to set
	 */
	public void setReduce(String reduce) {
		this.reduce = reduce;
	}
	@Override
	public String toString() {
		return "viewName : " + viewName +
				"\nMap : " + map +
				"\nReduce : " + reduce + "\n";
	}
}
//end of ViewVo.java