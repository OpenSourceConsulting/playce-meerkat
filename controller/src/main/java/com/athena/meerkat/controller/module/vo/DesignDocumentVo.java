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
package com.athena.meerkat.controller.module.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class DesignDocumentVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String designDocumentName;
	private List<ViewVo> viewList;
	
	/**
	 * @return the designDocumentName
	 */
	public String getDesignDocumentName() {
		return designDocumentName;
	}
	/**
	 * @param designDocumentName the designDocumentName to set
	 */
	public void setDesignDocumentName(String designDocumentName) {
		this.designDocumentName = designDocumentName;
	}
	/**
	 * @return the viewList
	 */
	public List<ViewVo> getViewList() {
		if (this.viewList == null) {
			this.viewList = new ArrayList<ViewVo>();
		}
		
		return viewList;
	}
	/**
	 * @param viewList the viewList to set
	 */
	public void setViewList(List<ViewVo> viewList) {
		this.viewList = viewList;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("designDocumentName : ").append(designDocumentName).append("\n");
		
		for (ViewVo view : viewList) {
			sb.append(view.toString());
		}
		
		return sb.append("\n").toString();
	}
}
//end of DesignDocumentVo.java