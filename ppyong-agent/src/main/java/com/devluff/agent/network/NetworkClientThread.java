package com.devluff.agent.network;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
	private Socket oSocket;
	
	public NetworkClientThread() {
		this("NetworkClientThread");
	}
	
	public NetworkClientThread(String strThreadName) {
		super(strThreadName);
	}
	
	public void setCountDownLatch(CountDownLatch oLatch) {
		this.oLatch = oLatch;
	}
	
	public boolean init() {
		try {
			oSocket = new Socket("", 0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
	
	// System을 통해
	public void run() {
		isContinued = true;
		try {
			while(isContinued) {
				String task = oSchedule.getSystemCheckResultFromQue();
				synchronized (oLock) {
					// 작업 
					OutputStream oOs = oSocket.getOutputStream();
					oOs.write("".getBytes());
					oOs.flush();
					
					InputStream oIs = oSocket.getInputStream();
					//....
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
