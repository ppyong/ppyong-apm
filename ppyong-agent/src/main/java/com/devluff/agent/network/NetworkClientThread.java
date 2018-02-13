package com.devluff.agent.network;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.devluff.agent.scheduler.Schedule;

/**
 * Server Agent 로의 Server 정보를 보내기 위한 Thread
 * @author ppyong
 *
 */
// 1) 정책에 따라 생성된 데이터를 Server로 전송 
public class NetworkClientThread extends Thread{
	
	private static final Logger logger = LoggerFactory.getLogger(NetworkClientThread.class);

	@Autowired private Schedule oSchedule;
	
	private boolean isContinued;
	private Object oLock;
	private CountDownLatch oLatch;
	
	public NetworkClientThread() {
		this("NetworkClientThread");
	}
	
	public NetworkClientThread(String strThreadName) {
		super(strThreadName);
	}
	
	public void setCountDownLatch(CountDownLatch oLatch) {
		this.oLatch = oLatch;
	}
	
	public void run() {
		isContinued = true;
		try {
			while(isContinued) {
				String task = oSchedule.getTaskFromTaskQue();
				synchronized (oLock) {
					// 작업 
				}
			}
		}catch (Exception e) {
			if(!interrupted()) {
				logger.error(e.getMessage(), e);
			}
		}finally {
			oLatch.countDown();
		}
	}
	
	public void terminate() {
		synchronized (oLock) {
			this.isContinued = false;
			Thread.currentThread().interrupt();
		}
	}
}
