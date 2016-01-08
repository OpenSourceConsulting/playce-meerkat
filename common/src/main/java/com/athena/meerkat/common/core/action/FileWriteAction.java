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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class FileWriteAction extends Action {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileWriteAction.class);
	
	private static final long serialVersionUID = 1L;
	
	private String contents;
	private String fileName;
	private String permission;
	
	public FileWriteAction(int sequence) {
		super(sequence);
	}

	/**
	 * @return the contents
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * @param permission the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/* (non-Javadoc)
	 * @see com.athena.meerkat.common.core.action.Action#perform()
	 */
	@Override
	public String perform() {
		String result = fileName;
		
		try {
			String separator = File.separator;
			
			fileName = fileName.replaceAll("\\\\", separator);
			
			String path = fileName.substring(0, fileName.lastIndexOf(separator));
			String name = fileName.substring(fileName.lastIndexOf(separator) + 1);
			
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			FileOutputStream fos = new FileOutputStream(new File(path, name));
			
			if (StringUtils.isNotEmpty(permission)) {
				Files.setPosixFilePermissions(Paths.get(fileName), PosixFilePermissions.fromString(permission));	
			}
			
			IOUtils.write(contents, fos, "UTF-8");
			IOUtils.closeQuietly(fos);
			
			result += " saved.\n";
		} catch (FileNotFoundException e) {
			LOGGER.error("FileNotFoundException has occurred. : ", e);
			result += " does not saved.\n";
		} catch (IOException e) {
			LOGGER.error("IOException has occurred. : ", e);
			result += " does not saved.\n";
		}
		
		return result;
	}

}
//end of FileWriteAction.java