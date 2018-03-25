package com.devluff.server.network;

import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NetworkClientThread extends Thread{
	
	private static final Logger logger = LoggerFactory.getLogger(NetworkClientThread.class);
	
	private boolean isContinued;
	private Object oLock;
	private CountDownLatch oLatch;
	private Socket oSocket;
	
	public void setCountDownLatch(CountDownLatch oLatch) {
		this.oLatch = oLatch;
	}

	public void terminate() {
		synchronized (oLock) {
			this.isContinued = false;
			Thread.currentThread().interrupt();
		}
	}
}
