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
 * BongJin Kwon		2016. 5. 30.		First Draft.
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
import org.apache.commons.io.input.Tailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketSession;

import com.athena.meerkat.common.tomcat.TomcatVersionsProperties;
import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.SshAccount;
import com.athena.meerkat.controller.web.entities.TaskHistoryDetail;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.provisioning.log.LogTailerListener;
import com.athena.meerkat.controller.web.provisioning.util.ProvisioningUtil;
import com.athena.meerkat.controller.web.resources.services.DataGridServerGroupService;
import com.athena.meerkat.controller.web.tomcat.services.TaskHistoryService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatConfigFileService;

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
public abstract class AbstractProvisioningService implements InitializingBean{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProvisioningService.class);
	
	public static final String LOG_END = "provisioning finished.";
	
	private static final String PROPS_COMMENTS = "Don't modify this file. This file is auto generated.";
	private static final String JOBS_DIR_NM = "jobs";
	
	@Autowired
	protected TomcatConfigFileService configFileService;
	
	@Autowired
	private DataGridServerGroupService dataGridGroupService;
	
	@Autowired
	private CommonCodeHandler codeHandler;
	
	@Value("${meerkat.commander.home}")
	protected String commanderHome;// = "G:/project/AthenaMeerkat/.aMeerkat";
	
	@Value("${meerkat.tomcat.down.url}")
	private String tomcatDownUrl;
	
	@Value("${meerkat.agent.file.name:athena-meerkat-agent-1.0.0-SNAPSHOT}")
	private String agentFileName;// 확장자 제외 파일명.
	
	@Value("${meerkat.dolly.jar.name}")
	private String dollyAgentJarName;
	
	protected File commanderDir;
	
	private Configuration cfg;
	
	@Autowired
	protected TaskHistoryService taskService;
	
	@Autowired
	protected TomcatVersionsProperties tomcatVerProps;
	
	

	
	protected void sendCommand(ProvisionModel pModel, String cmdFileName, WebSocketSession session) {
		sendCommand(pModel, cmdFileName, session, null);
	}
	protected void sendCommand(ProvisionModel pModel, String cmdFileName, WebSocketSession session, AdditionalTask task) {

		File jobDir = generateBuildProperties(pModel, session);

		try {

			/*
			 * 1. copy cmd.xml & default.xml.
			 */
			copyCmds(cmdFileName, jobDir);

			/*
			 * 2. send cmd.
			 */
			boolean isSuccess = ProvisioningUtil.sendCommand(commanderDir, jobDir);
			
			if(task != null) {
				task.runTask(jobDir);
			}
			
			if (isSuccess) {
				updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_SUCCESS);
			} else {
				updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_FAIL);
			}

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			
			updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_FAIL);
			//throw new RuntimeException(e);

		} finally {
			LOGGER.debug(LOG_END);
			MDC.remove("jobPath");
			MDC.remove("serverIp");
		}
	}

	/**
	 * <pre>
	 * jobDir 에서 cmdFileName ant script를 실행.
	 * - 주로 commander가 직접  scp or sshexec 등의 명령을 수행.
	 * </pre>
	 * @param pModel
	 * @param cmdFileName
	 * @param session
	 */
	protected void runCommand(ProvisionModel pModel, String cmdFileName, WebSocketSession session) {

		File jobDir = generateBuildProperties(pModel, session);

		try {

			/*
			 * 1. copy cmd.xml & default.xml.
			 */
			copyCmds(cmdFileName, jobDir);

			/*
			 * 2. run cmd.
			 */
			boolean isSuccess = ProvisioningUtil.runCommand(commanderDir, jobDir);
			
			if (isSuccess) {
				updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_SUCCESS);
			} else {
				updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_FAIL);
			}

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			
			updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_FAIL);
			//throw new RuntimeException(e);

		} finally {
			LOGGER.debug(LOG_END);
			MDC.remove("jobPath");
			MDC.remove("serverIp");
		}
	}
	
	protected boolean runDefaultTargets(ProvisionModel pModel, WebSocketSession session, String... targetNames) {

		File jobDir = generateBuildProperties(pModel, session);

		boolean isSuccess = true;
		try {

			/*
			 * 1. copy default.xml.
			 */
			copyDefault(jobDir);

			/*
			 * 2. run targetNames.
			 */
			for (String targetName : targetNames) {
				isSuccess = ProvisioningUtil.runDefaultTarget(commanderDir, jobDir, targetName) && isSuccess;
			}
			
			if (isSuccess) {
				updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_SUCCESS);
			} else {
				updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_FAIL);
			}

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			
			updateTaskStatus(pModel, MeerkatConstants.TASK_STATUS_FAIL);
			//throw new RuntimeException(e);

		} finally {
			LOGGER.debug(LOG_END);
			MDC.remove("jobPath");
			MDC.remove("serverIp");
		}
		
		return isSuccess;
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
	protected File generateBuildProperties(ProvisionModel pModel, WebSocketSession session) {

		TomcatInstance tomcatInstance = pModel.getTomcatInstance();
		DomainTomcatConfiguration tomcatConfig = pModel.getTomcatConfig();
		List<TomcatConfigFile> confFiles = pModel.getConfFiles();

		Server targetServer = pModel.getServer();
		String serverIp = targetServer.getSshIPAddr();
		MDC.put("serverIp", serverIp);

		List<SshAccount> accounts = (List<SshAccount>) targetServer.getSshAccounts();

		String userId = accounts.get(0).getUsername();
		String userPass = accounts.get(0).getPassword();

		Properties prop = new Properties(); // build-ssh.properties
		prop.setProperty("server.ip", serverIp);
		prop.setProperty("server.id", String.valueOf(targetServer.getId()));
		prop.setProperty("server.port", String.valueOf(targetServer.getSshPort()));
		prop.setProperty("user.id", userId);
		prop.setProperty("user.passwd", userPass);
		prop.setProperty("key.file", commanderDir + "/ssh/svn_key.pem");

		String agentDeployDir = "/home/" + userId + "/athena-meerkat-agent";
		//String agentName = "athena-meerkat-agent-1.0.0-SNAPSHOT";

		Properties targetProps = new Properties(); // build.properties
		targetProps.setProperty("agent.deploy.dir", agentDeployDir);
		targetProps.setProperty("agent.name", 		agentFileName);
		targetProps.setProperty("server.ant.home", 	agentDeployDir + "/" + agentFileName + "/apache-ant-1.9.6");
		targetProps.setProperty("tomcat.down.url", 	tomcatDownUrl);
		
		if (tomcatInstance != null) {
			targetProps.setProperty("tomcat.instance.name",	tomcatInstance.getName());
		}
		
		if (tomcatConfig != null) {
			targetProps.setProperty("catalina.home", 	tomcatConfig.getCatalinaHome());
			targetProps.setProperty("catalina.base", 	tomcatConfig.getCatalinaBase());
			targetProps.setProperty("tomcat.name", 		getTomcatName(tomcatConfig.getTomcatVersionCd()));
			targetProps.setProperty("tomcat.major.ver", String.valueOf(tomcatVerProps.getTomcatMajorVersion(tomcatConfig.getTomcatVersionCd())));
			targetProps.setProperty("tomcat.full.ver",  getTomcatVersion(tomcatConfig.getTomcatVersionCd()));
			targetProps.setProperty("am.server.port", 	String.valueOf(tomcatConfig.getServerPort()));
			targetProps.setProperty("am.http.port", 	String.valueOf(tomcatConfig.getHttpPort()));
			targetProps.setProperty("am.ajp.port", 		String.valueOf(tomcatConfig.getAjpPort()));
			targetProps.setProperty("am.uri.encoding", 	tomcatConfig.getEncoding());
			targetProps.setProperty("am.rmi.registry.port", String.valueOf(tomcatConfig.getRmiRegistryPort()));
			targetProps.setProperty("am.rmi.server.port", 	String.valueOf(tomcatConfig.getRmiServerPort()));
			targetProps.setProperty("am.ssion.timeout", 	String.valueOf(tomcatConfig.getSessionTimeout()));
		}
		

		targetProps.setProperty("am.conf.op", pModel.getConfigOP());
		
		if (pModel.getPropsMap() != null) {
			targetProps.putAll(pModel.getPropsMap());// additional properties.
		}

		if (confFiles != null) {
			/*
			 * used in default.xml (update-config target)
			 */
			for (TomcatConfigFile confFile : confFiles) {
				targetProps.setProperty(codeHandler.getFileTypeName(confFile.getFileTypeCdId()) + ".file", configFileService.getFileFullPath(confFile));
			}
		}

		OutputStream output = null;
		File jobDir = null;
		try {
			int jobNum = getJobNumber(serverIp);
			targetProps.setProperty("job.number", String.valueOf(jobNum));
			jobDir = makeJobDir(serverIp, jobNum);
			
			if (pModel.getTaskHistoryId() > 0) {
				
				taskService.updateTaskLogFile(pModel.getTaskHistoryId(), pModel.getTomcatInstance(), jobDir);
			}

			MDC.put("jobPath", jobDir.getAbsolutePath());

			sendLog(session, jobDir.getAbsolutePath(), pModel.isLastTask());

			if (CollectionUtils.isEmpty(accounts)) {
				throw new RuntimeException("생성할 ssh 계정정보가 없습니다.");
			}

			LOGGER.debug("SERVER NAME : " + targetServer.getName());

			/*
			 * generate agentenv.sh
			 */
			if (tomcatConfig != null) {
				createAgentEnvSHFile(jobDir, tomcatConfig.getJavaHome(), agentDeployDir, agentFileName);
				generateTomcatEnvFile(jobDir, tomcatConfig, serverIp);
			} else {
				createAgentEnvSHFile(jobDir, null, agentDeployDir, agentFileName);
			}
			

			/*
			 * generate build properties
			 */
			output = new FileOutputStream(jobDir.getAbsolutePath() + File.separator + "build-ssh.properties");
			prop.store(output, PROPS_COMMENTS);
			LOGGER.debug("generated build-ssh.properties");

			IOUtils.closeQuietly(output);

			output = new FileOutputStream(jobDir.getAbsolutePath() + File.separator + "build.properties");
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
	 * 실행할 ant commands file (cmd.xml & default.xml) 을 jobDir 에 copy 한다.
	 * </pre>
	 * 
	 * @param cmdFileName
	 * @param jobDir
	 * @throws IOException
	 */
	protected void copyCmds(String cmdFileName, File jobDir) throws IOException {
		String cmdsPath = getCmdsPath();
		FileUtils.copyFileToDirectory(new File(cmdsPath + "default.xml"), jobDir);
		FileUtils.copyFile(new File(cmdsPath + cmdFileName), new File(jobDir.getAbsolutePath() + File.separator + "cmd.xml"));
	}
	
	private void copyDefault(File jobDir) throws IOException {
		copyAntScript("default.xml", jobDir);
	}
	
	protected void copyAntScript(String cmdFileName, File jobDir) throws IOException {
		String cmdsPath = getCmdsPath();
		FileUtils.copyFileToDirectory(new File(cmdsPath + cmdFileName), jobDir);
	}
	
	private String getCmdsPath() {
		return commanderDir.getAbsolutePath() + File.separator + "cmds" + File.separator;
	}

	

	/**
	 * <pre>
	 * send log to client(UI)
	 * </pre>
	 * 
	 * @param session
	 * @param jobPath
	 */
	private void sendLog(WebSocketSession session, String jobPath, boolean lastTask) {

		if (session != null) {

			LogTailerListener listener = new LogTailerListener(session, lastTask);
			long delay = 2000;
			File file = new File(jobPath + File.separator + "build.log");
			LOGGER.debug("log file : {}", file.getAbsoluteFile());

			Tailer tailer = new Tailer(file, listener, delay);
			new Thread(tailer).start();
		}

	}
	
	public void sendLog(WebSocketSession session, int taskHistoryDetailId) {

		TaskHistoryDetail taskDetail = taskService.getTaskHistoryDetail(taskHistoryDetailId);

		LogTailerListener listener = new LogTailerListener(session, true);
		long delay = 2000;
		File file = new File(taskDetail.getLogFilePath());
		LOGGER.debug("log file : {}", file.getAbsoluteFile());

		Tailer tailer = new Tailer(file, listener, delay);
		new Thread(tailer).start();

	}

	private int getJobNumber(String serverIp) throws IOException {
		int jobNum = ProvisioningUtil.getJobNum(new File(commanderDir.getAbsolutePath() + File.separator + JOBS_DIR_NM + File.separator + serverIp));

		return jobNum;
	}

	private File makeJobDir(String serverIp, int jobNum) {
		File jobDir = new File(commanderDir.getAbsolutePath() + File.separator + JOBS_DIR_NM + File.separator + serverIp + File.separator
				+ String.valueOf(jobNum));
		if (jobDir.exists() == false) {
			jobDir.mkdirs();
		}
		LOGGER.debug("JOB_DIR : " + jobDir.getAbsolutePath());

		return jobDir;
	}
	

	private void generateTomcatEnvFile(File jobDir, DomainTomcatConfiguration tomcatConfig, String rmiHostname) throws IOException {

		Map<String, String> model = new HashMap<String, String>();
		model.put("javaHome", tomcatConfig.getJavaHome());
		model.put("catalinaHome", tomcatConfig.getCatalinaHome());
		model.put("catalinaBase", tomcatConfig.getCatalinaBase());
		model.put("catalinaOpts", tomcatConfig.getCatalinaOpts());
		model.put("rmiHostname", rmiHostname);

		Writer output = null;

		try {
			output = new FileWriter(jobDir.getAbsolutePath() + File.separator + "env.sh");

			generate("templates/env.sh.ftl", model, output);

			LOGGER.debug("generated env.sh");
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

	private void createAgentEnvSHFile(File jobDir, String javaHome, String deployDir, String agentName) throws IOException {
		Map<String, String> model = new HashMap<String, String>();
		
		if (javaHome != null) {
			model.put("javaHome", javaHome);
		}
		model.put("deployDir", deployDir);
		model.put("agentName", agentName);

		Writer output = null;

		try {
			output = new FileWriter(jobDir.getAbsolutePath() + File.separator + "agentenv.sh");

			generate("templates/agentenv.sh.ftl", model, output);

			LOGGER.debug("generated agentenv.sh");
		} finally {
			IOUtils.closeQuietly(output);
		}

	}

	private boolean generate(String templateFileName, Object dataModel, Writer out) throws IOException {

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

	private String getTomcatName(int tomcatVersionCd) {
		return codeHandler.getCodeNm(MeerkatConstants.CODE_GROP_TE_VERSION, tomcatVersionCd);
	}
	
	/**
	 * <pre>
	 * get tomcat full version (e.g. "7.0.68")
	 * </pre>
	 * @param tomcatVersionCd
	 * @return
	 */
	protected String getTomcatVersion(int tomcatVersionCd) {
		
		String tomcatName = getTomcatName(tomcatVersionCd);
		return tomcatName.substring(tomcatName.lastIndexOf("-")+1);
	}
	
	protected void initService() {
		cfg = new Configuration(Configuration.VERSION_2_3_22);

		// cfg.setDirectoryForTemplateLoading(new
		// File("/where/you/store/templates"));
		cfg.setTemplateLoader(new ClassTemplateLoader(getClass(), "/"));
		cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_22));
		cfg.setDefaultEncoding("UTF-8");

		// During web page *development*
		// TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
		// cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		
		Assert.notNull(commanderHome);
		commanderDir = new File(commanderHome);
	}
	
	protected void addDollyDefaultProperties(ProvisionModel pModel, int sessionGroupId) {
		pModel.addProps("dolly.jar.name", dollyAgentJarName);
		pModel.addProps("ant.script.file", "installDolly.xml");
		pModel.addProps("infinispan.hotrod.server_list", dataGridGroupService.getServerListPropertyValue(sessionGroupId));
	}
	
	protected void updateTaskStatus(ProvisionModel pModel, int status) {
		if (pModel.getTaskHistoryId() > 0) {
			taskService.updateTaskStatus(pModel.getTaskHistoryId(), pModel.getTomcatInstance(), status);
		} else {
			LOGGER.warn("TaskHistory id is zero.");
		}
	}
	
	public void afterPropertiesSet() throws Exception {

		initService();
	}

}
//end of AbstractProvisioningService.java