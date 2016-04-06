package com.athena.meerkat.agent.monitoring.jobs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.athena.meerkat.agent.monitoring.websocket.StompWebSocketClient;

@Component
public class ScheduledTasks {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Autowired
    private StompWebSocketClient webSocketClient;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
    	//LOGGER.debug("The time is now " + dateFormat.format(new Date()));
    	
    	try{
    	
    		webSocketClient.sendMessage("{\"name\":\"dddd\"}");
    	}catch (IOException e) {
    		LOGGER.error(e.toString(), e);
    	}
    }
}