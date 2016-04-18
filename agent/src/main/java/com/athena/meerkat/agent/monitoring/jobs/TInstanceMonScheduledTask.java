package com.athena.meerkat.agent.monitoring.jobs;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.NetConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.athena.meerkat.agent.monitoring.utils.SigarUtil;
import com.athena.meerkat.agent.monitoring.websocket.StompWebSocketClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * <pre>
 * tomcat instance monitoring.
 * - instance status.
 * - jmx monitoring.
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Component
public class TInstanceMonScheduledTask extends MonitoringTask{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TInstanceMonScheduledTask.class);

    
    private List<String> monDatas = new ArrayList<String>();
    
    
    @Autowired
    private StompWebSocketClient webSocketClient;
    
		
	public TInstanceMonScheduledTask() {
	}


	@Scheduled(fixedRate = 10000)
	@Override
    public void monitor() {
    	
		monDatas.clear();
    	try{
    		ArrayNode configs = webSocketClient.getInstanceConfigs();
    		
    		if (configs == null) {
    			LOGGER.debug("tomcat instance configs is null. monitoring skip!");
    			return;
			}
    		
    		NetConnection[] listenPorts = SigarUtil.getListenPorts();
    		
    		
    		for (JsonNode jsonNode : configs) {
    			
    			String tomcatInstanceId = jsonNode.get("tomcatInstanceId").asText();
    			long port = jsonNode.get("httpPort").asLong();
    			int isRun = 7; // TS_STATE common code id.
    			for (NetConnection netConnection : listenPorts) {
    				if (netConnection.getLocalPort() == port) {
    					isRun = 8;
    				}
    			}
    			
    			monDatas.add(createJmxJsonString("ti.run", tomcatInstanceId, isRun));//tomcat instance running status.
    		}
    		
    		//TODO add another jmx monitoring.
    		
    		sendInstanceMonData(monDatas);
    		
    	} catch (Exception e) {
    		LOGGER.error(e.toString(), e);
    	} finally {
    		monDatas.clear();
    	}
    }
    
    
}