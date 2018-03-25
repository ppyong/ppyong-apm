package com.devluff.agent.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devluff.agent.ApplicationManager;

// 1) 정책을 받음
// 2) 프로세스 종료 명령을 받음
@Service
public class NetworkServerThread extends Thread{
	
	private static final Logger logger = LoggerFactory.getLogger(NetworkServerThread.class);
	
	@Autowired private ApplicationManager oApplicationManager;
	
	private boolean isContinued;
	private ServerSocket oServerSocket;
	private Object oLock;
	private CountDownLatch oLatch;
	
	public NetworkServerThread() {
		this("NetworkServerThread");
	}
	
	public NetworkServerThread(String strThreadName) {
		super(strThreadName);
	}
	
	public void setCountDownLatch(CountDownLatch oLatch) {
		this.oLatch = oLatch;
	}
	
	public void run() {
		try {
			oServerSocket = new ServerSocket(10);
			while(isContinued) {
				Socket oSocket = oServerSocket.accept();
				synchronized (oLock) {
					// 작업 
					
					// 스케쥴 변경 
//				boolean bResult = oApplicationManager.changeAgentScheduleInfo();
//				if (!bResult) {
//					logger.error("Fail to change agent schedule...");
//				}

					// 종료
//					if(){
//						oApplicationManager.terminateProcess();
//					}
//					else if() {
//						
//					}
				}
			}
		}catch (Exception e) {
			if(!(e instanceof InterruptedException)) {
				logger.error(e.getMessage());
			}
		}finally {
			oLatch.countDown();
		}
	}
	
	public void terminate() {
		// ServerSocket 을 통해 받은 액션을 완료하기 까지 대기한다. 
		synchronized (oLock) {
			isContinued = false;
			if(oServerSocket != null && !oServerSocket.isClosed()) {
				try {
					oServerSocket.close();
				} catch (IOException e) {
					if(e instanceof SocketException) {
						logger.info("Server Socket is closed...");
					}else {
						logger.error(e.getMessage());
					}
				}
			}
		}
	}
}
