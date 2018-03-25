package com.devluff.agent.scheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Schedule {
	
	private static final Logger logger = LoggerFactory.getLogger(Schedule.class);
	private BlockingQueue<String> queSystemCheckResult;
	
	public Schedule() {
		queSystemCheckResult = new ArrayBlockingQueue<>(100);
	}

	public boolean addToSystempCheckResultQue(String task) throws InterruptedException{
		queSystemCheckResult.put(task);
		return true;
	}
	
	public String getSystemCheckResultFromQue() throws InterruptedException{
		return queSystemCheckResult.take();
	}
}
