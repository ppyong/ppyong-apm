package com.devluff.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
	
	private static Logger logger = LoggerFactory.getLogger(Application.class);		
	
	@Autowired ApplicationManager oApplicationManager;
	
	public void run(String... args) throws Exception {
		if(!oApplicationManager.init()) {
			logger.error("Application Manager init is fail!");
			return;
		}
		boolean ret = oApplicationManager.process();
		logger.debug("process() result is {}", ret ? "true" : "false");
	}
	
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
	}
}
