/* 
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
 * Bong-Jin Kwon	2013. 9. 27.		First Draft.
 */
package com.athena.meerkat.controller.web.common.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.NumberUtils;

/**
 * <pre>
 *  화면(view)에서 전달된 number 값이 없을때(''일때) 0으로 변환해서 처리하도록함. 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public StringToNumberConverterFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
		// TODO Auto-generated method stub
		return new StringToNumber<T>(targetType);
	}
	
	private final class StringToNumber<T extends Number> implements Converter<String, T> {

		private final Class<T> targetType;

		public StringToNumber(Class<T> targetType) {
			this.targetType = targetType;
		}

		public T convert(String source) {
			if (source.length() == 0) {
				source = "0";
			}
			return NumberUtils.parseNumber(source, this.targetType);
		}
	}

}
//end of StringToNumberConverterFactory.java