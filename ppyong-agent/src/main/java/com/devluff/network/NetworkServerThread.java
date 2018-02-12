package com.devluff.network;

import com.devluff.scheduler.Schedule;

public class NetworkServerThread extends Thread{
	
	private boolean isContinued;
	private String strThreadName;
	private int	nSleepTimeSecond;
	private Schedule oSchedule;
	
	public NetworkServerThread(Schedule oSchedule) {
		this("NetworkClientThread", 10, oSchedule);
	}
	
	public NetworkServerThread(int nSleepTimeSecond, Schedule oSchedule) {
		this("NetworkClientThread", nSleepTimeSecond, oSchedule);
	}
	
	public NetworkServerThread(String strThreadName, Schedule oSchedule) {
		this(strThreadName, 10, oSchedule);
	}
	
	public NetworkServerThread(String strThreadName, int nSleepTimeSecond, Schedule oSchedule) {
		this.strThreadName = strThreadName;
		this.nSleepTimeSecond = nSleepTimeSecond;
		this.oSchedule = oSchedule;
	}
	
	public void run() {
		String task = "";
		try {
			oSchedule.addToTaskQue(task);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

}
