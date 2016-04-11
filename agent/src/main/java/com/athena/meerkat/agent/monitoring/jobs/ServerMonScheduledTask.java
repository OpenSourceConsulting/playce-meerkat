package com.athena.meerkat.agent.monitoring.jobs;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.athena.meerkat.agent.monitoring.utils.SigarUtil;

@Component
public class ServerMonScheduledTask extends MonitoringTask{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerMonScheduledTask.class);

    //private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Value("${meerkat.agent.server.id:0}")
    private String serverId;
    
		
	public ServerMonScheduledTask() {
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@Scheduled(fixedRate = 5000)
	@Override
    public void monitor() {
    	//LOGGER.debug("The time is now " + dateFormat.format(new Date()));
    	
    	try{
    		CpuPerc cpu = SigarUtil.getCpuPerc();
    		Mem mem = SigarUtil.getMem();
    		NetStat netStat = SigarUtil.getNetStat();
    		
    		double cpuUsed = (cpu.getCombined()) * 100.0d ;
    		long memUsed = mem.getActualUsed() / 1024L;
    		int netIn = netStat.getAllInboundTotal();
    		int netOut = netStat.getAllOutboundTotal();
    		
    		String cpuUsedJson = createJsonString("cpu.used", serverId, cpuUsed);
    		String memUsedJson = createJsonString("mem.used", serverId, memUsed);
    		String netInJson = createJsonString("net.in", serverId, netIn);
    		String netOutJson = createJsonString("net.out", serverId, netOut);
    		//TODO tran : verify data and add additional data(disk.used, disk.free)
    		
    		sendMonData(cpuUsedJson, memUsedJson, netInJson, netOutJson);
    		
    	}catch (Exception e) {
    		LOGGER.error(e.toString(), e);
    	}
    }

	

    
    
    
}