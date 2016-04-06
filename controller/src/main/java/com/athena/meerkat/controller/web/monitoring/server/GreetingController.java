package com.athena.meerkat.controller.web.monitoring.server;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {


    @MessageMapping("/monitor/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        //Thread.sleep(3000); // simulated delay
        
        
        System.out.println("################# greeting. ############");
        
        return new Greeting("Hello, " + message.getName() + "!");
    }

}