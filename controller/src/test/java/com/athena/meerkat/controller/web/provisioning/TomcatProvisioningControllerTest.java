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
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class TomcatProvisioningControllerTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatProvisioningControllerTest.class);
	
	private Configuration cfg;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		if (cfg == null) {
			cfg = new Configuration(Configuration.VERSION_2_3_22);

	        //cfg.setDirectoryForTemplateLoading(new File("/where/you/store/templates"));
			cfg.setTemplateLoader(new ClassTemplateLoader(getClass(), ""));
	        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_22));
	        cfg.setDefaultEncoding("UTF-8");
	        
	        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
	        //cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		}
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInstall() {
		
		File workingDir = new File("G:/project/git/athena-meerkat/agent");
		
		Properties prop = new Properties(); //build-ssh.properties
		prop.setProperty("server.ip", 	"192.168.0.156");
		prop.setProperty("server.port", "22");
		prop.setProperty("user.id", 	"centos");
		prop.setProperty("user.passwd", "centos");
		prop.setProperty("key.file", 	"G:/svn_key.pem");
		
		Properties targetProps = new Properties(); //build.properties
		targetProps.setProperty("deploy.dir", 		"/home/centos/athena-meerkat-agent");
		targetProps.setProperty("agent.name", 		"athena-meerkat-agent-1.0.0-SNAPSHOT");
		targetProps.setProperty("tomcat.unzip.pah", 	"/home/centos/tmp");
		targetProps.setProperty("catalina.base", 		"/home/centos/tmp/instance1");
		
		
		OutputStream output = null;
		
		try {
			/*
			 * 1. generate agentenv.sh
			 */
			createEnvSHFile(workingDir, targetProps.getProperty("deploy.dir"), targetProps.getProperty("agent.name"));
			
			/*
			 * 2. generate build properties
			 */
			output = new FileOutputStream(workingDir.getAbsolutePath() + File.separator + "build-ssh.properties");
			prop.store(output, "desc");
			LOGGER.debug("generated build-ssh.properties");
			
			IOUtils.closeQuietly(output);
			
			output = new FileOutputStream(workingDir.getAbsolutePath() + File.separator + "build.properties");
			targetProps.store(output, "desc");
			LOGGER.debug("generated build.properties");
			
			/*
			 * 3. deploy agent
			 */
			deployAgent(workingDir);
			
			/*
			 * 4. send cmd.
			 */
			sendCommand(workingDir);
			
			
		} catch (Exception e) {
			fail(e.toString());
		} finally {
			IOUtils.closeQuietly(output);
		}
		
	}
	
	public void deployAgent(File workingDir) throws Exception {
		List<String> cmds = new ArrayList<String>();
		cmds.add(workingDir.getAbsolutePath() + File.separator + "deployAgent.bat");
				
		CommandUtil.execWithLog(workingDir, cmds);
	}
	
	public void sendCommand(File workingDir) throws Exception {
		List<String> cmds = new ArrayList<String>();
		cmds.add(workingDir.getAbsolutePath() + File.separator + "sendCommand.bat");
		CommandUtil.execWithLog(workingDir, cmds);
	}
	
	private void createEnvSHFile(File workingDir, String deployDir, String agentName) throws IOException {
		Map<String, String> model = new HashMap<String, String>();
		model.put("deployDir", deployDir);
		model.put("agentName", agentName);
		
		Writer output = null;
		
		try{
			output = new FileWriter(workingDir.getAbsolutePath() + File.separator + "agentenv.sh");
		
			generate("agentenv.sh.ftl", model, output);
			
			LOGGER.debug("generated env.sh");
		} finally {
			IOUtils.closeQuietly(output);
		}
		
	}
	
	private boolean generate(String templateFileName, Object dataModel, Writer out) throws IOException{
		
		/* Get the template */
        Template temp = cfg.getTemplate(templateFileName);

        try{
        	temp.process(dataModel, out);
        	
        	return true;
        }catch(TemplateException e){
        	e.printStackTrace();
        	return false;
        }
	}

}
//end of TomcatProvisioningControllerTest.java