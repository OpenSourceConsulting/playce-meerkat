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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.viewmodels.TomcatInstanceViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bongjin Kwon
 * @version 1.0
 */
@Component
public class JsonHttpMessageConverter extends
		MappingJackson2HttpMessageConverter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JsonHttpMessageConverter.class);

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
	 * 
	 * @param objectMapper
	 */
	public JsonHttpMessageConverter(ObjectMapper objectMapper) {
		super(objectMapper);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		if (object instanceof SimpleJsonResponse) {
			handleResponse((SimpleJsonResponse) object);

		} else if (object instanceof GridJsonResponse) {
			handleResponse((GridJsonResponse) object);

		} else if (object instanceof List) {
			List<Object> list = (List<Object>) object;

			if (list.size() > 0) {

				for (Object obj : list) {
					handleObject(obj);
				}
			}

		} else {

			handleObject(object);
		}

		super.writeInternal(object, outputMessage);
	}

	protected void handleObject(Object object) {

		if (object instanceof DataSource) {
			handleEntity((DataSource) object);

		} else if (object instanceof TomcatInstance) {
			handleEntity((TomcatInstance) object);

		} else if (object instanceof TomcatInstanceViewModel) {
			handleEntity((TomcatInstanceViewModel) object);

		} else {
			LOGGER.debug("Not found convert method for {}", object.getClass()
					.getName());
		}
	}

	protected void handleEntity(DataSource entity) {
		entity.setDbTypeName(codeHandler.getCodeNm(
				MeerkatConstants.CODE_GROP_DB_TYPE, entity.getDbType()));

		LOGGER.debug("converted for {}", entity.getClass().getName());
	}

	protected void handleEntity(TomcatInstance entity) {
		entity.setStateNm(codeHandler.getCodeNm(
				MeerkatConstants.CODE_GROP_TS_STATE, entity.getState()));

		LOGGER.debug("converted for {}", entity.getClass().getName());
	}

	protected void handleEntity(TomcatInstanceViewModel entity) {
		entity.setStateNm(codeHandler.getCodeNm(
				MeerkatConstants.CODE_GROP_TS_STATE, entity.getState()));

		LOGGER.debug("converted for {}", entity.getClass().getName());
	}

	protected void handleResponse(SimpleJsonResponse jsRes) {
		Object data = jsRes.getData();

		if (data == null) {
			return;
		}

		if (data instanceof List) {
			List<Object> list = (List<Object>) data;

			if (list.size() > 0) {

				for (Object object : list) {
					handleObject(object);
				}
			}
		} else {
			handleObject(data);
		}

	}

	protected void handleResponse(GridJsonResponse jsRes) {

		List<Object> list = (List<Object>) jsRes.getList();

		if (list.size() > 0) {

			for (Object object : list) {
				handleObject(object);
			}
		}

	}

}
// end of JsonHttpMessageConverter.java