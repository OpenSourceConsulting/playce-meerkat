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
 * Bong-Jin Kwon	2013. 9. 27.		First Draft.
 */
package com.athena.meerkat.controller.web.common.model;

/**
 * <pre>
 * 서버 C,U,D 작업 결과에 대한 JSON Response 클래스.
 * - ex) {"success": true, "msg":"Create sucess"}
 * - 특히 Extjs form.Panel 에서 submit() 함수 사용시 필수 사용해야함.
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
public class SimpleJsonResponse {
	
	/**
	 * 서버 작업 성공여부
	 */
	private boolean success = true;
	
	/**
	 * 서버 작업 결과 메시지.
	 */
	private String msg;
	
	/**
	 * 부가 정보.
	 */
	private Object data;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public SimpleJsonResponse() {
		// TODO Auto-generated constructor stub
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
//end of SimpleJsonResponse.java