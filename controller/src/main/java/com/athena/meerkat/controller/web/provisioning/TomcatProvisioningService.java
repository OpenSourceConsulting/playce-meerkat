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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.SshAccount;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Service
public class TomcatProvisioningService implements InitializingBean{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatProvisioningService.class);
	private static final String JOBS_DIR_NM = "jobs";
	
	@Autowired
	private TomcatDomainService domainService;
	
	@Autowired
	private TomcatInstanceService instanceService;
	
	private String commanderHome = "G:/project/AthenaMeerkat/.aMeerkat";
	private File commanderDir;
	
	private Configuration cfg;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public TomcatProvisioningService() {
		cfg = new Configuration(Configuration.VERSION_2_3_22);

        //cfg.setDirectoryForTemplateLoading(new File("/where/you/store/templates"));
		cfg.setTemplateLoader(new ClassTemplateLoader(getClass(), "/"));
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_22));
        cfg.setDefaultEncoding("UTF-8");
        
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        //cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
	}
	
	@Async
	public void installTomcatInstance2(int domainId) {
		
		try{
			Thread.sleep(10000);
			System.out.println("test ############################# test");
		}catch(Exception e) {
			
		}
		
	}
	
	@Transactional
	@Async
	public void installTomcatInstance(int domainId) {
		
				
		DomainTomcatConfiguration tomcatConfig = domainService.getTomcatConfig(domainId);
		//List<Tomcat>   TomcatInstanceService
		List<TomcatInstance> list = instanceService.getTomcatListByDomainId(domainId);
		
		if(tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}
		
		if(list != null && list.size() > 0){
		
			for (TomcatInstance tomcatInstance : list) {
				doInstallTomcatInstance(tomcatConfig, tomcatInstance.getServer());
			}
		} else {
			LOGGER.warn("tomcat instances is empty!!");
		}
		
	}
	
	private void doInstallTomcatInstance(DomainTomcatConfiguration tomcatConfig, Server targetServer) {

		LOGGER.debug("SERVER NAME : " + targetServer.getName());
		
		String serverIp = targetServer.getSshIPAddr();
		List<SshAccount> accounts = (List<SshAccount>)targetServer.getSshAccounts();
		
		if (CollectionUtils.isEmpty(accounts)) {
			throw new RuntimeException("생성할 ssh 계정정보가 없습니다.");
		}
		
		String userId = 	accounts.get(0).getUsername();
		String userPass = 	accounts.get(0).getPassword();
		
		Properties prop = new Properties(); //build-ssh.properties
		prop.setProperty("server.ip", 	serverIp);
		prop.setProperty("server.port", String.valueOf(targetServer.getSshPort()));
		prop.setProperty("user.id", 	userId);
		prop.setProperty("user.passwd", userPass);
		prop.setProperty("key.file", 	commanderDir + "/ssh/svn_key.pem");
		
		String agentDeployDir = "/home/"+ userId +"/athena-meerkat-agent";
		String agentName = "athena-meerkat-agent-1.0.0-SNAPSHOT";
		
		Properties targetProps = new Properties(); //build.properties
		targetProps.setProperty("agent.deploy.dir", agentDeployDir);
		targetProps.setProperty("agent.name", 		agentName);
		targetProps.setProperty("tomcat.unzip.pah", "/home/"+ userId +"/tmp");
		targetProps.setProperty("catalina.base", 	"/home/"+ userId +"/tmp/instance1");
		
		OutputStream output = null;
		
		try {
			int jobNum = getJobNumber(serverIp);
			targetProps.setProperty("job.number", 	String.valueOf(jobNum));
			
			/*
			 * 1. make job dir & copy build.xml.
			 */
			File jobDir = makeJobDir(serverIp, jobNum);
			
			FileUtils.copyFileToDirectory(new File(commanderDir.getAbsolutePath() + File.separator + "build.xml"), jobDir);
			
			
			/*
			 * 2. generate agentenv.sh
			 */
			createAgentEnvSHFile(jobDir, agentDeployDir, agentName);
			generateTomcatEnvFile(jobDir,  tomcatConfig);
			
			
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
			ProvisioningUtil.deployAgent(commanderDir, jobDir);
			
			
			/*
			 * 5. send cmd.
			 */
			ProvisioningUtil.sendCommand(commanderDir, jobDir);
			
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			throw new RuntimeException(e);
			
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
	
	private int getJobNumber(String serverIp) throws IOException {
		int jobNum = ProvisioningUtil.getJobNum(new File(commanderDir.getAbsolutePath() + File.separator + JOBS_DIR_NM + File.separator + serverIp));
		
		return jobNum;
	}
	
	private File makeJobDir(String serverIp, int jobNum) {
		File jobDir = new File(commanderDir.getAbsolutePath() + File.separator + JOBS_DIR_NM + File.separator + serverIp + File.separator + String.valueOf(jobNum));
		if (jobDir.exists() == false) {
			jobDir.mkdirs();
		}
		LOGGER.debug("JOB_DIR : " + jobDir.getAbsolutePath());
		
		return jobDir;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		Assert.notNull(commanderHome);
		
		commanderDir = new File(commanderHome);
		
	}
	
	private void generateTomcatEnvFile(File jobDir, DomainTomcatConfiguration tomcatConfig) throws IOException  {
		Writer output = null;
		
		try{
			output = new FileWriter(jobDir.getAbsolutePath() + File.separator + "env.sh");
		
			generate("templates/env.sh.ftl", tomcatConfig, output);
			
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
		
			generate("templates/agentenv.sh.ftl", model, output);
			
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
//end of TomcatProvisioningService.java