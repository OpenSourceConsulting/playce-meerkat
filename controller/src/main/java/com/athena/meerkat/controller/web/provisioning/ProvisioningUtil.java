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
 * BongJin Kwon		2016. 3. 24.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.athena.meerkat.controller.web.provisioning.util.OSUtil;

public class ProvisioningUtil {
	
	private static final String JOB_NUM_FILE_NAME = "job";
	
	public static int getJobNum(File jobBaseDir) throws IOException {
		File jobFile = new File(jobBaseDir.getAbsolutePath() + File.separator + JOB_NUM_FILE_NAME);
		int jobNum = 0;
		
		if (jobFile.exists()) {
			jobNum = Integer.parseInt( FileUtils.readFileToString(jobFile) );
		} 
		
		FileUtils.writeStringToFile(jobFile, String.valueOf(++jobNum));
		
		return jobNum;
	}
	
	public static void deployAgent(File commanderDir, File jobDir) throws Exception {
		
		String scriptFile = "deployagent.sh";
		
		if(OSUtil.isWindows()) {
			scriptFile = "deployAgent.bat";
		}
		
		List<String> cmds = new ArrayList<String>();
		cmds.add(commanderDir.getAbsolutePath() + File.separator + scriptFile);
		cmds.add(jobDir.getAbsolutePath() + File.separator + "build.xml");
				
		CommandUtil.execWithLog(commanderDir, cmds);
	}
	
	public static void sendCommand(File commanderDir, File jobDir) throws Exception {
		
		String scriptFile = "sendcmd.sh";
		
		if(OSUtil.isWindows()) {
			scriptFile = "sendCommand.bat";
		}
		
		List<String> cmds = new ArrayList<String>();
		cmds.add(commanderDir.getAbsolutePath() + File.separator + scriptFile);
		cmds.add(jobDir.getAbsolutePath() + File.separator + "build.xml");
		
		CommandUtil.execWithLog(commanderDir, cmds);
	}
	
	
}
//end of ProvisioningUtil.java