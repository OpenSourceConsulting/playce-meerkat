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
package com.athena.meerkat.common.core.util;

import java.io.File;
import java.io.IOException;

import org.apache.ant.compress.taskdefs.Unzip;
import org.apache.ant.compress.taskdefs.Zip;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.ManifestException;
import org.apache.tools.ant.types.FileSet;
import org.springframework.util.Assert;

/**
 * <pre>
 * 압축 및 압축해제 기능을 지원하는 유틸리티 클래스
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class ZipUtil {
	
	/**
	 * <pre>
	 * 지정된 디렉토리를 "디렉토리명.zip" 으로 압축한다.
	 * </pre>
	 * @param baseDir
	 * @return
	 * @throws IOException
	 * @throws ManifestException 
	 */
	public static String compress(String baseDir) throws IOException, ManifestException {
		return compress(baseDir, null);
	}//end of compress()

	/**
	 * <pre>
	 * 지정된 디렉토리를 지정된 이름으로 압축한다.
	 * </pre>
	 * @param baseDir
	 * @param destFile
	 * @return
	 * @throws IOException
	 * @throws ManifestException 
	 */
	public static String compress(String baseDir, String destFile) throws IOException, ManifestException {
		Assert.notNull(baseDir, "baseDir cannot be null.");
		
		Project project = new Project();
		
		File filesetDir = new File(baseDir);
		File archiveFile = null;
		
		Assert.isTrue(filesetDir.exists(), baseDir + " does not exist.");
		Assert.isTrue(filesetDir.isDirectory(), baseDir + " is not a directory.");
		
		if(StringUtils.isEmpty(destFile)) {
			archiveFile = new File(filesetDir.getParent(), new StringBuilder(filesetDir.getName()).append(".zip").toString());
		} else {
			archiveFile = new File(destFile);
		}

		FileSet fileSet = new FileSet();
		fileSet.setDir(filesetDir);
    	fileSet.setProject(project); // project가 세팅되지 않으면 DirectoryScanner가 초기화 되지 않아 Excption이 발생함다.

    	Zip zip = new Zip();
    	zip.setProject(project); // project가 세팅되지 않으면 destFile 존재 시 Exception 발생한다.
    	zip.setDestfile(archiveFile);
    	zip.add(fileSet);
		
    	zip.execute();
		
		return archiveFile.getAbsolutePath();
	}//end of compress()
	
	/**
	 * <pre>
	 * 지정된 압축파일을 같은 디렉토리에 압축파일 명으로 압축 해제한다. 
	 * </pre>
	 * @param sourceFile
	 * @return
	 * @throws IOException
	 */
	public static String decompress(String sourceFile) throws IOException {
        return decompress(sourceFile, null);
    }//end of decompress()

    /**
     * <pre>
     * 지정된 압축파일을 지정된 경로에 압축 해제한다.
     * </pre>
     * @param sourceFile
     * @param destDir
     * @return 
     * @throws IOException
     */
    public static String decompress(String sourceFile, String destDir) throws IOException {
		Assert.notNull(sourceFile, "sourceFile cannot be null.");
		
		File src = new File(sourceFile);
		File dest = null;

		Assert.isTrue(src.exists(), src + " does not exist.");
		Assert.isTrue(src.isFile(), sourceFile + " is not a file.");
		
		if(StringUtils.isEmpty(destDir)) {
			dest = new File(src.getParent(), src.getName().substring(0, src.getName().lastIndexOf(".")));
		} else {
			dest = new File(destDir);
		}
		
		Unzip unzip = new Unzip();
    	unzip.setSrc(src);
    	unzip.setDest(dest);
    	unzip.execute();
    	
		return dest.getAbsolutePath();
    }//end of decompress()
    
}// end of ZipUtil.java