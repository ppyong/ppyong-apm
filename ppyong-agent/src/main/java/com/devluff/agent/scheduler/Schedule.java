package com.devluff.agent.scheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Schedule {
	
	private static final Logger logger = LoggerFactory.getLogger(Schedule.class);
	private BlockingQueue<String> taskQue;
	
	public Schedule() {
		taskQue = new ArrayBlockingQueue<>(100);
	}

	public boolean addToTaskQue(String task) throws InterruptedException{
		taskQue.put(task);
		return true;
	}
	
	public String getTaskFromTaskQue() throws InterruptedException{
		return taskQue.take();
	}
}
