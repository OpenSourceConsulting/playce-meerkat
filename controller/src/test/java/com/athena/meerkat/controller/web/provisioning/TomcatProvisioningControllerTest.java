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
 * BongJin Kwon		2016. 3. 21.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomcatProvisioningControllerTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatProvisioningControllerTest.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInstall() {
		
		File workingDir = new File("G:/project/git/athena-meerkat/agent");
		
		Properties prop = new Properties();
		prop.setProperty("server.ip", "192.168.0.87");
		prop.setProperty("server.port", "22");
		prop.setProperty("user.id", "meerkat");
		prop.setProperty("user.passwd", "meerkat");
		prop.setProperty("deploy.dir", "/home/meerkat/athena-meerkat-agent");
		prop.setProperty("agent.name", "athena-meerkat-agent-1.0.0-SNAPSHOT");
		
		
		List<String> cmds = new ArrayList<String>();
		cmds.add(workingDir.getAbsolutePath() + File.separator + "deployAgent.bat");
		
		
		try {
			/*
			 * deploy agent
			 */
			prop.store(new FileOutputStream(workingDir.getAbsolutePath() + File.separator + "build.properties"), "desc");
			
			CommandUtil.execWithLog(workingDir, cmds);
			
			/*
			 * send cmd.
			 */
			cmds = new ArrayList<String>();
			cmds.add(workingDir.getAbsolutePath() + File.separator + "sendCommand.bat");
			CommandUtil.execWithLog(workingDir, cmds);
			
			
		} catch (Exception e) {
			fail(e.toString());
		}
		
	}

}
//end of TomcatProvisioningControllerTest.java