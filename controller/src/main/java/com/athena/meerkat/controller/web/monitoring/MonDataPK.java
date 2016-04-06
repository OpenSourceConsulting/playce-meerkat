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
 * BongJin Kwon		2016. 4. 6.		First Draft.
 */
package com.athena.meerkat.controller.web.monitoring;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
public class MonDataPK implements Serializable {

	private static final long serialVersionUID = 5808702600047332135L;
	
	private String monFactorId;//
	private int serverId;//
	private Date monDt;//
	
	public MonDataPK() {
		// TODO Auto-generated constructor stub
	}

	public String getMonFactorId() {
		return monFactorId;
	}

	public void setMonFactorId(String monFactorId) {
		this.monFactorId = monFactorId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public Date getMonDt() {
		return monDt;
	}

	public void setMonDt(Date monDt) {
		this.monDt = monDt;
	}

}
//end of MonDataPK.java