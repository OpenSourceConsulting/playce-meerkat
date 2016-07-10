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
 * BongJin Kwon		2016. 7. 10.		First Draft.
 */
package com.athena.meerkat.controller.web.tomcat.viewmodels;

/**
 * <pre>
 * tomcat instance application view model.
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
public class TomcatInstanceAppModel {
	
	private String perm;
	private String own;
	private String date;
	private String appName;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public TomcatInstanceAppModel() {
		
	}

	public String getPerm() {
		return perm;
	}

	public void setPerm(String perm) {
		this.perm = perm;
	}

	public String getOwn() {
		return own;
	}

	public void setOwn(String own) {
		this.own = own;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
//end of TomcatInstanceAppModel.java