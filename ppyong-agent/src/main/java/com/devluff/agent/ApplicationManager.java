package com.devluff.agent;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devluff.agent.network.NetworkClientThread;
import com.devluff.agent.network.NetworkServerThread;

@Service
public class ApplicationManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationManager.class);
	
	@Autowired private NetworkClientThread oNetworkClientThread;
	@Autowired private NetworkServerThread oNetworkServerThread;
	
	private CountDownLatch oLatch;
	
	public ApplicationManager() {
	}
	
	public boolean init() {
		oLatch = new CountDownLatch(2);
		oNetworkClientThread.setCountDownLatch(oLatch);
		oNetworkServerThread.setCountDownLatch(oLatch);
		return true;
	}
	
	public boolean process() {
		oNetworkClientThread.start();
		oNetworkServerThread.start();
		try {
			oLatch.await();
		} catch (InterruptedException e) {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean terminateProcess() {
		oNetworkClientThread.terminate();
		oNetworkServerThread.terminate();
		return true;
	}
}
