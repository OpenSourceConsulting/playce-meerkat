package com.athena.meerkat.agent.monitoring.jobs;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.NetStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.athena.meerkat.agent.monitoring.utils.SigarUtil;
import com.athena.meerkat.agent.monitoring.websocket.StompWebSocketClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Component
public class ServerMonScheduledTask extends MonitoringTask {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ServerMonScheduledTask.class);

	@Value("${meerkat.agent.server.id:0}")
	private String serverId;

	private List<String> monDatas = new ArrayList<String>();

	public ServerMonScheduledTask() {
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@Scheduled(fixedRate = 10000)
	@Override
	public void monitor() {

		monDatas.clear();
		try {
			CpuPerc cpu = SigarUtil.getCpuPerc();
			Mem mem = SigarUtil.getMem();
			NetStat netStat = SigarUtil.getNetStat();

			double cpuUsed = (cpu.getCombined()) * 100.0d;
			double memUsed = mem.getActualUsed() / (1024L*1024L);
			double memUsedPer = mem.getUsedPercent();
			int netIn = netStat.getAllInboundTotal();
			int netOut = netStat.getAllOutboundTotal();

			monDatas.add(createJsonString("cpu.used", serverId, cpuUsed));
			monDatas.add(createJsonString("mem.used", serverId, memUsed));
			monDatas.add(createJsonString("mem.used_per", serverId, memUsedPer));
			monDatas.add(createJsonString("net.in", serverId, netIn));
			monDatas.add(createJsonString("net.out", serverId, netOut));
			// TODO tran : verify data and add additional data

			sendMonData(monDatas);

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		} finally {
			monDatas.clear();
		}
	}

}