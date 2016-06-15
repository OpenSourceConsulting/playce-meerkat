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
 * BongJin Kwon		2016. 6. 15.		First Draft.
 */
package com.athena.meerkat.controller.web.common;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Component
public class FileHandler {
	
	@Autowired
	private ResourceLoader resourceLoader;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public FileHandler() {
		
	}
	
	public String readFileToString(String fileLocation) {
		Resource resource = resourceLoader.getResource(fileLocation);
		
		String fileContents = null;
		try {
			fileContents = FileUtils.readFileToString(resource.getFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return fileContents;
		
	}

}
//end of FileHandler.java