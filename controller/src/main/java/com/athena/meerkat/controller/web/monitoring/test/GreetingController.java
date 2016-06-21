package com.athena.meerkat.controller.web.monitoring.test;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/monitor/hello")
    @SendToUser("/queue/greetings")
    public Greeting greeting(List<LinkedHashMap> datas) throws Exception {
            //Thread.sleep(3000); // simulated delay

    	
    	List<HelloMessage> messages = new ArrayList<HelloMessage>();
    	
    	for (LinkedHashMap map : datas) {
    		
    		HelloMessage msg = new HelloMessage();
			
    		PropertyUtils.copyProperties(msg, map);
    		
			messages.add(msg);
		}
        
    	String name = null;
        for (HelloMessage message : messages) {
        	name = message.getName();
        	System.out.println("################# greeting. " + message.getName() + " ############");
		}
        
        
        //messagingTemplate.convertAndSendToUser("username", "/queue/greetings", new Greeting("Hello, "+name+"!"));
        return new Greeting("Hello, "+name+"!");
        
    }

}