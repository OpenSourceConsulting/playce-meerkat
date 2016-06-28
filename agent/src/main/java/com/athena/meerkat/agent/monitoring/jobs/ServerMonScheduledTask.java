package com.athena.meerkat.agent.monitoring.jobs;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.athena.meerkat.agent.MeerkatAgentConstants;
import com.athena.meerkat.agent.monitoring.NetworkData;
import com.athena.meerkat.agent.monitoring.utils.SigarUtil;

@Component
public class ServerMonScheduledTask extends MonitoringTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerMonScheduledTask.class);

	@Value("${meerkat.agent.server.id:0}")
	private String serverId;

	private List<String> monDatas = new ArrayList<String>();

	private NetworkData netData;

	public ServerMonScheduledTask() {
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@Scheduled(fixedRate = 10000)
	@Override
	public void monitor() {

		monDatas.clear();
		
		if (ServerInitialMonTask.ENABLE_MNITORING == false) {
			LOGGER.debug("======== skip.");
			return;
		}
		
		try {
			CpuPerc cpu = SigarUtil.getCpuPerc();
			Mem mem = SigarUtil.getMem();
			//NetStat netStat = SigarUtil.getNetStat();

			if (netData == null) {
				netData = new NetworkData(SigarUtil.getInstance());
			}

			double cpuUsedPer = (cpu.getCombined()) * 100.0d;
			double memUsed = mem.getActualUsed() / (1024L * 1024L);//mb
			double memUsedPer = mem.getUsedPercent();
			Long[] netDatas = netData.getMetric();

			monDatas.add(createJsonString(MeerkatAgentConstants.MON_FACTOR_ID_CPU_USED_PER, serverId, cpuUsedPer));
			monDatas.add(createJsonString(MeerkatAgentConstants.MON_FACTOR_ID_MEM_USED, serverId, memUsed));
			monDatas.add(createJsonString(MeerkatAgentConstants.MON_FACTOR_ID_MEM_USED_PER, serverId, memUsedPer));
			monDatas.add(createJsonString(MeerkatAgentConstants.MON_FACTOR_ID_NET_IN, serverId, netDatas[0]));
			monDatas.add(createJsonString(MeerkatAgentConstants.MON_FACTOR_ID_NET_OUT, serverId, netDatas[1]));

			sendMonData(monDatas);

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		} finally {
			monDatas.clear();
		}
	}

}