package com.devluff.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
	
	private static Logger logger = LoggerFactory.getLogger(Application.class);		
	
	public void run(String... args) throws Exception {
		logger.info("test");
	}
	
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
	}
}
