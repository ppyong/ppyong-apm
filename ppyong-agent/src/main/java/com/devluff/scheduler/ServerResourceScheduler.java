package com.devluff.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.devluff.commons.util.FileUtil;
import com.devluff.network.NetworkClientThread;
import com.devluff.network.NetworkServerThread;

@Service
public class ServerResourceScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(NetworkClientThread.class);
	
	private Schedule oSchedule;
	private NetworkClientThread oNetworkClientThread;
	private NetworkServerThread oNetworkServerThread;
	
	public ServerResourceScheduler() {
		oSchedule = new Schedule();
		oNetworkClientThread = new NetworkClientThread(oSchedule);
		oNetworkServerThread = new NetworkServerThread(oSchedule);
	}
	
	public boolean init() {
		oNetworkClientThread.start();
		oNetworkServerThread.start();
		return true;
	}
	
	@Scheduled(fixedDelay=1000)
	public void checkTaskSchedule() {
		String sResult = "";
		if(!writeResultToXMLFile(sResult)) {
			logger.error("Fail to save schedule result to file...");
		}
	}
	
	private boolean writeResultToXMLFile(String sResult) {
		String strFileName = System.currentTimeMillis() + ".temp";
		try {
			// 파일 저장 
			FileUtil.writeStringToFile(strFileName, sResult);
		}catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

}
