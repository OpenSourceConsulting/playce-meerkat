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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.Tailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketSession;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.SshAccount;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.provisioning.log.LogTailerListener;
import com.athena.meerkat.controller.web.provisioning.xml.ContextXmlHandler;
import com.athena.meerkat.controller.web.tomcat.services.TomcatConfigFileService;
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
 * 
 * @author Bongjin Kwon
 * @version 1.0
 */
@Service
public class TomcatProvisioningService implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TomcatProvisioningService.class);

	private static final String PROPS_COMMENTS = "Don't modify this file. This file is auto generated.";
	private static final String JOBS_DIR_NM = "jobs";
	public static final String LOG_END = "provisioning finished.";

	@Autowired
	private TomcatDomainService domainService;

	@Autowired
	private TomcatInstanceService instanceService;

	@Autowired
	private TomcatConfigFileService configFileService;

	@Autowired
	private CommonCodeHandler codeHandler;

	@Value("${meerkat.commander.home}")
	private String commanderHome;// = "G:/project/AthenaMeerkat/.aMeerkat";

	private File commanderDir;

	private Configuration cfg;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public TomcatProvisioningService() {
		cfg = new Configuration(Configuration.VERSION_2_3_22);

		// cfg.setDirectoryForTemplateLoading(new
		// File("/where/you/store/templates"));
		cfg.setTemplateLoader(new ClassTemplateLoader(getClass(), "/"));
		cfg.setObjectWrapper(new DefaultObjectWrapper(
				Configuration.VERSION_2_3_22));
		cfg.setDefaultEncoding("UTF-8");

		// During web page *development*
		// TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
		// cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
	}

	@Async
	public void installTomcatInstance2(int domainId) {

		try {
			Thread.sleep(10000);
			System.out.println("test ############################# test");
		} catch (Exception e) {

		}

	}

	@Transactional
	// @Async
	public void installTomcatInstance(int domainId, WebSocketSession session) {

		DomainTomcatConfiguration tomcatConfig = domainService
				.getTomcatConfig(domainId);
		List<DataSource> dsList = domainService
				.getDatasourceByDomainId(domainId);

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}

		List<TomcatConfigFile> confFiles = new ArrayList<TomcatConfigFile>();
		confFiles.add(configFileService.getLatestConfVersion(
				tomcatConfig.getTomcatDomain(), null,
				MeerkatConstants.CONFIG_FILE_TYPE_SERVER_XML_CD));
		confFiles.add(configFileService.getLatestConfVersion(
				tomcatConfig.getTomcatDomain(), null,
				MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML_CD));

		List<TomcatInstance> list = instanceService
				.getTomcatListByDomainId(domainId);

		if (list != null && list.size() > 0) {

			for (TomcatInstance tomcatInstance : list) {
				ProvisionModel pModel = new ProvisionModel(tomcatConfig,
						tomcatInstance, dsList, true);
				pModel.setConfFiles(confFiles);
				doInstallTomcatInstance(pModel, session);
			}
		} else {
			LOGGER.warn("tomcat instances is empty!!");
		}

	}

	private void doInstallTomcatInstance(ProvisionModel pModel,
			WebSocketSession session) {

		File jobDir = generateBuildProperties(pModel, session);

		try {

			/*
			 * 1. make job dir & copy install.xml with default.xml.
			 */
			copyCmds("install.xml", jobDir);

			/*
			 * 2. generate agentenv.sh createAgentEnvSHFile(jobDir,
			 * agentDeployDir, agentName); generateTomcatEnvFile(jobDir,
			 * tomcatConfig);
			 */

			/*
			 * 4. deploy agent
			 */
			ProvisioningUtil.runDefaultTarget(commanderDir, jobDir,
					"deploy-agent");

			/*
			 * 5. send install .
			 */
			ProvisioningUtil.sendCommand(commanderDir, jobDir);

			/*
			 * 6. update server.xml & context.xml
			 */
			ProvisioningUtil.runDefaultTarget(commanderDir, jobDir,
					"update-config");

			instanceService.saveState(pModel.getTomcatInstance().getId(),
					MeerkatConstants.TOMCAT_STATUS_INSTALLED);

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			throw new RuntimeException(e);

		} finally {
			LOGGER.debug(LOG_END);
			MDC.remove("jobPath");
			MDC.remove("serverIp");
		}
	}

	@Transactional
	public void updateTomcatInstanceConfig(int domainId,
			WebSocketSession session) {

		DomainTomcatConfiguration tomcatConfig = domainService
				.getTomcatConfig(domainId);
		// List<Tomcat> TomcatInstanceService
		List<TomcatInstance> list = instanceService
				.getTomcatListByDomainId(domainId);

		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}

		if (list != null && list.size() > 0) {

			for (TomcatInstance tomcatInstance : list) {
				sendCommand(new ProvisionModel(tomcatConfig, tomcatInstance,
						null), "updateTomcatConfig.xml", session);
			}
		} else {
			LOGGER.warn("tomcat instances is empty!!");
		}

	}

	@Transactional
	public void updateTomcatInstanceConfig(TomcatInstance tomcat,
			WebSocketSession session) {
		DomainTomcatConfiguration tomcatConfig = instanceService
				.getTomcatConfig(tomcat.getId());
		if (tomcatConfig == null) {
			LOGGER.warn("tomcat config is not set!!");
			return;
		}
		sendCommand(new ProvisionModel(tomcatConfig, tomcat, null),
				"updateTomcatConfig.xml", session);
	}

	public void startTomcatInstance(int instanceId, WebSocketSession session) {

		TomcatInstance tomcatInstance = instanceService.findOne(instanceId);
		DomainTomcatConfiguration tomcatConfig = instanceService
				.getTomcatConfig(tomcatInstance.getDomainId(), instanceId);

		runCommand(new ProvisionModel(tomcatConfig, tomcatInstance, null),
				"startTomcat.xml", session);
	}

	public void stopTomcatInstance(int instanceId, WebSocketSession session) {

		TomcatInstance tomcatInstance = instanceService.findOne(instanceId);
		DomainTomcatConfiguration tomcatConfig = instanceService
				.getTomcatConfig(tomcatInstance.getDomainId(), instanceId);

		runCommand(new ProvisionModel(tomcatConfig, tomcatInstance, null),
				"stopTomcat.xml", session);
	}

	private void sendCommand(ProvisionModel pModel, String cmdFileName,
			WebSocketSession session) {

		File jobDir = generateBuildProperties(pModel, session);

		try {

			/*
			 * 1. make job dir & copy build.xml.
			 */
			copyCmds(cmdFileName, jobDir);

			/*
			 * 2. send cmd.
			 */
			ProvisioningUtil.sendCommand(commanderDir, jobDir);

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			throw new RuntimeException(e);

		} finally {
			LOGGER.debug(LOG_END);
			MDC.remove("jobPath");
			MDC.remove("serverIp");
		}
	}

	private void runCommand(ProvisionModel pModel, String cmdFileName,
			WebSocketSession session) {

		File jobDir = generateBuildProperties(pModel, session);

		try {

			/*
			 * 1. make job dir & copy build.xml.
			 */
			copyCmds(cmdFileName, jobDir);

			/*
			 * 2. send cmd.
			 */
			ProvisioningUtil.runCommand(commanderDir, jobDir);

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			throw new RuntimeException(e);

		} finally {
			LOGGER.debug(LOG_END);
			MDC.remove("jobPath");
			MDC.remove("serverIp");
		}
	}

	/**
	 * <pre>
	 * 실행할 ant commands file 을 jobDir 에 copy 한다.
	 * </pre>
	 * 
	 * @param cmdFileName
	 * @param jobDir
	 * @throws IOException
	 */
	private void copyCmds(String cmdFileName, File jobDir) throws IOException {
		String cmdsPath = commanderDir.getAbsolutePath() + File.separator
				+ "cmds" + File.separator;
		FileUtils.copyFileToDirectory(new File(cmdsPath + "default.xml"),
				jobDir);
		FileUtils
				.copyFile(new File(cmdsPath + cmdFileName),
						new File(jobDir.getAbsolutePath() + File.separator
								+ "cmd.xml"));
	}

	/**
	 * <pre>
	 * generate build-ssh.properties & build.properties in job path.
	 * </pre>
	 * 
	 * @param tomcatConfig
	 * @param tomcatInstance
	 * @param session
	 * @return
	 */
	private File generateBuildProperties(ProvisionModel pModel,
			WebSocketSession session) {

		DomainTomcatConfiguration tomcatConfig = pModel.getTomcatConfig();
		List<TomcatConfigFile> confFiles = pModel.getConfFiles();

		Server targetServer = pModel.getTomcatInstance().getServer();
		String serverIp = targetServer.getSshIPAddr();
		MDC.put("serverIp", serverIp);

		List<SshAccount> accounts = (List<SshAccount>) targetServer
				.getSshAccounts();

		String userId = accounts.get(0).getUsername();
		String userPass = accounts.get(0).getPassword();

		Properties prop = new Properties(); // build-ssh.properties
		prop.setProperty("server.ip", serverIp);
		prop.setProperty("server.id", String.valueOf(targetServer.getId()));
		prop.setProperty("server.port",
				String.valueOf(targetServer.getSshPort()));
		prop.setProperty("user.id", userId);
		prop.setProperty("user.passwd", userPass);
		prop.setProperty("key.file", commanderDir + "/ssh/svn_key.pem");

		String agentDeployDir = "/home/" + userId + "/athena-meerkat-agent";
		String agentName = "athena-meerkat-agent-1.0.0-SNAPSHOT";

		Properties targetProps = new Properties(); // build.properties
		targetProps.setProperty("agent.deploy.dir", agentDeployDir);
		targetProps.setProperty("agent.name", agentName);
		// targetProps.setProperty("tomcat.unzip.pah", "/home/"+ userId
		// +"/tmp");
		targetProps
				.setProperty("catalina.home", tomcatConfig.getCatalinaHome());
		targetProps
				.setProperty("catalina.base", tomcatConfig.getCatalinaBase());
		targetProps.setProperty("tomcat.name",
				getTomcatName(tomcatConfig.getTomcatVersionNm()));
		targetProps.setProperty("am.server.port", "8005");
		targetProps.setProperty("am.http.port",
				String.valueOf(tomcatConfig.getHttpPort()));
		targetProps.setProperty("am.ajp.port",
				String.valueOf(tomcatConfig.getAjpPort()));
		targetProps.setProperty("am.uri.encoding", tomcatConfig.getEncoding());
		targetProps.setProperty("am.rmi.registry.port",
				String.valueOf(tomcatConfig.getRmiRegistryPort()));
		targetProps.setProperty("am.rmi.server.port",
				String.valueOf(tomcatConfig.getRmiServerPort()));

		targetProps.setProperty("am.conf.op", pModel.getConfigOP());

		if (confFiles != null) {
			for (TomcatConfigFile confFile : confFiles) {
				targetProps.setProperty(
						codeHandler.getFileTypeName(confFile.getFileTypeCdId())
								+ ".file",
						configFileService.getFileFullPath(confFile));
			}
		}

		OutputStream output = null;
		File jobDir = null;
		try {
			int jobNum = getJobNumber(serverIp);
			targetProps.setProperty("job.number", String.valueOf(jobNum));
			jobDir = makeJobDir(serverIp, jobNum);

			MDC.put("jobPath", jobDir.getAbsolutePath());

			sendLog(session, jobDir.getAbsolutePath());

			if (CollectionUtils.isEmpty(accounts)) {
				throw new RuntimeException("생성할 ssh 계정정보가 없습니다.");
			}

			LOGGER.debug("SERVER NAME : " + targetServer.getName());

			/*
			 * generate agentenv.sh
			 */
			createAgentEnvSHFile(jobDir, agentDeployDir, agentName);
			generateTomcatEnvFile(jobDir, tomcatConfig, serverIp);

			/*
			 * generate build properties
			 */
			output = new FileOutputStream(jobDir.getAbsolutePath()
					+ File.separator + "build-ssh.properties");
			prop.store(output, PROPS_COMMENTS);
			LOGGER.debug("generated build-ssh.properties");

			IOUtils.closeQuietly(output);

			output = new FileOutputStream(jobDir.getAbsolutePath()
					+ File.separator + "build.properties");
			targetProps.store(output, PROPS_COMMENTS);
			LOGGER.debug("generated build.properties");

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			throw new RuntimeException(e);

		} finally {
			IOUtils.closeQuietly(output);
		}

		return jobDir;
	}

	/**
	 * <pre>
	 * send log to client(UI)
	 * </pre>
	 * 
	 * @param session
	 * @param jobPath
	 */
	private void sendLog(WebSocketSession session, String jobPath) {

		if (session != null) {

			LogTailerListener listener = new LogTailerListener(session);
			long delay = 2000;
			File file = new File(jobPath + File.separator + "build.log");
			LOGGER.debug("log file : {}", file.getAbsoluteFile());

			Tailer tailer = new Tailer(file, listener, delay);
			new Thread(tailer).start();
		}

	}

	private int getJobNumber(String serverIp) throws IOException {
		int jobNum = ProvisioningUtil.getJobNum(new File(commanderDir
				.getAbsolutePath()
				+ File.separator
				+ JOBS_DIR_NM
				+ File.separator + serverIp));

		return jobNum;
	}

	private File makeJobDir(String serverIp, int jobNum) {
		File jobDir = new File(commanderDir.getAbsolutePath() + File.separator
				+ JOBS_DIR_NM + File.separator + serverIp + File.separator
				+ String.valueOf(jobNum));
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

	private void generateTomcatEnvFile(File jobDir,
			DomainTomcatConfiguration tomcatConfig, String rmiHostname)
			throws IOException {

		Map<String, String> model = new HashMap<String, String>();
		model.put("javaHome", tomcatConfig.getJavaHome());
		model.put("catalinaHome", tomcatConfig.getCatalinaHome());
		model.put("catalinaBase", tomcatConfig.getCatalinaBase());
		model.put("rmiHostname", rmiHostname);

		Writer output = null;

		try {
			output = new FileWriter(jobDir.getAbsolutePath() + File.separator
					+ "env.sh");

			generate("templates/env.sh.ftl", model, output);

			LOGGER.debug("generated env.sh");
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

	private void createAgentEnvSHFile(File jobDir, String deployDir,
			String agentName) throws IOException {
		Map<String, String> model = new HashMap<String, String>();
		model.put("deployDir", deployDir);
		model.put("agentName", agentName);

		Writer output = null;

		try {
			output = new FileWriter(jobDir.getAbsolutePath() + File.separator
					+ "agentenv.sh");

			generate("templates/agentenv.sh.ftl", model, output);

			LOGGER.debug("generated agentenv.sh");
		} finally {
			IOUtils.closeQuietly(output);
		}

	}

	private boolean generate(String templateFileName, Object dataModel,
			Writer out) throws IOException {

		/* Get the template */
		Template temp = cfg.getTemplate(templateFileName);

		try {
			temp.process(dataModel, out);

			return true;
		} catch (TemplateException e) {
			e.printStackTrace();
			return false;
		}
	}

	private String getTomcatName(String tomcatVersion) {
		return codeHandler.getCodeNm(MeerkatConstants.CODE_GROP_TE_VERSION,
				Integer.parseInt(tomcatVersion));
	}
}