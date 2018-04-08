package com.devluff.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.devluff.server.network.NetworkServerThread;
import io.netty.channel.ChannelHandlerContext;

@Service
public class ApplicationManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationManager.class);
	
	@Autowired private NetworkServerThread oNetworkServerThread;
	@Autowired private ApplicationConfig oApplicationConfig;
	
	// 현재 접속 중인 Agent의 정보를 저장한다. 
	public Map<String, ChannelHandlerContext> oMapCurrentConnectedAgent; 
	
	private CountDownLatch oLatch;
	
	public ApplicationManager() {
	    oMapCurrentConnectedAgent = new HashMap<>();
	}
	
	public boolean init() {
		if(!oApplicationConfig.loadConfig()) {
			logger.error("Loading application's config is fail...");
			return false;
		}
		oLatch = new CountDownLatch(1);
		oNetworkServerThread.setCountDownLatch(oLatch);
		return true;
	}
	
	public boolean process() {
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
		oNetworkServerThread.terminate();
		return true;
	}
}
