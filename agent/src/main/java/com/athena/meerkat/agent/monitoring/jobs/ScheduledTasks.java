package com.athena.meerkat.agent.monitoring.jobs;

import java.text.SimpleDateFormat;

import org.hyperic.sigar.CpuPerc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.athena.meerkat.agent.monitoring.utils.SigarUtil;
import com.athena.meerkat.agent.monitoring.websocket.StompWebSocketClient;

@Component
public class ScheduledTasks {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Value("${meerkat.agent.server.id:0}")
    private String serverId;
    
    @Autowired
    private StompWebSocketClient webSocketClient;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
    	//LOGGER.debug("The time is now " + dateFormat.format(new Date()));
    	
    	try{
    		CpuPerc cpu = SigarUtil.getCpuPerc();
    		System.out.println(cpu);
    		double value = (cpu.getUser() + cpu.getSys()) * 100.0d ;
    		
    		
    		webSocketClient.sendMessage("{\"monFactorId\":\"cpu\",\"serverId\":\""+serverId+"\",\"monValue\":\""+value+"\"}");
    		
    	}catch (Exception e) {
    		LOGGER.error(e.toString(), e);
    	}
    }
    
}