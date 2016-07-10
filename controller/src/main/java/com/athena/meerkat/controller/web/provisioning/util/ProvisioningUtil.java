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
package com.athena.meerkat.controller.web.provisioning.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProvisioningUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProvisioningUtil.class);
	
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
	
	/**
	 * <pre>
	 * run default.xml target.
	 * </pre>
	 * @param commanderDir
	 * @param jobDir
	 * @param targetName
	 * @throws Exception
	 */
	public static boolean runDefaultTarget(File commanderDir, File jobDir, String targetName, String... args) throws Exception {
		
		return runAntTarget(commanderDir, jobDir, "default.xml", targetName, args);

	}
	
	protected static boolean runCmds(File commanderDir, List<String> cmds) {
		String isSuccess = CommandUtil.execWithLog(commanderDir, cmds);
		
		return Boolean.parseBoolean(isSuccess);
	}
	
	protected static List<String> runAndReturnLogs(File commanderDir, List<String> cmds) {
		
		return CommandUtil.execAndReturnLogs(commanderDir, cmds);
	}
	
	/**
	 * <pre>
	 * default.xml 의 send-cmd 실행.
	 * </pre>
	 * @param commanderDir
	 * @param jobDir
	 * @throws Exception
	 */
	public static boolean sendCommand(File commanderDir, File jobDir) throws Exception {
		
		return runAntTarget(commanderDir, jobDir, "default.xml", "send-cmd");
	}
	
	/**
	 * <pre>
	 * jobDir 의 cmd.xml 을 실행.
	 * </pre>
	 * @param commanderDir
	 * @param jobDir
	 * @return
	 * @throws Exception
	 */
	public static boolean runCommand(File commanderDir, File jobDir) throws Exception {

		return runAntTarget(commanderDir, jobDir, "cmd.xml", null);
	}
	
	public static boolean runAntTarget(File commanderDir, File jobDir, String antScript, String targetName, String... args) throws Exception {
		
		String execFile = "runtarget.sh";
		
		if(OSUtil.isWindows()) {
			execFile = "runTarget.bat";
		}
		
		List<String> cmds = new ArrayList<String>();
		cmds.add(commanderDir.getAbsolutePath() + File.separator + execFile);
		cmds.add(jobDir.getAbsolutePath() + File.separator + antScript);
		
		if (targetName != null) {
			cmds.add(targetName);
		}
		
		if (args != null) {
			for (String argument : args) {
				LOGGER.debug("%%%%%%%%%%%%%% {}", argument);
				cmds.add(argument);
			}
		}
		
				
		return runCmds(commanderDir, cmds);

	}
	
	public static List<String> runAntTargetWithLogs(File commanderDir, File jobDir, String antScript, String targetName, String... args) throws Exception {
		
		String execFile = "runtarget.sh";
		
		if(OSUtil.isWindows()) {
			execFile = "runTarget.bat";
		}
		
		List<String> cmds = new ArrayList<String>();
		cmds.add(commanderDir.getAbsolutePath() + File.separator + execFile);
		cmds.add(jobDir.getAbsolutePath() + File.separator + antScript);
		
		if (targetName != null) {
			cmds.add(targetName);
		}
		
		if (args != null) {
			for (String argument : args) {
				LOGGER.debug("%%%%%%%%%%%%%% {}", argument);
				cmds.add(argument);
			}
		}
		
				
		return runAndReturnLogs(commanderDir, cmds);

	}
	
	
}
//end of ProvisioningUtil.java