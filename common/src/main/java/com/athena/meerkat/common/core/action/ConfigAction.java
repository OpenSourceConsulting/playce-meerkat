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
 * Sang-cheon Park	2013. 10. 10.		First Draft.
 */
package com.athena.meerkat.common.core.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.athena.meerkat.common.core.action.support.Property;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class ConfigAction extends Action {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConfigAction.class);

	private static final long serialVersionUID = 1L;

	/** 프로비저닝 시 변경되어야 할 파일 */
	private String fileName;

	/** 파일 내의 변경되어야 할 항목들에 대한 목록 */
	private List<Property> properties;

	public ConfigAction(int sequence) {
		super(sequence);
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the properties
	 */
	public List<Property> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.athena.meerkat.common.core.action.Action#perform()
	 */
	@Override
	public String perform() {
		Assert.notNull(fileName, "filename cannot be null.");
		Assert.notNull(properties, "properties cannot be null.");

		File file = new File(fileName);

		Assert.isTrue(file.exists(), fileName + " does not exist.");
		Assert.isTrue(file.isFile(), fileName + " is not a file.");

		LOGGER.debug("[{}] file's configuration will be changed.", fileName);

		try {
			String fileContents = IOUtils.toString(file.toURI());

			for (Property property : properties) {
				LOGGER.debug("\"${{}}\" will be changed to \"{}\".",
						property.getKey(),
						property.getValue().replaceAll("\\\\", ""));
				fileContents = fileContents.replaceAll(
						"\\$\\{" + property.getKey() + "\\}",
						property.getValue());
			}

			FileOutputStream fos = new FileOutputStream(file);
			IOUtils.write(fileContents, fos);
			IOUtils.closeQuietly(fos);
		} catch (IOException e) {
			LOGGER.error("IOException has occurred.", e);
		}

		return null;
	}

}
// end of ConfigAction.java