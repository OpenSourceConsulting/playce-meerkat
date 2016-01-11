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
 * Sang-cheon Park	2013. 10. 24.		First Draft.
 */
package com.athena.meerkat.common.netty.message;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class OSPackageInfoMessage extends AbstractMessage {

	private static final long serialVersionUID = 1L;
	
	private List<PackageInfo> packageInfoList;

	public OSPackageInfoMessage(MessageType messageType) {
		super(MessageType.PACKAGE_INFO);
		messageType.name();
	}

	/**
	 * @return the packageInfoList
	 */
	public List<PackageInfo> getPackageInfoList() {
		if (packageInfoList == null) {
			packageInfoList = new ArrayList<PackageInfo>();
		}
		return packageInfoList;
	}

	/**
	 * @param packageInfo the packageInfo to add
	 */
	public void addPackageInfo(PackageInfo packageInfo) {
		getPackageInfoList().add(packageInfo);
	}

	/**
	 * @param packageInfoList the packageInfoList to set
	 */
	public void setPackageInfoList(List<PackageInfo> packageInfoList) {
		this.packageInfoList = packageInfoList;
	}
}
//end of OSPackageInfoMessage.java