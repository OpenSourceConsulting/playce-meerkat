/*
 * Copyright 2013 The Athena-Peacock Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 7. 17.		First Draft.
 */
package com.athena.meerkat.agent.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils.StringStreamConsumer;
import org.codehaus.plexus.util.cli.Commandline;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.athena.meerkat.agent.util.MacAddressUtil;
import com.athena.meerkat.agent.util.PropertyUtil;
import com.athena.meerkat.agent.util.SigarUtil;
import com.athena.meerkat.common.constant.MeerkatConstant;
import com.athena.meerkat.common.netty.MeerkatDatagram;
import com.athena.meerkat.common.netty.message.AgentInitialInfoMessage;
import com.athena.meerkat.common.netty.message.ConfigInfo;
import com.athena.meerkat.common.netty.message.MessageType;
import com.athena.meerkat.common.netty.message.OSPackageInfoMessage;
import com.athena.meerkat.common.netty.message.PackageInfo;
import com.athena.meerkat.common.netty.message.ProvisioningCommandMessage;
import com.athena.meerkat.common.netty.message.ProvisioningResponseMessage;
import com.athena.meerkat.common.netty.message.SoftwareInfo;
import com.athena.meerkat.common.netty.message.SoftwareInfoMessage;
import com.athena.meerkat.common.provider.AppContext;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("meerkatClientHandler")
@Sharable
public class MeerkatClientHandler extends SimpleChannelInboundHandler<Object>
		implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MeerkatClientHandler.class);

	private boolean connected = false;
	private static boolean gPackageCollected = false;
	private static boolean gSoftwareCollected = false;
	private String machineId = null;

	private MeerkatClient client = null;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.debug("channelActive() has invoked. RemoteAddress=[{}]", ctx
				.channel().remoteAddress());

		connected = true;

		String ipAddr = ctx.channel().remoteAddress().toString();
		ipAddr = ipAddr.substring(1, ipAddr.indexOf(":"));

		// register a new channel
		if (ChannelManagement.registerChannel(ipAddr, ctx.channel()) == 1) {
			// 제일 처음 등록되는 채널일 경우에만 System 정보를 전달한다.
			ctx.writeAndFlush(getAgentInitialInfo());
		} else {
			while (true) {
				if (this.machineId == null) {
					Thread.sleep(100);
				} else {
					break;
				}
			}

			AgentInitialInfoMessage message = new AgentInitialInfoMessage();
			message.setMachineId(this.machineId);

			ctx.writeAndFlush(new MeerkatDatagram<AgentInitialInfoMessage>(
					message));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		LOGGER.debug("channelRead0() has invoked.");

		LOGGER.debug("[Client] Object => " + msg.getClass().getName());
		LOGGER.debug("[Client] Contents => " + msg.toString());

		if (msg instanceof MeerkatDatagram) {
			MessageType messageType = ((MeerkatDatagram<?>) msg)
					.getMessageType();

			if (messageType.equals(MessageType.COMMAND)) {
				ProvisioningResponseMessage response = new ProvisioningResponseMessage();
				response.setAgentId(((MeerkatDatagram<ProvisioningCommandMessage>) msg)
						.getMessage().getAgentId());
				response.setBlocking(((MeerkatDatagram<ProvisioningCommandMessage>) msg)
						.getMessage().isBlocking());

				((MeerkatDatagram<ProvisioningCommandMessage>) msg)
						.getMessage().executeCommands(response);

				ctx.writeAndFlush(new MeerkatDatagram<ProvisioningResponseMessage>(
						response));
			} else if (messageType.equals(MessageType.PACKAGE_INFO)) {
				ctx.writeAndFlush("Start OS Package collecting...");

				String packageFile = null;

				try {
					packageFile = PropertyUtil
							.getProperty(MeerkatConstant.PACKAGE_FILE_KEY);
				} catch (Exception e) {
					LOGGER.error("Meerkat Error", e);
				} finally {
					if (StringUtils.isEmpty(packageFile)) {
						packageFile = "/meerkat/agent/config/package.log";
					}
				}

				new PackageGatherThread(ctx, packageFile).start();
			} else if (messageType.equals(MessageType.INITIAL_INFO)) {
				machineId = ((MeerkatDatagram<AgentInitialInfoMessage>) msg)
						.getMessage().getAgentId();
				String packageCollected = ((MeerkatDatagram<AgentInitialInfoMessage>) msg)
						.getMessage().getPackageCollected();
				String softwareInstalled = ((MeerkatDatagram<AgentInitialInfoMessage>) msg)
						.getMessage().getSoftwareInstalled();

				String agentFile = null;
				String agentId = null;

				try {
					agentFile = PropertyUtil
							.getProperty(MeerkatConstant.AGENT_ID_FILE_KEY);
				} catch (Exception e) {
					LOGGER.error("Meerkat Error", e);
				} finally {
					if (StringUtils.isEmpty(agentFile)) {
						agentFile = "/meerkat/agent/.agent";
					}
				}

				if (StringUtils.isEmpty(agentFile)) {
					agentFile = "/meerkat/agent/.agent";
				}

				File file = new File(agentFile);
				boolean isNew = false;

				try {
					agentId = IOUtils.toString(file.toURI());

					if (!agentId.equals(machineId)) {
						isNew = true;
					}
				} catch (IOException e) {
					LOGGER.error(agentFile
							+ " file cannot read or saved invalid agent ID.", e);
				}

				if (isNew) {
					LOGGER.info("New Agent-ID({}) will be saved.", machineId);

					try {
						file.setWritable(true);
						OutputStreamWriter output = new OutputStreamWriter(
								new FileOutputStream(file));
						output.write(machineId);
						file.setReadOnly();
						IOUtils.closeQuietly(output);
					} catch (UnsupportedEncodingException e) {
						LOGGER.error(
								"UnsupportedEncodingException has occurred : ",
								e);
					} catch (FileNotFoundException e) {
						LOGGER.error("FileNotFoundException has occurred : ", e);
					} catch (IOException e) {
						LOGGER.error("IOException has occurred : ", e);
					}
				}

				// 패키지 정보 수집을 수행하지 않았고 패키지 정보 수집 이력이 없는 경우 수행
				if (packageCollected != null && packageCollected.equals("N")) {
					if (!gPackageCollected) {
						gPackageCollected = true;
						String packageFile = null;

						try {
							packageFile = PropertyUtil
									.getProperty(MeerkatConstant.PACKAGE_FILE_KEY);
						} catch (Exception e) {
							LOGGER.error("Meerkat Error", e);
						} finally {
							if (StringUtils.isEmpty(packageFile)) {
								packageFile = "/meerkat/agent/config/package.log";
							}
						}

						if (StringUtils.isEmpty(packageFile)) {
							packageFile = "/meerkat/agent/config/package.log";
						}

						file = new File(packageFile);

						if (!file.exists()) {
							new PackageGatherThread(ctx, packageFile).start();
						}
					}
				}

				if (softwareInstalled != null && softwareInstalled.equals("N")) {
					if (!gSoftwareCollected) {
						gSoftwareCollected = true;
						new SoftwareGatherThread(ctx).start();
					}
				}

				Scheduler scheduler = (Scheduler) AppContext
						.getBean("quartzJobScheduler");

				if (!scheduler.isStarted()) {
					scheduler.start();
				}
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		LOGGER.error("Unexpected exception from downstream.", cause);
		ctx.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty
	 * .channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.debug("channelInactive() has invoked. RemoteAddress=[{}]", ctx
				.channel().remoteAddress());

		// deregister a closed channel
		ChannelManagement.deregisterChannel(ctx.channel());

		if (ChannelManagement.getChannelSize() == 0) {
			connected = false;
		}

		// 서버와의 연결이 종료되면 5초 단위로 재접속을 수행한다.
		if (client == null) {
			client = AppContext.getBean(MeerkatClient.class);
		}

		final EventLoop eventLoop = ctx.channel().eventLoop();
		final String ipAddr = ctx.channel().remoteAddress().toString();
		eventLoop.schedule(new Runnable() {
			public void run() {
				LOGGER.debug("Attempt to reconnect within 5 seconds.");
				client.createBootstrap(new Bootstrap(), eventLoop,
						ipAddr.substring(1, ipAddr.indexOf(":")));
			}
		}, 5L, TimeUnit.SECONDS);

		super.channelInactive(ctx);
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * @return the channel
	 */
	private Channel getAnyChannel() {
		List<String> keyList = ChannelManagement.getKeys();
		int idx = (int) (Math.random() * 10) % keyList.size();
		return getChannel(keyList.get(idx));
	}

	/**
	 * @return the channel
	 */
	private Channel getChannel(String ipAddr) {
		return ChannelManagement.getChannel(ipAddr);
	}

	/**
	 * @return the channel
	 */
	public void close() {
		ChannelManagement.deregisterAllChannel();
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param datagram
	 */
	public void sendMessage(MeerkatDatagram<?> datagram) {
		// Agent가 Controller로 메시지를 전송하는 경우는 Monitoring 정보를 수집하여 전송하는 경우 밖에 없으며,
		// Agent는 하나만 동작하므로 어떤 체널로 전송이 되는 관계 없음.
		getAnyChannel().writeAndFlush(datagram);
	}// end of sendMessage()

	/**
	 * <pre>
	 * Agent의 시스템 정보를 조회한다.
	 * </pre>
	 * 
	 * @return
	 * @throws Exception
	 */
	private MeerkatDatagram<AgentInitialInfoMessage> getAgentInitialInfo()
			throws Exception {
		String agentId = IOUtils.toString(new File(PropertyUtil
				.getProperty(MeerkatConstant.AGENT_ID_FILE_KEY)).toURI());

		AgentInitialInfoMessage message = new AgentInitialInfoMessage();
		message.setMacAddrMap(MacAddressUtil.getMacAddressMap());
		message.setAgentId(agentId);
		message.setOsName(System.getProperty("os.name"));
		message.setOsArch(System.getProperty("os.arch"));
		message.setOsVersion(System.getProperty("os.version"));
		message.setCpuClock(SigarUtil.getCpuClock());
		message.setCpuNum(SigarUtil.getCpuNum());
		message.setCpuModel(SigarUtil.getCpuModel());
		message.setCpuVendor(SigarUtil.getCpuVendor());
		message.setMemSize(SigarUtil.getMemSize());
		message.setHostName(SigarUtil.getNetInfo().getHostName());
		// message.setHostName(InetAddress.getLocalHost().getHostName());

		try {
			//
			message.setIpAddr(InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
			// ignore
			LOGGER.info("[{}] has occurred but ignore this exception.",
					e.getMessage());
		}

		return new MeerkatDatagram<AgentInitialInfoMessage>(message);
	}// end of getAgentInitialInfo()

	/**
	 * <pre>
	 * Channel 관리를 위한 클래스
	 * </pre>
	 * 
	 * @author Sang-cheon Park
	 * @version 1.0
	 */
	static class ChannelManagement {

		static Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();

		/**
		 * <pre>
		 * 신규 채널을 등록한다.
		 * </pre>
		 * 
		 * @param ipAddr
		 * @param channel
		 */
		synchronized static int registerChannel(String ipAddr, Channel channel) { // NOPMD
			LOGGER.debug(
					"ipAddr({}) and channel({}) will be added to channelMap.",
					ipAddr, channel);
			channelMap.put(ipAddr, channel);

			return channelMap.size();
		}// end of registerChannel()

		/**
		 * <pre>
		 * ipAddr에 해당하는 채널을 map에서 제거한다.
		 * </pre>
		 * 
		 * @param ipAddr
		 */
		synchronized static void deregisterChannel(String ipAddr) { // NOPMD
			LOGGER.debug("ipAddr({}) will be removed from channelMap.", ipAddr);
			channelMap.remove(ipAddr);
		}// end of deregisterChannel()

		/**
		 * <pre>
		 * 연결 종료된 채널을 map에서 제거한다.
		 * </pre>
		 * 
		 * @param channel
		 */
		synchronized static void deregisterChannel(Channel channel) { // NOPMD
			Iterator<Entry<String, Channel>> iter = channelMap.entrySet()
					.iterator();

			Entry<String, Channel> entry = null;
			while (iter.hasNext()) {
				entry = iter.next();

				if (entry.getValue() != null && entry.getValue() == channel) {
					deregisterChannel(entry.getKey());
					break;
				}
			}
		}// end of deregisterChannel()

		/**
		 * <pre>
		 * 연결된 모든 채널을 제거한다.
		 * </pre>
		 */
		synchronized static void deregisterAllChannel() { // NOPMD
			Iterator<Entry<String, Channel>> iter = channelMap.entrySet()
					.iterator();

			while (iter.hasNext()) {
				deregisterChannel(iter.next().getKey());
			}
		}// end of deregisterAllChannel()

		/**
		 * <pre>
		 * ipAddr에 해당하는 채널 정보를 가져온다.
		 * </pre>
		 * 
		 * @param ipAddr
		 * @return
		 */
		static Channel getChannel(String ipAddr) {
			return channelMap.get(ipAddr);
		}// end of getChannel()

		/**
		 * <pre>
		 * 연결된 채널 수를 조회한다.
		 * </pre>
		 * 
		 * @return
		 */
		static synchronized int getChannelSize() { // NOPMD
			return channelMap.size();
		}// end of getChannelSize()

		/**
		 * <pre>
		 * 연결된 채널 IP 목록을 조회한다.
		 * </pre>
		 * 
		 * @return
		 */
		static List<String> getKeys() {
			return new ArrayList<String>(channelMap.keySet());
		}// end of getKeys()
	}

	// end of ChannelManagement.java

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		LOGGER.debug("!!!!!!!!!!Meerkat client handler inited !!!!!!!!!!!!!\n\n\n");
		System.out
				.println("!!!!!!!!!!Meerkat client handler inited !!!!!!!!!!!!!\n\n\n");
	}
}

// end of MeerkatClientHandler.java

/**
 * <pre>
 * Agent의 패키지 정보를 수집하는 스레드
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
class PackageGatherThread extends Thread {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PackageGatherThread.class);

	private final ChannelHandlerContext ctx;
	private final String packageFile;

	public PackageGatherThread(ChannelHandlerContext ctx, String packageFile) {
		this.ctx = ctx;
		this.packageFile = packageFile;
	}

	@Override
	public void run() {
		try {
			// rpm -qa 로 전체 패키지 조회
			Commandline commandLine = new Commandline();
			commandLine.setExecutable("rpm");
			commandLine.createArg().setValue("-qa");

			StringStreamConsumer consumer = new CommandLineUtils.StringStreamConsumer();

			LOGGER.debug("Start Package(rpm) info gathering...");
			LOGGER.debug("~]$ {}\n", commandLine.toString());

			int returnCode = CommandLineUtils.executeCommandLine(commandLine,
					consumer, consumer, Integer.MAX_VALUE);

			if (returnCode == 0) {
				String[] rpms = consumer.getOutput().split("\n");

				OSPackageInfoMessage msg = new OSPackageInfoMessage(
						MessageType.PACKAGE_INFO);
				msg.setAgentId(IOUtils.toString(new File(PropertyUtil
						.getProperty(MeerkatConstant.AGENT_ID_FILE_KEY))
						.toURI()));
				PackageInfo packageInfo = null;
				for (String rpm : rpms) {
					try {
						// rpm -q --qf
						// "%{NAME}\n%{ARCH}\n%{SIZE}\n%{VERSION}\n%{RELEASE}\n%{INSTALLTIME:date}\n%{SUMMARY}\n%{DESCRIPTION}"
						// ${PACKAGE_NAME} 으로 각 패키지 세부사항 조회
						packageInfo = getPackageInfo(rpm);

						if (packageInfo != null) {
							msg.addPackageInfo(packageInfo);
						}
					} catch (Exception e) {
						LOGGER.error("Unhandled Exception has occurred. ", e);
					}
				}

				StringBuilder sb = new StringBuilder("CurrenDate : ")
						.append(new Date()).append(", RPM Count : ")
						.append(rpms.length).append("\r\n");

				FileWriter fw = new FileWriter(packageFile, true);
				fw.write(sb.toString());
				fw.flush();

				IOUtils.closeQuietly(fw);

				if (msg.getPackageInfoList().size() > 0) {
					ctx.writeAndFlush(new MeerkatDatagram<OSPackageInfoMessage>(
							msg));
				}

				LOGGER.debug("End Package(rpm) info gathering...");
			} else {
				// when command execute failed.
				// especially command not found.
				LOGGER.debug("End Package(rpm) info gathering with error(command execute failed)...");
			}
		} catch (Exception e) {
			LOGGER.error("Unhandled Exception has occurred. ", e);
		}
	}

	private PackageInfo getPackageInfo(String rpm) throws CommandLineException {
		Commandline commandLine = new Commandline();
		commandLine.setExecutable("rpm");
		commandLine.createArg().setValue("-q");
		commandLine.createArg().setValue("--qf");
		commandLine
				.createArg()
				.setValue(
						"%{NAME}\n%{ARCH}\n%{SIZE}\n%{VERSION}\n%{RELEASE}\n%{INSTALLTIME}\n%{SUMMARY}\n%{DESCRIPTION}");
		commandLine.createArg().setValue(rpm);

		// LOGGER.debug("~]$ {}\n", commandLine.toString());

		StringStreamConsumer consumer = new CommandLineUtils.StringStreamConsumer();
		int returnCode = CommandLineUtils.executeCommandLine(commandLine,
				consumer, consumer, Integer.MAX_VALUE);

		if (returnCode == 0) {
			PackageInfo packageInfo = new PackageInfo();

			String result = consumer.getOutput();
			int start = 0, end = 0;

			start = 0;
			end = result.indexOf("\n", start);
			packageInfo.setName(result.substring(start, end));

			start = end + 1;
			end = result.indexOf("\n", start);
			packageInfo.setArch(result.substring(start, end));

			start = end + 1;
			end = result.indexOf("\n", start);
			packageInfo.setSize(result.substring(start, end));

			start = end + 1;
			end = result.indexOf("\n", start);
			packageInfo.setVersion(result.substring(start, end));

			start = end + 1;
			end = result.indexOf("\n", start);
			packageInfo.setRelease(result.substring(start, end));

			start = end + 1;
			end = result.indexOf("\n", start);
			packageInfo.setInstallDate(new Date(Long.parseLong(result
					.substring(start, end)) * 1000));

			start = end + 1;
			end = result.indexOf("\n", start);
			packageInfo.setSummary(result.substring(start, end));

			start = end + 1;
			packageInfo.setDescription(result.substring(start));

			return packageInfo;
		} else {
			return null;
		}
	}
}

// end of PackageGatherThread.java

/**
 * <pre>
 * Agent의 소프트웨어 정보를 수집하는 스레드
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
class SoftwareGatherThread extends Thread {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SoftwareGatherThread.class);

	private final ChannelHandlerContext ctx;

	public SoftwareGatherThread(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void run() {
		try {
			StringStreamConsumer consumer = null;
			Commandline commandLine = null;
			int returnCode = 0;
			Properties properties = null;

			SoftwareInfoMessage msg = new SoftwareInfoMessage(
					MessageType.SOFTWARE_INFO);
			msg.setAgentId(IOUtils.toString(new File(PropertyUtil
					.getProperty(MeerkatConstant.AGENT_ID_FILE_KEY)).toURI()));
			SoftwareInfo softwareInfo = null;

			// =======================
			// 1. JBOSS EWS(HTTPD)
			// =======================
			try {
				commandLine = new Commandline();
				commandLine.setExecutable("sh");
				commandLine.createArg().setLine("chkhttpd.sh");

				LOGGER.debug("Start Software(httpd) info gathering...");
				LOGGER.debug("~]$ {}\n", commandLine.toString());

				consumer = new CommandLineUtils.StringStreamConsumer();

				returnCode = CommandLineUtils.executeCommandLine(commandLine,
						consumer, consumer, Integer.MAX_VALUE);

				if (returnCode == 0) {
					properties = new Properties();
					properties.load(new StringReader(consumer.getOutput()));

					int count = Integer.parseInt((String) properties
							.get("COUNT"));
					String apacheHome = (String) properties.get("APACHE_HOME");
					String serverHome = (String) properties.get("SERVER_HOME");
					String startCmd = (String) properties.get("START_CMD");
					String stopCmd = (String) properties.get("STOP_CMD");

					for (int i = 1; i <= count; i++) {
						softwareInfo = new SoftwareInfo();
						softwareInfo.setName("httpd");

						if (apacheHome != null
								&& apacheHome.split(",")[i].indexOf("2.1") >= 0) {
							softwareInfo.setVersion("2.2.26");
						} else {
							softwareInfo.setVersion("2.2.22");
						}

						if (apacheHome != null)
							softwareInfo.getInstallLocations().add(
									apacheHome.split(",")[i]);
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/bin");
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/conf");
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/log");
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/run");
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/www");
						softwareInfo.setStartWorkingDir("");
						softwareInfo.setStartCommand(startCmd.split(",")[i]
								.substring(0,
										startCmd.split(",")[i].indexOf(" ")));
						softwareInfo
								.setStartArgs(startCmd.split(",")[i]
										.substring(startCmd.split(",")[i]
												.indexOf(" ") + 1));
						softwareInfo.setStopWorkingDir("");
						softwareInfo.setStopCommand(stopCmd.split(",")[i]
								.substring(0,
										stopCmd.split(",")[i].indexOf(" ")));
						softwareInfo
								.setStopArgs(stopCmd.split(",")[i]
										.substring(stopCmd.split(",")[i]
												.indexOf(" ") + 1));

						String configFile = null;
						ConfigInfo configInfo = null;

						String[] configKeys = new String[] { "HTTPD_CONF",
								"HTTPD_INFO_CONF", "SSL_CONF", "URIWORKERMAP",
								"WORKERS" };

						for (String configKey : configKeys) {
							configFile = (String) properties.get(configKey);
							if (configFile != null) {
								// ,,,,, 일 경우 split size가 0으로 반환되는 것을 방지하기 위해
								// 공백을 넣어준다.
								configFile = configFile + " ";
								if (configFile.split(",").length > 0
										&& configFile.split(",")[i]
												.indexOf("/") >= 0) {
									commandLine = new Commandline();
									commandLine.setExecutable("cat");
									commandLine.createArg().setLine(
											configFile.split(",")[i]);

									consumer = new CommandLineUtils.StringStreamConsumer();

									returnCode = CommandLineUtils
											.executeCommandLine(commandLine,
													consumer, consumer,
													Integer.MAX_VALUE);

									if (returnCode == 0) {
										configInfo = new ConfigInfo();
										configInfo
												.setConfigFileLocation(configFile
														.split(",")[i]
														.substring(
																0,
																configFile
																		.split(",")[i]
																		.lastIndexOf("/")));
										configInfo.setConfigFileName(configFile
												.split(",")[i].substring(
												configFile.split(",")[i]
														.lastIndexOf("/") + 1,
												configFile.split(",")[i]
														.length()).trim());
										configInfo
												.setConfigFileContents(consumer
														.getOutput()
														.substring(
																0,
																consumer.getOutput()
																		.length() - 1));
										softwareInfo.getConfigInfoList().add(
												configInfo);
									}
								}
							}
						}

						msg.getSoftwareInfoList().add(softwareInfo);
					}

					LOGGER.debug("End Software(httpd) info gathering...");
				}
			} catch (Exception e1) {
				// ignore after logging
				LOGGER.error("Unhandled Exception has occurred. ", e1);
			}

			// =======================
			// 2. JBOSS EWS(TOMCAT)
			// =======================
			try {
				commandLine = new Commandline();
				commandLine.setExecutable("sh");
				commandLine.createArg().setLine("chkews.sh");

				LOGGER.debug("Start Software(tomcat) info gathering...");
				LOGGER.debug("~]$ {}\n", commandLine.toString());

				consumer = new CommandLineUtils.StringStreamConsumer();

				returnCode = CommandLineUtils.executeCommandLine(commandLine,
						consumer, consumer, Integer.MAX_VALUE);

				if (returnCode == 0) {
					properties = new Properties();
					properties.load(new StringReader(consumer.getOutput()));

					int count = Integer.parseInt((String) properties
							.get("COUNT"));
					String version = (String) properties.get("VERSION");
					String catalinaHome = (String) properties
							.get("CATALINA_HOME");
					String serverHome = (String) properties.get("SERVER_HOME");
					String startCmd = (String) properties.get("START_CMD");
					String stopCmd = (String) properties.get("STOP_CMD");

					for (int i = 1; i <= count; i++) {
						softwareInfo = new SoftwareInfo();
						softwareInfo.setName("tomcat");

						if (version != null
								&& version.split(",")[i].indexOf("7") >= 0) {
							softwareInfo.setVersion("7.0.54");
						} else {
							softwareInfo.setVersion("6.0.41");
						}

						softwareInfo.getInstallLocations().add(
								catalinaHome.split(",")[i].substring(0,
										catalinaHome.split(",")[i]
												.lastIndexOf("/")));
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/apps");
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/bin");
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/Servers");
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/svrlogs");
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/wily");
						softwareInfo.setStartWorkingDir("");
						softwareInfo.setStartCommand(startCmd.split(",")[i]
								.substring(0,
										startCmd.split(",")[i].indexOf(" ")));
						softwareInfo
								.setStartArgs(startCmd.split(",")[i]
										.substring(startCmd.split(",")[i]
												.indexOf(" ") + 1));
						softwareInfo.setStopWorkingDir("");
						softwareInfo.setStopCommand(stopCmd.split(",")[i]
								.substring(0,
										stopCmd.split(",")[i].indexOf(" ")));
						softwareInfo
								.setStopArgs(stopCmd.split(",")[i]
										.substring(stopCmd.split(",")[i]
												.indexOf(" ") + 1));

						String configFile = null;
						ConfigInfo configInfo = null;

						String[] configKeys = new String[] { "ENV_SH",
								"SERVER_XML", "CONTEXT_XML" };

						for (String configKey : configKeys) {
							configFile = (String) properties.get(configKey);
							if (configFile != null) {
								// ,,,,, 일 경우 split size가 0으로 반환되는 것을 방지하기 위해
								// 공백을 넣어준다.
								configFile = configFile + " ";
								if (configFile.split(",").length > 0
										&& configFile.split(",")[i]
												.indexOf("/") >= 0) {
									commandLine = new Commandline();
									commandLine.setExecutable("cat");
									commandLine.createArg().setLine(
											configFile.split(",")[i]);

									consumer = new CommandLineUtils.StringStreamConsumer();

									returnCode = CommandLineUtils
											.executeCommandLine(commandLine,
													consumer, consumer,
													Integer.MAX_VALUE);

									if (returnCode == 0) {
										configInfo = new ConfigInfo();
										configInfo
												.setConfigFileLocation(configFile
														.split(",")[i]
														.substring(
																0,
																configFile
																		.split(",")[i]
																		.lastIndexOf("/")));
										configInfo.setConfigFileName(configFile
												.split(",")[i].substring(
												configFile.split(",")[i]
														.lastIndexOf("/") + 1,
												configFile.split(",")[i]
														.length()).trim());
										configInfo
												.setConfigFileContents(consumer
														.getOutput()
														.substring(
																0,
																consumer.getOutput()
																		.length() - 1));
										softwareInfo.getConfigInfoList().add(
												configInfo);
									}
								}
							}
						}

						msg.getSoftwareInfoList().add(softwareInfo);
					}

					LOGGER.debug("End Software(tomcat) info gathering...");
				}
			} catch (Exception e1) {
				// ignore after logging
				LOGGER.error("Unhandled Exception has occurred. ", e1);
			}

			// =======================
			// 3. JBOSS EAP
			// =======================
			try {
				commandLine = new Commandline();
				commandLine.setExecutable("sh");
				commandLine.createArg().setLine("chkeap.sh");

				LOGGER.debug("Start Software(eap) info gathering...");
				LOGGER.debug("~]$ {}\n", commandLine.toString());

				consumer = new CommandLineUtils.StringStreamConsumer();

				returnCode = CommandLineUtils.executeCommandLine(commandLine,
						consumer, consumer, Integer.MAX_VALUE);

				if (returnCode == 0) {
					properties = new Properties();
					properties.load(new StringReader(consumer.getOutput()));

					int count = Integer.parseInt((String) properties
							.get("COUNT"));
					String version = (String) properties.get("VERSION");
					String jbossHome = (String) properties.get("JBOSS_HOME");
					String serverHome = (String) properties.get("SERVER_HOME");
					String startCmd = (String) properties.get("START_CMD");
					String stopCmd = (String) properties.get("STOP_CMD");

					for (int i = 1; i <= count; i++) {
						softwareInfo = new SoftwareInfo();
						softwareInfo.setName("eap");

						if (version != null
								&& version.split(",")[i].indexOf("5.2") >= 0) {
							softwareInfo.setVersion("5.2.0");
						} else {
							softwareInfo.setVersion("5.1.2");
						}

						softwareInfo.getInstallLocations().add(
								jbossHome.split(",")[i]);
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/apps");
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/Servers");
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/svrlogs");
						softwareInfo.getInstallLocations().add(
								serverHome.split(",")[i] + "/wily");
						softwareInfo.setStartWorkingDir("");
						softwareInfo.setStartCommand(startCmd.split(",")[i]
								.substring(0,
										startCmd.split(",")[i].indexOf(" ")));
						softwareInfo
								.setStartArgs(startCmd.split(",")[i]
										.substring(startCmd.split(",")[i]
												.indexOf(" ") + 1));
						softwareInfo.setStopWorkingDir("");
						softwareInfo.setStopCommand(stopCmd.split(",")[i]
								.substring(0,
										stopCmd.split(",")[i].indexOf(" ")));
						softwareInfo
								.setStopArgs(stopCmd.split(",")[i]
										.substring(stopCmd.split(",")[i]
												.indexOf(" ") + 1));

						String configFile = null;
						ConfigInfo configInfo = null;

						String[] configKeys = new String[] { "ENV_SH",
								"DS_XML", "LOGIN_CONFIG_XML" };

						for (String configKey : configKeys) {
							configFile = (String) properties.get(configKey);
							if (configFile != null) {
								// ,,,,, 일 경우 split size가 0으로 반환되는 것을 방지하기 위해
								// 공백을 넣어준다.
								configFile = configFile + " ";
								if (configFile.split(",").length > 0
										&& configFile.split(",")[i]
												.indexOf("/") >= 0) {
									commandLine = new Commandline();
									commandLine.setExecutable("cat");
									commandLine.createArg().setLine(
											configFile.split(",")[i]);

									consumer = new CommandLineUtils.StringStreamConsumer();

									returnCode = CommandLineUtils
											.executeCommandLine(commandLine,
													consumer, consumer,
													Integer.MAX_VALUE);

									if (returnCode == 0) {
										configInfo = new ConfigInfo();
										configInfo
												.setConfigFileLocation(configFile
														.split(",")[i]
														.substring(
																0,
																configFile
																		.split(",")[i]
																		.lastIndexOf("/")));
										configInfo.setConfigFileName(configFile
												.split(",")[i].substring(
												configFile.split(",")[i]
														.lastIndexOf("/") + 1,
												configFile.split(",")[i]
														.length()).trim());
										configInfo
												.setConfigFileContents(consumer
														.getOutput()
														.substring(
																0,
																consumer.getOutput()
																		.length() - 1));
										softwareInfo.getConfigInfoList().add(
												configInfo);
									}
								}
							}
						}

						msg.getSoftwareInfoList().add(softwareInfo);
					}

					LOGGER.debug("End Software(eap) info gathering...");
				}
			} catch (Exception e1) {
				// ignore after logging
				LOGGER.error("Unhandled Exception has occurred. ", e1);
			}

			if (msg.getSoftwareInfoList().size() > 0) {
				ctx.writeAndFlush(new MeerkatDatagram<SoftwareInfoMessage>(msg));
			}
		} catch (Exception e) {
			LOGGER.error("Unhandled Exception has occurred. ", e);
		}
	}
}
// end of SoftwareGatherThread.java