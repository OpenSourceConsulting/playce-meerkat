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
 * Sang-cheon Park	2013. 10. 21.		First Draft.
 */
package com.athena.meerkat.controller.common.provisioning;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.jboss.ews.dbcp.BlowfishEncrypter;
import org.picketbox.datasource.security.SecureIdentityLoginModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.athena.meerkat.common.core.action.ConfigAction;
import com.athena.meerkat.common.core.action.FileWriteAction;
import com.athena.meerkat.common.core.action.ShellAction;
import com.athena.meerkat.common.core.action.support.Property;
import com.athena.meerkat.common.core.command.Command;
import com.athena.meerkat.common.netty.MeerkatDatagram;
import com.athena.meerkat.common.netty.message.AbstractMessage;
import com.athena.meerkat.common.netty.message.ProvisioningCommandMessage;
import com.athena.meerkat.common.netty.message.ProvisioningResponseMessage;
import com.athena.meerkat.controller.netty.MeerkatTransmitter;
//import com.athena.meerkat.controller.web.ceph.CephService;
//import com.athena.meerkat.controller.web.ceph.base.CephDto;
//import com.athena.meerkat.controller.web.config.ConfigDto;
//import com.athena.meerkat.controller.web.config.ConfigService;
//import com.athena.meerkat.controller.web.software.SoftwareDto;
//import com.athena.meerkat.controller.web.software.SoftwareService;

/**
 * <pre>
 * Software 설치 및 Config 파일 변경 등 프로비저닝 관련 지원 클래스
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("provisioningHandler")
public class ProvisioningHandler {

	protected final Logger LOGGER = LoggerFactory
			.getLogger(ProvisioningHandler.class);

	@Inject
	@Named("meerkatTransmitter")
	private MeerkatTransmitter meerkatTransmitter;

	public void install(ProvisioningDetail provisioningDetail) throws Exception {
		tomcatInstall(provisioningDetail);
	}

	public void remove(ProvisioningDetail provisioningDetail) throws Exception {
		tomcatRemove(provisioningDetail);
	}

	private void tomcatInstall(ProvisioningDetail provisioningDetail)
			throws Exception {
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);

		/**
		 * Tomcat Variables
		 */
		String fileLocation = provisioningDetail.getFileLocation();
		String fileName = provisioningDetail.getFileName();

		String user = provisioningDetail.getUser();
		String group = provisioningDetail.getGroup();
		String javaHome = provisioningDetail.getJavaHome();
		String serverHome = provisioningDetail.getServerHome();
		String serverName = provisioningDetail.getServerName();
		String catalinaHome = provisioningDetail.getCatalinaHome();
		String catalinaBase = provisioningDetail.getCatalinaBase();
		String portOffset = (StringUtils.isEmpty(provisioningDetail
				.getPortOffset()) ? "0" : provisioningDetail.getPortOffset());
		String encoding = provisioningDetail.getEncoding();
		String heapSize = (StringUtils
				.isEmpty(provisioningDetail.getHeapSize()) ? "1024"
				: provisioningDetail.getHeapSize());
		String permgenSize = (StringUtils.isEmpty(provisioningDetail
				.getPermgenSize()) ? "256" : provisioningDetail
				.getPermgenSize());
		String httpEnable = provisioningDetail.getHttpEnable();
		String highAvailability = provisioningDetail.getHighAvailability();
		String bindAddress = provisioningDetail.getBindAddress();
		String otherBindAddress = provisioningDetail.getOtherBindAddress();
		String localIPAddress = provisioningDetail.getLocalIPAddress();
		String hostName = provisioningDetail.getHostName();

		LOGGER.debug("fileLocation : " + fileLocation);
		LOGGER.debug("fileName : " + fileName);
		LOGGER.debug("user : " + user);
		LOGGER.debug("group : " + group);
		LOGGER.debug("serverHome : " + serverHome);
		LOGGER.debug("serverName : " + serverName);
		LOGGER.debug("catalinaHome : " + catalinaHome);
		LOGGER.debug("catalinaBase : " + catalinaBase);
		LOGGER.debug("portOffset : " + portOffset);
		LOGGER.debug("encoding : " + encoding);
		LOGGER.debug("heapSize : " + heapSize);
		LOGGER.debug("permgenSize : " + permgenSize);
		LOGGER.debug("httpEnable : " + httpEnable);
		LOGGER.debug("highAvailability : " + highAvailability);
		LOGGER.debug("bindAddress : " + bindAddress);
		LOGGER.debug("otherBindAddress : " + otherBindAddress);
		LOGGER.debug("localIPAddress : " + localIPAddress);
		LOGGER.debug("hostName : " + hostName);
		LOGGER.debug("machineId : " + provisioningDetail.getMachineId());
		LOGGER.debug("softwareId : " + provisioningDetail.getSoftwareId());
		LOGGER.debug("softwareName : " + provisioningDetail.getSoftwareName());
		LOGGER.debug("autoStart : " + provisioningDetail.getAutoStart());

		/**
		 * DataSource Variables
		 */
		String[] databaseType = provisioningDetail.getDatabaseType();
		String[] jndiName = provisioningDetail.getJndiName();
		String[] connectionUrl = provisioningDetail.getConnectionUrl();
		String[] userName = provisioningDetail.getUserName();
		String[] password = provisioningDetail.getPassword();
		String[] maxIdle = provisioningDetail.getMaxIdle();
		String[] maxActive = provisioningDetail.getMaxActive();

		LOGGER.debug("databaseType : " + databaseType);
		LOGGER.debug("jndiName : " + jndiName);
		LOGGER.debug("connectionUrl : " + connectionUrl);
		LOGGER.debug("userName : " + userName);
		LOGGER.debug("password : " + password);
		LOGGER.debug("maxIdle : " + maxIdle);
		LOGGER.debug("maxActive : " + maxActive);

		Command command = new Command("Tomcat INSTALL");
		ShellAction sAction = null;
		int sequence = 0;

		sAction = new ShellAction(sequence++);
		sAction.setWorkingDiretory("/tmp");
		sAction.setCommand("wget");
		sAction.addArguments("${RepositoryUrl}" + fileLocation
				+ "/apr-1.3.9-5.el6_2.x86_64.rpm");
		sAction.addArguments("-O");
		sAction.addArguments("apr-1.3.9-5.el6_2.x86_64.rpm");
		command.addAction(sAction);

		sAction = new ShellAction(sequence++);
		sAction.setWorkingDiretory("/tmp");
		sAction.setCommand("wget");
		sAction.addArguments("${RepositoryUrl}" + fileLocation
				+ "/apr-util-1.3.9-3.el6_0.1.x86_64.rpm");
		sAction.addArguments("-O");
		sAction.addArguments("apr-util-1.3.9-3.el6_0.1.x86_64.rpm");
		command.addAction(sAction);

		sAction = new ShellAction(sequence++);
		sAction.setWorkingDiretory("/tmp");
		sAction.setCommand("rpm");
		sAction.addArguments("-Uvh");
		sAction.addArguments("apr-1.3.9-5.el6_2.x86_64.rpm");
		command.addAction(sAction);

		sAction = new ShellAction(sequence++);
		sAction.setWorkingDiretory("/tmp");
		sAction.setCommand("rpm");
		sAction.addArguments("-Uvh");
		sAction.addArguments("apr-util-1.3.9-3.el6_0.1.x86_64.rpm");
		command.addAction(sAction);

		String[] fileNames = fileName.split(",");

		for (int i = 0; i < fileNames.length; i++) {
			sAction = new ShellAction(sequence++);
			sAction.setWorkingDiretory("/tmp");
			sAction.setCommand("wget");
			sAction.addArguments("${RepositoryUrl}" + fileLocation + "/"
					+ fileNames[i]);
			sAction.addArguments("-O");
			sAction.addArguments(fileNames[i]);
			command.addAction(sAction);

			sAction = new ShellAction(sequence++);
			sAction.setCommand("mkdir");
			sAction.addArguments("-p");
			sAction.addArguments(serverHome);
			command.addAction(sAction);

			sAction = new ShellAction(sequence++);
			sAction.setWorkingDiretory("/tmp");
			sAction.setCommand("unzip");
			sAction.addArguments("-o");
			sAction.addArguments(fileNames[i]);
			sAction.addArguments("-d");
			sAction.addArguments(serverHome);
			command.addAction(sAction);
		}

		// Add Tomcat INSTALL Command
		cmdMsg.addCommand(command);

		command = new Command("Directory & File Setting");
		sequence = 0;

		// mv /home/${user}/jbossews /etc/init.d/jbossews
		sAction = new ShellAction(sequence++);
		sAction.setWorkingDiretory(serverHome);
		sAction.setCommand("mv");
		sAction.addArguments("jbossews");
		sAction.addArguments("/etc/init.d/jbossews");
		command.addAction(sAction);

		// mv /home/${user}/Servers/codeServer21
		// /home/${user}/Servers/${user}Server21
		sAction = new ShellAction(sequence++);
		sAction.setWorkingDiretory(serverHome + "/Servers");
		sAction.setCommand("mv");
		sAction.addArguments("codeServer21");
		sAction.addArguments(serverName);
		command.addAction(sAction);

		// Add Directory & File Setting Command
		cmdMsg.addCommand(command);

		command = new Command("Set Configurations");
		sequence = 0;

		String envSh = IOUtils.toString(
				new URL(provisioningDetail.getUrlPrefix()
						+ "/repo/tomcat/conf/env.sh"), "UTF-8");
		envSh = envSh.replaceAll("\\$\\{user\\}", user)
				.replaceAll("\\$\\{java.home\\}", javaHome)
				.replaceAll("\\$\\{server.home\\}", serverHome)
				.replaceAll("\\$\\{server.name\\}", serverName)
				.replaceAll("\\$\\{catalina.home\\}", catalinaHome)
				.replaceAll("\\$\\{catalina.base\\}", catalinaBase)
				.replaceAll("\\$\\{port.offset\\}", portOffset)
				.replaceAll("\\$\\{encoding\\}", encoding)
				.replaceAll("\\$\\{heap.size\\}", heapSize)
				.replaceAll("\\$\\{permgen.size\\}", permgenSize)
				.replaceAll("\\$\\{bind.addr\\}", bindAddress)
				.replaceAll("\\$\\{other.bind.addr\\}", otherBindAddress);

		FileWriteAction writeAction = new FileWriteAction(sequence++);
		writeAction.setContents(envSh);
		writeAction.setFileName(catalinaBase + "/env.sh");
		command.addAction(writeAction);

		List<Property> properties = new ArrayList<Property>();
		Property property = null;

		property = new Property();
		property.setKey("user");
		property.setValue(user);
		properties.add(property);

		property = new Property();
		property.setKey("java.home");
		property.setValue(javaHome);
		properties.add(property);

		property = new Property();
		property.setKey("server.home");
		property.setValue(serverHome);
		properties.add(property);

		property = new Property();
		property.setKey("server.name");
		property.setValue(serverName);
		properties.add(property);

		property = new Property();
		property.setKey("catalina.home");
		property.setValue(catalinaHome);
		properties.add(property);

		property = new Property();
		property.setKey("catalina.base");
		property.setValue(catalinaBase);
		properties.add(property);

		/**
		 * sed -i 's/code/'${USER}'/g' /etc/init.d/jbossews
		 */
		ConfigAction configAction = new ConfigAction(sequence);
		sequence++;
		configAction.setFileName("/etc/init.d/jbossews");
		configAction.setProperties(properties);
		command.addAction(configAction);

		// Set server.xml
		String serverFileName = "server-all.xml";
		String serverXml = null;

		if (httpEnable != null && httpEnable.toUpperCase().equals("Y")) {
			if (highAvailability != null
					&& highAvailability.toUpperCase().equals("Y")) {
				serverFileName = "server-all.xml";
			} else {
				serverFileName = "server-http.xml";
			}
		} else {
			if (highAvailability != null
					&& highAvailability.toUpperCase().equals("Y")) {
				serverFileName = "server-cluster.xml";
			} else {
				serverFileName = "server-none.xml";
			}
		}

		serverXml = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix()
				+ "/repo/tomcat/conf/" + serverFileName), "UTF-8");

		writeAction = new FileWriteAction(sequence++);
		writeAction.setContents(serverXml);
		writeAction.setFileName(catalinaBase + "/conf/server.xml");
		command.addAction(writeAction);

		// Set context.xml
		StringBuilder datasource = new StringBuilder();
		String driverClassName = null;
		String encPasswd = null;
		String query = null;
		if (databaseType != null) {
			for (int i = 0; i < databaseType.length; i++) {
				if (StringUtils.isNotEmpty(databaseType[i])
						&& StringUtils.isNotEmpty(jndiName[i])
						&& StringUtils.isNotEmpty(connectionUrl[i])
						&& StringUtils.isNotEmpty(userName[i])
						&& StringUtils.isNotEmpty(password[i])) {

					if (databaseType[i].toLowerCase().equals("oracle")) {
						driverClassName = "oracle.jdbc.driver.OracleDriver";
						query = "SELECT 1 FROM DUAL";
					} else if (databaseType[i].toLowerCase().equals("mysql")) {
						driverClassName = "com.mysql.jdbc.Driver";
						query = "SELECT 1";
					} else if (databaseType[i].toLowerCase().equals("mssql")) {
						driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
						query = "SELECT 1";
					} else if (databaseType[i].toLowerCase().equals("db2")) {
						driverClassName = "com.ibm.db2.jcc.DB2Driver";
						query = "VALUES 1";
					}

					encPasswd = ewsPasswordEncrypt(password[i]);

					datasource.append(
							"	<Resource name=\"" + jndiName[i]
									+ "\" auth=\"Container\"").append("\n");
					datasource.append(
							"	          type=\"javax.sql.DataSource\" driverClassName=\""
									+ driverClassName + "\"").append("\n");
					datasource.append(
							"	          url=\"" + connectionUrl[i] + "\"")
							.append("\n");
					datasource
							.append("	          factory=\"org.jboss.ews.dbcp.EncryptDataSourceFactory\"")
							.append("\n");
					datasource.append(
							"	          username=\"" + userName[i]
									+ "\" password=\"" + encPasswd + "\"")
							.append("\n");
					datasource.append("	          initialSize=\"2\"").append(
							"\n");
					datasource.append(
							"	          maxActive=\"" + maxActive[i] + "\"")
							.append("\n");
					datasource.append(
							"	          maxIdle=\"" + maxIdle[i] + "\"")
							.append("\n");
					datasource.append("	          maxWait=\"-1\"").append("\n");
					datasource.append(
							"	          validationQuery=\"" + query + "\"")
							.append("\n");
					datasource.append("	          testOnBorrow=\"true\"")
							.append("\n");
					datasource.append(
							"	          poolPreparedStatements=\"true\"")
							.append("\n");
					datasource.append(
							"	          maxOpenPreparedStatements=\"10\"")
							.append("\n");
					datasource.append("	          removeAbandoned=\"true\"")
							.append("\n");
					datasource.append(
							"	          removeAbandonedTimeout=\"60\"").append(
							"\n");
					datasource.append("	          logAbandoned=\"true\"")
							.append("\n");
					datasource.append("	/>").append("\n");
				}
			}
		}

		String contextXml = IOUtils.toString(
				new URL(provisioningDetail.getUrlPrefix()
						+ "/repo/tomcat/conf/context.xml"), "UTF-8");
		contextXml = contextXml.replaceAll("\\$\\{datasource\\}",
				datasource.toString());

		writeAction = new FileWriteAction(sequence++);
		writeAction.setContents(contextXml);
		writeAction.setFileName(catalinaBase + "/conf/context.xml");
		command.addAction(writeAction);

		// Add Set Configurations Command
		cmdMsg.addCommand(command);

		command = new Command("Set Owner");
		sequence = 0;

		// chonw -R ${user}:${group} /home/${user}
		sAction = new ShellAction(sequence++);
		sAction.setCommand("chown");
		sAction.addArguments("-R");
		sAction.addArguments(user + ":" + group);
		sAction.addArguments(serverHome);
		command.addAction(sAction);

		// Add Set IP & Hostname Command
		cmdMsg.addCommand(command);

		command = new Command("Set chkconfig & Start service");
		sequence = 0;

		/** chkconfig --add jbossews */
		sAction = new ShellAction(sequence++);
		sAction.setCommand("chkconfig");
		sAction.addArguments("--add");
		sAction.addArguments("jbossews");
		command.addAction(sAction);

		/** chkconfig jbossews on */
		sAction = new ShellAction(sequence++);
		sAction.setCommand("chkconfig");
		sAction.addArguments("jbossews");
		sAction.addArguments("on");
		command.addAction(sAction);

		if (provisioningDetail.getAutoStart().equals("Y")) {
			sAction = new ShellAction(sequence++);
			sAction.setCommand("service");
			sAction.addArguments("jbossews");
			sAction.addArguments("start");
			command.addAction(sAction);
		}

		// Add Set chkconfig & Start service Command
		cmdMsg.addCommand(command);

		new InstallThread(meerkatTransmitter, cmdMsg).start();
	}

	private void tomcatRemove(ProvisioningDetail provisioningDetail)
			throws Exception {

		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);

		Command command = new Command("Uninstall");
		int sequence = 0;

		ShellAction sAction = new ShellAction(sequence++);
		sAction.setCommand("service");
		sAction.addArguments("jbossews");
		sAction.addArguments("stop");
		command.addAction(sAction);

		// for (ConfigDto configDto : configList) {
		// sAction = new ShellAction(sequence++);
		// sAction.setCommand("rm");
		// sAction.addArguments("-f");
		// sAction.addArguments(configDto.getConfigFileLocation() + "/"
		// + configDto.getConfigFileName());
		// command.addAction(sAction);
		// }
		//
		// String[] installLocation = software.getInstallLocation().split(",");
		// for (String location : installLocation) {
		// sAction = new ShellAction(sequence++);
		// sAction.setCommand("rm");
		// sAction.addArguments("-rf");
		// sAction.addArguments(location);
		// command.addAction(sAction);
		// }
		//
		// // Add Uninstall Command
		// cmdMsg.addCommand(command);
		//
		// /***************************************************************
		// * software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		// ***************************************************************/
		// software.setInstallStat("UNINSTALLING");
		//
		// new UninstallThread(meerkatTransmitter, softwareService,
		// configService,
		// cmdMsg, software, config).start();
		new UninstallThread(meerkatTransmitter, cmdMsg).start();
	}

	private String ewsPasswordEncrypt(String password) {
		return BlowfishEncrypter.encrypt(password);
	}

	private String eapPasswordEncrypt(String password) throws Exception {
		SecureIdentityLoginModule module = new SecureIdentityLoginModule();

		Method method = module.getClass().getDeclaredMethod("encode",
				String.class);
		method.setAccessible(true);

		return (String) method.invoke(module, password);
	}
}

// end of ProvisioningHandler.java

class InstallThread extends Thread {

	protected final Logger LOGGER = LoggerFactory
			.getLogger(InstallThread.class);

	private final MeerkatTransmitter meerkatTransmitter;
	// private final SoftwareService softwareService;
	private final ProvisioningCommandMessage cmdMsg;

	// private final SoftwareDto software;
	// private final List<ConfigDto> configList;
	// private final CephService cephService;
	// private final CephDto cephDto;

	// public InstallThread(meerkatTransmitter meerkatTransmitter,
	// SoftwareService softwareService, ProvisioningCommandMessage cmdMsg,
	// SoftwareDto software, List<ConfigDto> configList,
	// CephService cephService, CephDto cephDto) {
	public InstallThread(MeerkatTransmitter meerkatTransmitter,
			ProvisioningCommandMessage cmdMsg) {
		this.meerkatTransmitter = meerkatTransmitter;
		// this.softwareService = softwareService;
		this.cmdMsg = cmdMsg;
		// this.software = software;
		// this.configList = configList;
		// this.cephService = cephService;
		// this.cephDto = cephDto;
	}

	@Override
	public void run() {
		try {
			// softwareService.insertSoftware(software);

			MeerkatDatagram<AbstractMessage> datagram = new MeerkatDatagram<AbstractMessage>(
					cmdMsg);
			ProvisioningResponseMessage response = meerkatTransmitter
					.sendMessage(datagram);

			// LOGGER.debug("[{}] Installed.", software.getSoftwareName());
			LOGGER.debug("[{}] Installed.", "software.getSoftwareName()");
			StringBuilder sb = new StringBuilder("");
			List<String> commands = response.getCommands();
			List<String> results = response.getResults();

			for (int i = 0; i < results.size(); i++) {
				if (i < commands.size()) {
					if (commands.get(i) != null) {
						sb.append("[Command] : " + commands.get(i) + "\n");

						if (commands.get(i).indexOf("radosgw-admin user info") > -1) {
							try {
								ObjectMapper mapper = new ObjectMapper();
								JsonNode node = mapper.readTree(results.get(i));
								JsonNode keys = node.get("keys");

								String accessKey = null;
								String secretKey = null;
								if (keys.isArray()) {
									for (JsonNode key : (ArrayNode) keys) {
										if (key.get("user").asText()
												.equals("osc")) {
											accessKey = key.get("access_key")
													.asText();
											secretKey = key.get("secret_key")
													.asText();
										}
									}
								} else {
									if (keys.get("user").asText().equals("osc")) {
										accessKey = keys.get("access_key")
												.asText();
										secretKey = keys.get("secret_key")
												.asText();
									}
								}

								LOGGER.debug("S3 AccessKey : [{}]", accessKey);
								LOGGER.debug("S3 SecretKey : [{}]", secretKey);

								// cephDto.setS3AccessKey(accessKey);
								// cephDto.setS3SecretKey(secretKey);
							} catch (Exception e) {
								LOGGER.error(
										"Unhandled Exception has occurred during parsing S3 credentials.",
										e);
							}
						}
					}
				}

				sb.append(results.get(i)).append("\n");
			}

			// software.setInstallStat("COMPLETED");
			// software.setInstallLog(sb.toString());
			//
			// LOGGER.debug("Install Log : [{}]", sb.toString());
			//
			// softwareService.insertSoftware(software, configList);
			//
			// LOGGER.debug("cephService's instance : [{}]", cephService);
			// LOGGER.debug("cephDto's instance : [{}]", cephDto);
			//
			// if (cephService != null && cephDto != null) {
			// LOGGER.debug("cephDto will be inserted to DB");
			// cephService.insertCeph(cephDto);
			// }
		} catch (Exception e) {
			// software.setInstallStat("INST_ERROR");
			// software.setInstallLog(e.getMessage());
			//
			// try {
			// softwareService.insertSoftware(software);
			// } catch (Exception e1) {
			// e1.printStackTrace();
			// }

			LOGGER.error("Unhandled Exception has occurred.", e);
		}
	}
}

class UninstallThread extends Thread {

	protected final Logger LOGGER = LoggerFactory
			.getLogger(UninstallThread.class);

	private final MeerkatTransmitter meerkatTransmitter;
	// private final SoftwareService softwareService;
	// private final ConfigService configService;
	private final ProvisioningCommandMessage cmdMsg;

	// private final SoftwareDto software;
	// private final ConfigDto config;

	// public UninstallThread(meerkatTransmitter meerkatTransmitter,
	// SoftwareService softwareService, ConfigService configService,
	// ProvisioningCommandMessage cmdMsg, SoftwareDto software,
	// ConfigDto config) {
	//
	public UninstallThread(MeerkatTransmitter meerkatTransmitter,
			ProvisioningCommandMessage cmdMsg) {
		this.meerkatTransmitter = meerkatTransmitter;
		// this.softwareService = softwareService;
		// this.configService = configService;
		this.cmdMsg = cmdMsg;
		// this.software = software;
		// this.config = config;
	}

	@Override
	public void run() {
		try {
			// softwareService.updateSoftware(software);

			MeerkatDatagram<AbstractMessage> datagram = new MeerkatDatagram<AbstractMessage>(
					cmdMsg);
			ProvisioningResponseMessage response = meerkatTransmitter
					.sendMessage(datagram);

			// LOGGER.debug("[{}] Uninstalled.", software.getSoftwareName());

			// configService.deleteConfig(config);

			StringBuilder sb = new StringBuilder();
			List<String> commands = response.getCommands();
			List<String> results = response.getResults();

			for (int i = 0; i < results.size(); i++) {
				if (commands.get(i) != null) {
					sb.append("[Command] : ").append(commands.get(i))
							.append("\n");
				}

				sb.append(results.get(i)).append("\n");
			}
			// software.setDeleteYn("Y");
			// software.setInstallStat("DELETED");
			// software.setInstallLog(sb.toString());
			//
			// LOGGER.debug("Uninstall Log : [{}]", sb.toString());
			//
			// softwareService.updateSoftware(software);
		} catch (Exception e) {
			// software.setInstallStat("UNINST_ERROR");
			// software.setInstallLog(e.getMessage());
			//
			// try {
			// softwareService.updateSoftware(software);
			// } catch (Exception e1) {
			// e1.printStackTrace();
			// }
			//
			LOGGER.error("Unhandled Exception has occurred.", e);
		}
	}
}
