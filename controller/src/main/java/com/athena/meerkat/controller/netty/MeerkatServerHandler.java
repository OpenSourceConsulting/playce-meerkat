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
 * Sang-cheon Park	2013. 7. 17.		First Draft.
 */
package com.athena.meerkat.controller.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.athena.meerkat.common.core.action.ShellAction;
import com.athena.meerkat.common.core.command.Command;
import com.athena.meerkat.common.netty.MeerkatDatagram;
import com.athena.meerkat.common.netty.message.AbstractMessage;
import com.athena.meerkat.common.netty.message.AgentSystemStatusMessage;
import com.athena.meerkat.common.netty.message.MessageType;
import com.athena.meerkat.common.netty.message.ProvisioningResponseMessage;
import com.redhat.rhevm.api.model.Cluster;
import com.redhat.rhevm.api.model.IP;
import com.redhat.rhevm.api.model.VM;
import com.redhat.rhevm.api.model.VMs;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("peacockServerHandler")
@Sharable
public class MeerkatServerHandler extends SimpleChannelInboundHandler<Object> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MeerkatServerHandler.class);

	@SuppressWarnings("unchecked")
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		LOGGER.debug("channelRead0() has invoked.");
		LOGGER.debug("[Server] IP Address => "
				+ ctx.channel().remoteAddress().toString());
		LOGGER.debug("[Server] Object => " + msg.getClass().getName());
		// LOGGER.debug("[Server] Contents => " + msg.toString());

		if ("bye".equals(msg.toString())) {
			// Response and exit.
			ChannelFuture future = ctx.write("This channel will be closed.");
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			if (msg instanceof MeerkatDatagram) {
				MessageType messageType = ((MeerkatDatagram<?>) msg)
						.getMessageType();

				switch (messageType) {
				case COMMAND:
					break;
				case RESPONSE:
					ProvisioningResponseMessage responseMsg = ((MeerkatDatagram<ProvisioningResponseMessage>) msg)
							.getMessage();

					if (responseMsg.isBlocking()) {
						CallbackManagement.poll().handle(responseMsg);
					}
					break;
//				case SYSTEM_STATUS:
//					AgentSystemStatusMessage statusMsg = ((PeacockDatagram<AgentSystemStatusMessage>) msg)
//							.getMessage();
//
//					// ThreadLocal cannot use.
//					// List<MonFactorDto> monFactorList = (List<MonFactorDto>)
//					// ThreadLocalUtil.get(PeacockConstant.MON_FACTOR_LIST);
//					List<MonFactorDto> monFactorList = AppContext.getBean(
//							MonFactorHandler.class).getMonFactorList();
//
//					List<MonDataDto> monDataList = new ArrayList<MonDataDto>();
//					MonDataDto monData = null;
//
//					for (MonFactorDto monFactor : monFactorList) {
//						monData = new MonDataDto();
//
//						monData.setMachineId(statusMsg.getAgentId());
//						monData.setMonFactorId(monFactor.getMonFactorId());
//						monData.setMonDataValue(getMonDataValue(monFactor,
//								statusMsg));
//						monData.setRegUserId(1);
//						monData.setUpdUserId(1);
//
//						monDataList.add(monData);
//					}
//
//					if (this.monitorService == null) {
//						monitorService = AppContext
//								.getBean(MonitorService.class);
//					}
//
//					monitorService.insertMonDataList(monDataList);
//
//					break;
//				case INITIAL_INFO:
//					AgentInitialInfoMessage infoMsg = ((PeacockDatagram<AgentInitialInfoMessage>) msg)
//							.getMessage();
//
//					if (infoMsg.getMachineId() != null) {
//						// register a new channel
//						ChannelManagement.registerChannel(
//								infoMsg.getMachineId(), ctx.channel());
//						break;
//					}
//
//					String ipAddr = ctx.channel().remoteAddress().toString();
//					ipAddr = ipAddr.substring(1, ipAddr.indexOf(":"));
//
//					// ipAddr에 해당하는 rhev_id를 RHEV Manager로부터 조회한다.
//					String machineId = infoMsg.getAgentId();
//					Integer hypervisorId = null;
//					String displayName = infoMsg.getHostName();
//					String clusterName = null;
//					String isPrd = "N";
//					String isVm = "N";
//					String description = null;
//					boolean isMatch = false;
//					try {
//						List<RHEVMRestTemplate> templates = RHEVMRestTemplateManager
//								.getAllTemplates();
//
//						if (templates == null || templates.size() == 0) {
//							List<HypervisorDto> hypervisorList = AppContext
//									.getBean(HypervisorService.class)
//									.getHypervisorList();
//							RHEVMRestTemplateManager
//									.resetRHEVMRestTemplate(hypervisorList);
//							templates = RHEVMRestTemplateManager
//									.getAllTemplates();
//						}
//
//						LOGGER.debug(
//								"[PeacockServerHandler] templates.size() : {}",
//								templates.size());
//
//						for (RHEVMRestTemplate restTemplate : templates) {
//							VMs vms = restTemplate.submit("/api/vms?search="
//									+ ipAddr, HttpMethod.GET, VMs.class);
//
//							if (vms.getVMs().size() > 0) {
//								isVm = "Y";
//
//								List<VM> vmList = vms.getVMs();
//								List<IP> ipList = null;
//								for (VM vm : vmList) {
//									// ip를 이용한 조회로써 getIps()가 null이 아님.
//									ipList = vm.getGuestInfo().getIps()
//											.getIPs();
//
//									for (IP ip : ipList) {
//										if (ip.getAddress().equals(ipAddr)) {
//											isMatch = true;
//											machineId = vm.getId();
//											hypervisorId = restTemplate
//													.getHypervisorId();
//											displayName = vm.getName();
//											description = vm.getDescription();
//
//											Cluster cluster = restTemplate
//													.submit(vm.getCluster()
//															.getHref(),
//															HttpMethod.GET,
//															Cluster.class);
//											clusterName = cluster.getName();
//											break;
//										}
//									}
//
//									if (isMatch) {
//										break;
//									}
//								}
//							}
//
//							if (isMatch) {
//								break;
//							}
//						}
//					} catch (Exception e) {
//						// ignore
//						LOGGER.error("Unhandled Exception has occurred.", e);
//					}
//
//					// register a new channel
//					ChannelManagement.registerChannel(machineId, ctx.channel());
//
//					// Agent에 RHEV Manager에 등록된 ID로 세팅되도록 AgentID를 전달
//					// 해당 Agent에 Software 설치 이력이 없을 경우 Software 정보 수집을 함꼐 요청
//					AgentInitialInfoMessage returnMsg = new AgentInitialInfoMessage();
//					returnMsg.setAgentId(machineId);
//					PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(
//							returnMsg);
//					ctx.channel().writeAndFlush(datagram);
//
//					if (this.machineService == null) {
//						machineService = AppContext
//								.getBean(MachineService.class);
//					}
//
//					// Instance 정보를 DB에 저장한다.
//					MachineDto machine = new MachineDto();
//					machine.setMachineId(machineId);
//					machine.setHypervisorId(hypervisorId);
//					machine.setDisplayName(displayName);
//					machine.setDescription(description);
//
//					if (StringUtils.isNotEmpty(displayName)) {
//						if (displayName.toLowerCase().startsWith("hhilws")
//								&& !displayName.toLowerCase().startsWith(
//										"hhilwsd")) {
//							isPrd = "Y";
//						}
//					}
//					machine.setIsPrd(isPrd);
//
//					machine.setMachineMacAddr(infoMsg.getMacAddrMap().get(
//							ipAddr));
//					machine.setIsVm(isVm);
//					machine.setCluster(clusterName);
//					machine.setOsName(infoMsg.getOsName());
//					machine.setOsVer(infoMsg.getOsVersion());
//					machine.setOsArch(infoMsg.getOsArch());
//					machine.setCpuClock(Integer.toString(infoMsg.getCpuClock()));
//					machine.setCpuNum(Integer.toString(infoMsg.getCpuNum()));
//					machine.setMemSize(Long.toString(infoMsg.getMemSize()));
//					machine.setIpAddr(ipAddr);
//					machine.setHostName(infoMsg.getHostName());
//					machine.setRegUserId(1);
//					machine.setUpdUserId(1);
//
//					machineService.insertMachine(machine);
//
//					// machine_additional_info_tbl에 hostname 및 고정 IP 등 변경해야 할
//					// 내용이 있는지 조회한다.(현재 연결된 IP와 applyYn 정보를 이용)
//					machine = machineService.getAdditionalInfo(machineId);
//
//					boolean hostnameChanged = false;
//					// 변경해야 할 hostname 정보가 있으면 chhost.sh를 실행하여 변경한다.
//					if (machine != null
//							&& StringUtils.isNotEmpty(machine.getHostName())
//							&& !machine.getHostName().equals(
//									infoMsg.getHostName())) {
//						try {
//							// /etc/hosts 파일에 저장될 ipAddress가 현재 Machine의 IP인지 수정
//							// 대상 IP인지 확인한다.
//							String ipAddress = null;
//
//							if (StringUtils.isNotEmpty(machine.getIpAddress())) {
//								ipAddress = machine.getIpAddress();
//							} else {
//								ipAddress = ipAddr;
//							}
//
//							ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
//							cmdMsg.setAgentId(machine.getMachineId());
//							// cmdMsg.setBlocking(true);
//
//							int sequence = 0;
//							Command command = new Command("SET_HOSTNAME");
//
//							ShellAction action = new ShellAction(sequence++);
//							action.setCommand("sh");
//							action.addArguments("chhost.sh");
//							action.addArguments(ipAddress);
//							action.addArguments(machine.getHostName());
//							command.addAction(action);
//
//							cmdMsg.addCommand(command);
//
//							datagram = new PeacockDatagram<AbstractMessage>(
//									cmdMsg);
//							ctx.channel().writeAndFlush(datagram);
//
//							hostnameChanged = true;
//						} catch (Exception e) {
//							// HostName 변경이 실패하더라고 고정 IP 세팅을 할 수 있도록 예외를 무시한다.
//							LOGGER.error(
//									"Unhandled exception has occurred while change hostname.",
//									e);
//						}
//					}
//
//					boolean resetIp = false;
//					if (machine != null
//							&& StringUtils.isNotEmpty(machine.getIpAddress())) {
//						if (machine.getApplyYn().equals("N")
//								&& !machine.getIpAddress().equals(ipAddr)) {
//							// 고정 IP 세팅이 필요할 경우 Agent가 재구동 되기 때문에 더 이상의 처리를 하지
//							// 않는다.
//							machine.setIpAddr(ipAddr);
//							machineService.applyStaticIp(machine);
//							resetIp = true;
//						}
//					}
//
//					if (!resetIp) {
//						if (hostnameChanged) {
//							// IP 변경 없이 hostname만 변경된 경우 peacock-agent를 restart
//							// 한다.
//							machineService.agentRestart(machineId);
//						} else {
//							// Package 정보 수집 및 Software 설치 정보 수집 등 필요한 작업을 수행하도록
//							// 전달한다.
//							if (this.softwareService == null) {
//								softwareService = AppContext
//										.getBean(SoftwareService.class);
//							}
//							List<SoftwareDto> softwareList = softwareService
//									.getSoftwareInstallListAll(machineId);
//
//							PackageService packageService = AppContext
//									.getBean(PackageService.class);
//							PackageDto ospackage = new PackageDto();
//							ospackage.setMachineId(machineId);
//							int packageCnt = packageService
//									.getPackageListCnt(ospackage);
//
//							returnMsg = new AgentInitialInfoMessage();
//							returnMsg.setAgentId(machineId);
//							if (softwareList != null && softwareList.size() > 0) {
//								returnMsg.setSoftwareInstalled("Y");
//							} else {
//								returnMsg.setSoftwareInstalled("N");
//							}
//							if (packageCnt > 0) {
//								returnMsg.setPackageCollected("Y");
//							} else {
//								returnMsg.setPackageCollected("N");
//							}
//							datagram = new PeacockDatagram<AbstractMessage>(
//									returnMsg);
//							ctx.channel().writeAndFlush(datagram);
//						}
//					}
//
//					break;
				// case PACKAGE_INFO:
				// OSPackageInfoMessage packageMsg =
				// ((PeacockDatagram<OSPackageInfoMessage>) msg)
				// .getMessage();
				// List<PackageInfo> packageInfoList = packageMsg
				// .getPackageInfoList();
				// PackageInfo packageInfo = null;
				// List<PackageDto> packageList = new ArrayList<PackageDto>();
				// PackageDto ospackage = null;
				// for (int i = 0; i < packageInfoList.size(); i++) {
				// packageInfo = packageInfoList.get(i);
				//
				// ospackage = new PackageDto();
				// ospackage.setPkgId(i + 1);
				// ospackage.setMachineId(packageMsg.getAgentId());
				// ospackage.setName(packageInfo.getName());
				// ospackage.setArch(packageInfo.getArch());
				// ospackage.setSize(packageInfo.getSize());
				// ospackage.setVersion(packageInfo.getVersion());
				// ospackage.setReleaseInfo(packageInfo.getRelease());
				// ospackage.setInstallDate(packageInfo.getInstallDate());
				// ospackage.setSummary(packageInfo.getSummary());
				// ospackage.setDescription(packageInfo.getDescription());
				//
				// packageList.add(ospackage);
				// }
				//
				// if (packageList.size() > 0) {
				// if (packageService == null) {
				// packageService = AppContext
				// .getBean(PackageService.class);
				// }
				//
				// packageService.insertPackageList(packageList);
				// }
				//
				// break;
				// case SOFTWARE_INFO:
				// SoftwareInfoMessage softwareMsg =
				// ((PeacockDatagram<SoftwareInfoMessage>) msg)
				// .getMessage();
				// List<SoftwareInfo> softwareInfoList = softwareMsg
				// .getSoftwareInfoList();
				// SoftwareInfo softwareInfo = null;
				//
				// if (softwareRepoService == null) {
				// softwareRepoService = AppContext
				// .getBean(SoftwareRepoService.class);
				// }
				//
				// SoftwareRepoDto softwareRepo = new SoftwareRepoDto();
				// softwareRepo.setStart(0);
				// softwareRepo.setLimit(100);
				// List<SoftwareRepoDto> softwareRepoList = softwareRepoService
				// .getSoftwareRepoList(softwareRepo);
				//
				// List<ConfigDto> configList = null;
				// List<ConfigInfo> configInfoList = null;
				// SoftwareDto software = null;
				// ConfigDto config = null;
				// int softwareId = 0;
				//
				// StringBuilder stopCmd = null;
				// StringBuilder startCmd = null;
				//
				// for (int i = 0; i < softwareInfoList.size(); i++) {
				// softwareInfo = softwareInfoList.get(i);
				//
				// for (SoftwareRepoDto repo : softwareRepoList) {
				// softwareId = 0;
				//
				// if (repo.getSoftwareName()
				// .toLowerCase()
				// .indexOf(
				// softwareInfo.getName()
				// .toLowerCase()) > 0) {
				// // 일치하지 않는 버전이 포함될 경우를 대비하기 위해 일단 softwareId를
				// // 저장한다.
				// softwareId = repo.getSoftwareId();
				// if (repo.getSoftwareVersion().startsWith(
				// softwareInfo.getVersion())) {
				// softwareId = repo.getSoftwareId();
				// break;
				// }
				// }
				// }
				//
				// // software.setInstallLocation(apacheHome + "," +
				// // serverHome + "/bin," + serverHome + "/conf," +
				// // serverHome + "/log," + serverHome + "/run," +
				// // serverHome + "/www");
				// // software.setInstallLocation(serverHome + "/apps," +
				// // serverHome + "/bin," + serverHome + "/Servers," +
				// // serverHome + "/svrlogs," + serverHome + "/wily," +
				// // serverHome + "/jboss-ews-2.1");
				// // software.setInstallLocation(jbossHome + "," +
				// // serverBase + ", " + serverHome + "/apps," +
				// // serverHome + "/svrlogs," + serverHome + "/wily");
				//
				// stopCmd = new StringBuilder();
				// startCmd = new StringBuilder();
				//
				// stopCmd.append("WORKING_DIR:")
				// .append(softwareInfo.getStopWorkingDir())
				// .append(",").append("CMD:")
				// .append(softwareInfo.getStopCommand())
				// .append(",").append("ARGS:")
				// .append(softwareInfo.getStopArgs());
				//
				// startCmd.append("WORKING_DIR:")
				// .append(softwareInfo.getStartWorkingDir())
				// .append(",").append("CMD:")
				// .append(softwareInfo.getStartCommand())
				// .append(",").append("ARGS:")
				// .append(softwareInfo.getStartArgs());
				//
				// software = new SoftwareDto();
				// software.setMachineId(softwareMsg.getAgentId());
				// software.setSoftwareId(softwareId);
				// software.setInstallLocation(StringUtils.join(
				// softwareInfo.getInstallLocations(), ","));
				// software.setInstallStat("COMPLETED");
				// software.setInstallLog("Software installed manually.");
				// software.setServiceStopCmd(stopCmd.toString());
				// software.setServiceStartCmd(startCmd.toString());
				// software.setDescription("");
				// software.setDeleteYn("N");
				//
				// configInfoList = softwareInfo.getConfigInfoList();
				// configList = new ArrayList<ConfigDto>();
				//
				// for (ConfigInfo configInfo : configInfoList) {
				// config = new ConfigDto();
				// config.setMachineId(softwareMsg.getAgentId());
				// config.setSoftwareId(softwareId);
				// config.setConfigFileLocation(configInfo
				// .getConfigFileLocation());
				// config.setConfigFileName(configInfo
				// .getConfigFileName());
				// config.setConfigFileContents(configInfo
				// .getConfigFileContents());
				// config.setDeleteYn("N");
				//
				// configList.add(config);
				// }
				//
				// if (softwareService == null) {
				// softwareService = AppContext
				// .getBean(SoftwareService.class);
				// }
				//
				// softwareService.insertSoftware(software, configList);
				// }
				//
				// break;

				default:
				}
			}
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		LOGGER.debug("channelReadComplete() has invoked.");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.debug("channelActive() has invoked.");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.debug("channelInactive() has invoked.");

		// deregister a closed channel
		ChannelManagement.deregisterChannel(ctx.channel());
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		LOGGER.debug("handlerAdded() has invoked.");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		LOGGER.error("Unexpected exception from downstream.", cause);

		// ctx will not be closed.
		// if (!(cause instanceof NestedRuntimeException)) {
		// ctx.close();
		// }
	}

//	F

	/**
	 * <pre>
	 * 해당 Agent로 Provisioning 관련 명령을 전달하고 필요 시 응답을 반환한다.
	 * </pre>
	 * 
	 * @param datagram
	 * @return
	 * @throws Exception
	 */
	public ProvisioningResponseMessage sendMessage(
			MeerkatDatagram<AbstractMessage> datagram) throws Exception { // NOPMD
		Channel channel = ChannelManagement.getChannel(datagram.getMessage()
				.getAgentId());
		boolean isBlocking = datagram.getMessage().isBlocking();

		if (isBlocking) {
			Callback callback = new Callback();
			CallbackManagement.lock();

			try {
				CallbackManagement.add(callback);

				if (channel != null) {
					channel.writeAndFlush(datagram);
				} else {
					throw new Exception("Channel is null.");
				}
			} finally {
				CallbackManagement.unlock();
			}

			return callback.get();
		} else {
			if (channel != null) {
				channel.writeAndFlush(datagram);
			} else {
				throw new Exception("Channel is null."); // NOPMD
			}

			return null;
		}
	}// end of sendMessage()

	/**
	 * <pre>
	 * channelMap 내에 agentId에 해당하는 Channel이 등록되어 있으면 true, 아니면 false
	 * </pre>
	 * 
	 * @param agentId
	 * @return
	 */
	public boolean isActive(String agentId) {
		return ChannelManagement.getChannel(agentId) != null ? true : false;
	}

	/**
	 * <pre>
	 * channelMap 내에 agentId에 해당하는 Channel을 close 한다.
	 * </pre>
	 * 
	 * @param agentId
	 */
	public void channelClose(String agentId) {
		Channel channel = ChannelManagement.getChannel(agentId);

		if (channel != null) {
			channel.close();
		}
	}

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
		 * @param agentId
		 * @param channel
		 */
		synchronized static void registerChannel(String agentId, Channel channel) { // NOPMD
			LOGGER.debug(
					"agentId({}) and channel({}) will be added to channelMap.",
					agentId, channel);

			// 기존에 등록된 채널이 있을 경우 close한다.
			Channel c = channelMap.get(agentId);
			if (c != null) {
				c.close();
			}

			channelMap.put(agentId, channel);
		}// end of registerChannel()

		/**
		 * <pre>
		 * agentId에 해당하는 채널을 map에서 제거한다.
		 * </pre>
		 * 
		 * @param agentId
		 */
		synchronized static void deregisterChannel(String agentId) { // NOPMD
			LOGGER.debug("agentId({}) will be removed from channelMap.",
					agentId);
			channelMap.remove(agentId);
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
		 * agentId에 해당하는 채널 정보를 가져온다.
		 * </pre>
		 * 
		 * @param agentId
		 * @return
		 */
		static Channel getChannel(String agentId) {
			return channelMap.get(agentId);
		}// end of getChannel()
	}

	// end of ChannelManagement.java

	/**
	 * <pre>
	 * 서버의 처리 순서대로 받기 위한 콜백 클래스
	 * </pre>
	 * 
	 * @author Sang-cheon Park
	 * @version 1.0
	 */
	static class Callback {

		private final CountDownLatch latch = new CountDownLatch(1);

		private ProvisioningResponseMessage response;

		ProvisioningResponseMessage get() {
			try {
				latch.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e); // NOPMD
			}
			return response;
		}

		void handle(ProvisioningResponseMessage response) {
			this.response = response;
			latch.countDown();
		}
	}

	// end of Callback.java

	/**
	 * <pre>
	 * Multi-thread 환경에서 Callback 객체를 관리하기 위한 클래스
	 * </pre>
	 * 
	 * @author Sang-cheon Park
	 * @version 1.0
	 */
	static class CallbackManagement {
		private static final Lock lock = new ReentrantLock();
		private static final Queue<Callback> callbacks = new ConcurrentLinkedQueue<Callback>();

		static void lock() {
			lock.lock();
		}

		static void unlock() {
			lock.unlock();
		}

		static void add(Callback callback) {
			callbacks.add(callback);
		}

		static Callback poll() {
			return callbacks.poll();
		}

		static int getSize() {
			return callbacks.size();
		}
	}
	// end of CallbackManagement.java

}
// end of PeacockServerHandler.java