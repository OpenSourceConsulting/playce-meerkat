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

import java.io.IOException;
import java.util.List;

import javax.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.MeerkatResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Component
public class JsonHttpMessageConverter extends MappingJackson2HttpMessageConverter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonHttpMessageConverter.class);
	
	@Autowired
	private CommonCodeHandler codeHandler;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public JsonHttpMessageConverter() {
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param objectMapper
	 */
	public JsonHttpMessageConverter(ObjectMapper objectMapper) {
		super(objectMapper);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		
		if(object instanceof DataSource) {
			handleEntity((DataSource)object);
		
		} else if (object instanceof SimpleJsonResponse) {
			LOGGER.debug("Skip convert for {}", object.getClass().getName());
			
		} else if (object instanceof GridJsonResponse) {
			LOGGER.debug("Skip convert for {}", object.getClass().getName());
			
		} else {
			LOGGER.debug("Not found convert method for {}", object.getClass().getName());
		}
		
		super.writeInternal(object, outputMessage);
	}

	protected void handleEntity(DataSource entity) {
		entity.setDbTypeName(codeHandler.getCodeNm(MeerkatConstants.DBTYPE_PARENT_CODE_VALUE, entity.getDbType()));
		
		LOGGER.debug("converted for {}", entity.getClass().getName());
	}
	
	

}
//end of JsonHttpMessageConverter.java