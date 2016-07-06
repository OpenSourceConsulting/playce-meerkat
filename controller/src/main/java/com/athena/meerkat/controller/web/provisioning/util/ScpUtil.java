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
 * BongJin Kwon		2016. 7. 5.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning.util;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.optional.ssh.Scp;
import org.apache.tools.ant.types.FileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * <pre>
 * 목적지 호스트로의 scp 명령을 수행하기 위한 유틸리티 클래스
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class ScpUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ScpUtil.class);
	
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param targetHost
	 * @param remoteDownFile down file uri.
	 * @param todir save path.
	 */
	public static void download(TargetHost targetHost, String remoteDownFile, String todir) {
		Assert.notNull(targetHost, "targetHost cannot be null.");
		Assert.notNull(remoteDownFile, "remoteDownFile cannot be null.");
		Assert.notNull(todir, "todir cannot be null.");
		
		String file = targetHost.getUsername() + "@" + targetHost.getHost() + ":" + remoteDownFile;
		
		logger.debug("[scp download] {} <-- {}" , todir, file);
		
		Project project = new Project();
		
		Scp scp = new Scp();
		
		// Ant Project Property
		scp.setProject(project);
		scp.setVerbose(true);

		// Set Scp properties 
		scp.setPort(targetHost.getPort());
		scp.setPassword(targetHost.getPassword());
		scp.setFile(file);
		scp.setTodir(todir);
		scp.setTrust(targetHost.isTrust());
		//scp.setFailonerror(true);// default is true.
		
		if( targetHost.getKeyfile() != null) {
			scp.setKeyfile(targetHost.getKeyfile());
		}
		
		scp.execute();
	}
	
	/**
	 * <pre>
	 * source에 해당하는 파일(또는 디렉토리)를 지정된 호스트의 target으로 전송한다.
	 * </pre>
	 * @param targetHost
	 * @param source
	 * @param target
	 */
	public static void upload(TargetHost targetHost, String source, String target) {

		Assert.notNull(targetHost, "targetHost cannot be null.");
		Assert.notNull(source, "source cannot be null.");
		Assert.notNull(target, "target cannot be null.");
		
		String destination = targetHost.getUsername() + "@" + targetHost.getHost() + ":" + target;
		
		logger.debug("[scp upload] {} --> {}", source, destination);
		
		Project project = new Project();
		
		Scp scp = new Scp();
		
		// Ant Project Property
		scp.setProject(project);
		scp.setVerbose(true);

		// Set Scp properties 
		scp.setPort(targetHost.getPort());
		scp.setPassword(targetHost.getPassword());
		scp.setTodir(destination);
		scp.setTrust(targetHost.isTrust());
		
		// 전달받은 source가 디렉토리 일 경우 FileSet을 scp에 추가한다.
		File filesetDir = new File(source);
		if(filesetDir.isDirectory()) {
			FileSet fileSet = new FileSet();
			fileSet.setDir(filesetDir);
	    	fileSet.setProject(project);
	    	
	    	scp.addFileset(fileSet);
		} else {
			scp.setFile(source);
		}
		
		if( targetHost.getKeyfile() != null) {
			scp.setKeyfile(targetHost.getKeyfile());
		}
		
		scp.execute();
	}//end of upload()
	
	public static void main(String[] args) {
		
		//G:\project\AthenaMeerkat\.aMeerkat\temp <-- centos:centos@192.168.0.157:/home/centos/apm/scouter/agent.java/svr33.conf
		
		TargetHost server = new TargetHost("192.168.0.157", 22, "centos", "centos");
		
		ScpUtil.download(server, "/home/centos/apm/scouter/agent.java/svr33.conf", "G:\\project\\AthenaMeerkat\\.aMeerkat\\temp");
		
	}
	
}
//end of ScpUtil.java