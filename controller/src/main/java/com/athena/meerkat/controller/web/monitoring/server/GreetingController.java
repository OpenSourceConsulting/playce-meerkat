package com.athena.meerkat.controller.web.monitoring.server;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {


    @MessageMapping("/monitor/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(List<LinkedHashMap> datas) throws Exception {
        //Thread.sleep(3000); // simulated delay
    	
    	List<HelloMessage> messages = new ArrayList<HelloMessage>();
    	
    	for (LinkedHashMap map : datas) {
    		
    		HelloMessage msg = new HelloMessage();
			
    		PropertyUtils.copyProperties(msg, map);
    		
			messages.add(msg);
		}
        
        for (HelloMessage message : messages) {
        	System.out.println("################# greeting. " + message.getName() + " ############");
		}
        
        
        return new Greeting("Hello, aaa!");
    }

}