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
 * Sang-cheon Park	2014. 3. 31.		First Draft.
 */
package com.athena.meerkat.controller.module.vo;

import java.io.Serializable;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class MemoryVo implements Serializable {

	private static final long serialVersionUID = 2801088346407306730L;
	
	private Long committed;
	private Long init;
	private Long max;
	private Long used;
	
	/**
	 * @return the committed
	 */
	public Long getCommitted() {
		return committed;
	}
	
	/**
	 * @param committed the committed to set
	 */
	public void setCommitted(Long committed) {
		this.committed = committed;
	}
	
	/**
	 * @return the init
	 */
	public Long getInit() {
		return init;
	}
	
	/**
	 * @param init the init to set
	 */
	public void setInit(Long init) {
		this.init = init;
	}
	
	/**
	 * @return the max
	 */
	public Long getMax() {
		return max;
	}
	
	/**
	 * @param max the max to set
	 */
	public void setMax(Long max) {
		this.max = max;
	}
	
	/**
	 * @return the used
	 */
	public Long getUsed() {
		return used;
	}
	
	/**
	 * @param used the used to set
	 */
	public void setUsed(Long used) {
		this.used = used;
	}
}
//end of MemoryVo.java