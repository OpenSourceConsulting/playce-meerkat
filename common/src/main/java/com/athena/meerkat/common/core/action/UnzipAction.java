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
 * Sang-cheon Park	2013. 10. 11.		First Draft.
 */
package com.athena.meerkat.common.core.action;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.meerkat.common.core.util.ZipUtil;

/**
 * <pre>
 * 지정된 파일에 대한 압축해제를 위한 Action 클래스
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class UnzipAction extends Action {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnzipAction.class);
	
	private static final long serialVersionUID = 1L;

	// 압축해제 대상 파일
	private String sourceFile;
	// 압축해제 결과 디렉토리
	private String destDir;

	public UnzipAction(int sequence) {
		super(sequence);
	}

	/**
	 * @return the sourceFile
	 */
	public String getSourceFile() {
		return sourceFile;
	}

	/**
	 * @param sourceFile the sourceFile to set
	 */
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	/**
	 * @return the destDir
	 */
	public String getDestDir() {
		return destDir;
	}

	/**
	 * @param destDir the destDir to set
	 */
	public void setDestDir(String destDir) {
		this.destDir = destDir;
	}

	/* (non-Javadoc)
	 * @see com.athena.meerkat.common.core.action.Action#perform()
	 */
	@Override
	public String perform() {
    	LOGGER.debug("Before decompress [{}] file to [{}]", sourceFile, destDir);
    	
		String result = "F";
    	
    	try {
    		destDir = ZipUtil.decompress(sourceFile, destDir);
			result = "S";
			
			LOGGER.debug("[{}] Decompress has done successfully.", destDir);
		} catch (IOException e) {
			LOGGER.error("IOException has occurred.", e);
		}
    	
    	return result;
    }//end of perform()

}
//end of UnzipAction.java