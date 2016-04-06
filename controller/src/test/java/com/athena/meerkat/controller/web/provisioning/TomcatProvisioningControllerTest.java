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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class TomcatProvisioningControllerTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatProvisioningControllerTest.class);
	
	private Configuration cfg;
	private String commanderDir = "G:/project/AthenaMeerkat/.aMeerkat";

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
		
		File workingDir = new File(commanderDir);
		String serverIp = "192.168.0.156";
		String userId = "centos";
		
		Properties prop = new Properties(); //build-ssh.properties
		prop.setProperty("server.ip", 	serverIp);
		prop.setProperty("server.port", "22");
		prop.setProperty("user.id", 	userId);
		prop.setProperty("user.passwd", userId);
		prop.setProperty("key.file", 	commanderDir + "/ssh/svn_key.pem");
		
		Properties targetProps = new Properties(); //build.properties
		targetProps.setProperty("agent.deploy.dir", "/home/"+userId+"/athena-meerkat-agent");
		targetProps.setProperty("agent.name", 		"athena-meerkat-agent-1.0.0-SNAPSHOT");
		targetProps.setProperty("tomcat.unzip.pah", "/home/"+userId+"/app");
		targetProps.setProperty("catalina.base", 	"/home/"+userId+"/app/instance1");
		
		
		OutputStream output = null;
		
		try {
			int jobNum = ProvisioningUtil.getJobNum(new File(workingDir.getAbsolutePath() + File.separator + "jobs" + File.separator + serverIp));
			targetProps.setProperty("job.number", 	String.valueOf(jobNum));
			
			/*
			 * 1. make job dir & copy build.xml.
			 */
			File jobDir = new File(workingDir.getAbsolutePath() + File.separator + "jobs" + File.separator + serverIp + File.separator + String.valueOf(jobNum));
			if (jobDir.exists() == false) {
				jobDir.mkdirs();
			}
			LOGGER.debug("JOB_DIR : " + jobDir.getAbsolutePath());
			
			FileUtils.copyFileToDirectory(new File(workingDir.getAbsolutePath() + File.separator + "build.xml"), jobDir);
			
			
			/*
			 * 2. generate agentenv.sh
			 */
			createAgentEnvSHFile(jobDir, targetProps.getProperty("agent.deploy.dir"), targetProps.getProperty("agent.name"));
			generateTomcatEnvFile(jobDir,  createTomcatConfig(userId));
			
			
			/*
			 * 3. generate build properties
			 */
			output = new FileOutputStream(jobDir.getAbsolutePath() + File.separator + "build-ssh.properties");
			prop.store(output, "desc");
			LOGGER.debug("generated build-ssh.properties");
			
			IOUtils.closeQuietly(output);
			
			output = new FileOutputStream(jobDir.getAbsolutePath() + File.separator + "build.properties");
			targetProps.store(output, "desc");
			LOGGER.debug("generated build.properties");
			
			
			/*
			 * 4. deploy agent
			 */
			ProvisioningUtil.deployAgent(workingDir, jobDir);
			
			
			/*
			 * 5. send cmd.
			 */
			ProvisioningUtil.sendCommand(workingDir, jobDir);
			
			
			
			
		} catch (Exception e) {
			//fail(e.toString());
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(output);
		}
		
	}
	
	@Test
	public void testInstallCommander() {
		
		String zipFilePath = "G:/project/git/athena-meerkat/agent/target/athena-meerkat-commander-1.0.0-SNAPSHOT-bin.zip";
		
		try {
			UnzipUtility.unzip(zipFilePath, commanderDir, true);
		} catch (Exception e) {
			//fail(e.toString());
			e.printStackTrace();
		}
	}
	
	private DomainTomcatConfiguration createTomcatConfig(String userId) {
		DomainTomcatConfiguration config = new DomainTomcatConfiguration();
		config.setJavaHome("/usr/java/jdk1.7.0_80");
		config.setCatalinaHome("/home/"+userId+"/tmp/apache-tomcat-7.0.68/apache-tomcat-7.0.68");
		config.setCatalinaBase("/home/"+userId+"/tmp/instance1");
		
		return config;
	}
	
	private void generateTomcatEnvFile(File jobDir, DomainTomcatConfiguration tomcatConfig) throws IOException  {
		Writer output = null;
		
		try{
			output = new FileWriter(jobDir.getAbsolutePath() + File.separator + "env.sh");
		
			generate("env.sh.ftl", tomcatConfig, output);
			
			LOGGER.debug("generated env.sh");
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
	
	
	private void createAgentEnvSHFile(File jobDir, String deployDir, String agentName) throws IOException {
		Map<String, String> model = new HashMap<String, String>();
		model.put("deployDir", deployDir);
		model.put("agentName", agentName);
		
		Writer output = null;
		
		try{
			output = new FileWriter(jobDir.getAbsolutePath() + File.separator + "agentenv.sh");
		
			generate("agentenv.sh.ftl", model, output);
			
			LOGGER.debug("generated agentenv.sh");
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