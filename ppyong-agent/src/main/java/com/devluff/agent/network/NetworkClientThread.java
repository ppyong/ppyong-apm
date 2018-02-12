package com.devluff.agent.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devluff.agent.scheduler.Schedule;

/**
 * Server Agent 로의 Server 정보를 보내기 위한 Thread
 * @author ppyong
 *
 */
public class NetworkClientThread extends Thread{
	
	private static final Logger logger = LoggerFactory.getLogger(NetworkClientThread.class);

	private boolean isContinued;
	private String strThreadName;
	private int	nSleepTimeSecond;
	private Schedule oSchedule;
	
	public NetworkClientThread(Schedule schedule) {
		this("NetworkClientThread", 10, schedule);
	}
	
	public NetworkClientThread(int nSleepTimeSecond, Schedule schedule) {
		this("NetworkClientThread", nSleepTimeSecond, schedule);
	}
	
	public NetworkClientThread(String strThreadName, Schedule schedule) {
		this(strThreadName, 10, schedule);
	}
	
	public NetworkClientThread(String strThreadName, int nSleepTimeSecond, Schedule oSchedule) {
		this.strThreadName = strThreadName;
		this.nSleepTimeSecond = nSleepTimeSecond;
		this.oSchedule = oSchedule;
	}
	
	public void run() {
		this.isContinued = true;
		try {
			while(this.isContinued) {
				// 작업 
				String task = oSchedule.getTaskFromTaskQue();
				
				// 처리 
				
				Thread.sleep(nSleepTimeSecond * 1000);
			}
		}catch (Exception e) {
			if(!interrupted()) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	public void closeSocket() {
		this.isContinued = false;
		Thread.currentThread().interrupt();
	}
}
