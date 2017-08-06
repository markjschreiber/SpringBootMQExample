package com.example;

import java.time.LocalTime;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@SpringBootApplication
public class Application {

	@Autowired
	Sender sender;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	
	@Scheduled(fixedRate = 5000)
	public void sendMsg() throws Exception {
		sender.send("Message sent at: "+LocalTime.now());
	}	
	
	
	
	@Component 
	class Sender {  
		@Autowired 
		RabbitMessagingTemplate template;  
		
		@Bean  
		Queue queue() {    
			return new Queue("TestQ", false);  
		}
		
		public void send(String message){ 
			template.convertAndSend("TestQ", message);  
		}
	}
	
	@Component
	class Receiver {
	    @RabbitListener(queues = "TestQ")
	    public void processMessage(String content) {
	       System.out.println(content);
	    }
	}


}
