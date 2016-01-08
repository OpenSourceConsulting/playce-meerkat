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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.optional.ssh.SSHExec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.athena.meerkat.common.core.action.support.TargetHost;

/**
 * <pre>
 * 목적지 호스트에 ssh 명령을 통해 지정된 명령을 수행하기 위한 유틸리티 클래스
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class SshExecUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(SshExecUtil.class);

	public static File output = new File("exec_result.log"); // 프로젝트 root에 파일 생성.
	
	private static FileWriter fstream = null;
	private static BufferedWriter out = null;
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param targetHost
	 * @param commandList
	 * @throws IOException 
	 */
	public static void executeCommand(TargetHost targetHost, List<String> commandList) throws IOException {
		fstream = new FileWriter(output, false);
		out = new BufferedWriter(fstream);
		
		for(String command : commandList) {
			out.write("[" + targetHost.getUsername() + "@" + targetHost.getHost() + " ~]$ " + command + "\n");
			out.write(executeCommand(targetHost, command));
		}
		
		IOUtils.closeQuietly(out);
	}//end of executeCommand()
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param targetHost
	 * @param command
	 * @return
	 * @throws IOException 
	 */
	public static String executeCommand(TargetHost targetHost, String command) throws IOException {
    	Assert.notNull(targetHost, "targetHost cannot be null.");
    	Assert.notNull(command, "command cannot be null.");
    	
        File result = new File(SshExecUtil.class.getClassLoader().getResource(".").getFile(), "result.log");
        
        if (!command.startsWith("sudo")) {
        	command = "sudo " + command;
        }
    	
		LOGGER.debug("[ssh exec] " + command);
		
		Project project = new Project();
		
		SSHExec exec = new SSHExec();
		exec.setProject(project);
		exec.setHost(targetHost.getHost());
		exec.setPort(targetHost.getPort());
		exec.setUsername(targetHost.getUsername());
		exec.setPassword(targetHost.getPassword());
		
		// to enable sudo like 'ssh -t' option
		exec.setUsePty(true);
		
		if (targetHost.getKeyfile() != null) {
			exec.setKeyfile(targetHost.getKeyfile());
		}
		
		exec.setTrust(targetHost.isTrust());
		exec.setVerbose(true);
		exec.setCommand(command);
		
		// command 실행 결과가 저장될 파일 지정
		exec.setOutput(result);
		exec.setAppend(false);

		exec.execute();
		
		return IOUtils.toString(result.toURI());
	}//end of executeCommand()

}
//end of SshExecUtil.java