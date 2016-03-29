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
 * BongJin Kwon		2016. 3. 29.		First Draft.
 */
package com.athena.meerkat.controller.web.common.converter;

import java.util.List;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.common.util.WebUtil;
import com.athena.meerkat.controller.web.entities.DataSource;

public class HandlerMethodEntityReturnValueHandler implements
		HandlerMethodReturnValueHandler {
	
	private CommonCodeHandler codeHandler;

	public HandlerMethodEntityReturnValueHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		
		return isEntityType(returnType) || isListType(returnType);
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		
		if (codeHandler == null) {
			WebApplicationContext appContext = WebUtil.getWebApplicationContext(webRequest.getNativeRequest(HttpServletRequest.class));
			codeHandler = appContext.getBean(CommonCodeHandler.class);
		}
		
		
		if (isEntityType(returnType)) {
			
			if(returnValue instanceof DataSource) {
				handleEntity((DataSource)returnValue);
			}
			
		} else if (isListType(returnType)) {
			
			
		}
	}
	
	protected void handleEntity(DataSource ds) {
		ds.setDbTypeName(codeHandler.getCodeNm(MeerkatConstants.DBTYPE_PARENT_CODE_VALUE, ds.getDbType()));
	}
	
	
	protected boolean isEntityType(MethodParameter returnType) {
		
		return Entity.class.isAssignableFrom(returnType.getAnnotatedElement().getClass());
	}
	
	protected boolean isListType(MethodParameter returnType) {
		
		return List.class.isAssignableFrom(returnType.getClass());
	}
	
	

}
//end of HandlerMethodEntityReturnValueHandler.java