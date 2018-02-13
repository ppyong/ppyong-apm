package com.devluff.agent.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.devluff.agent.network.NetworkClientThread;
import com.devluff.agent.network.NetworkServerThread;
import com.devluff.commons.util.FileUtil;

@Service
public class ServerResourceCheckScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(NetworkClientThread.class);
	
	@Autowired
	private Schedule oSchedule;
	
	public boolean init() {
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
	
	public boolean finish() {
		return true;
	}

}
