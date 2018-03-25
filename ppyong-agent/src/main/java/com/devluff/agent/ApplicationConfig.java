package com.devluff.agent;

import com.devluff.agent.scheduler.AgentScheduleInfo;
import com.devluff.commons.markup.XmlParser;
import com.devluff.commons.util.StringUtil;

public class ApplicationConfig {
	
	private String strServerConfigFilePath;
	private String strServerIP;
	private int nServerPORT;
	private boolean bSSLEnable;
	private String strSSLVersion;
	private AgentScheduleInfo oAgentScheduleInfo;
	
	public ApplicationConfig() {
		
	}
	
	public boolean loadConfig() {
		readServerConnectionConfig();
		
		return true;
	}
	
	public String getServerIP() {
		return strServerIP;
	}
	
	public int getServerPORT() {
		return nServerPORT;
	}
	
	public AgentScheduleInfo getAgentScheduleInfo() {
		return oAgentScheduleInfo;
	}
	
	/**
	 * <server>
	 *	 <ip>192.1.1.1</ip>
	 *	 <port>443</port>
	 *   <ssl enable="1" version="TLSv1.0"/>
	 * </server>
	 * @return
	 */
	private boolean readServerConnectionConfig() {
		XmlParser parser = new XmlParser();
		
		if(!parser.loadXml(strServerConfigFilePath)) 
			return false;
		
		if(!parser.findElement("server")) 
			return false;
		
		parser.intoToElement();
		
		if(!parser.findElement("ip"))
			return false;
		
		strServerIP = parser.getContent();
		
		if(!parser.findElement("port"))
			return false;
		
		nServerPORT = StringUtil.stringToInt(parser.getContent());
		
		if(parser.findElement("ssl")) {
			bSSLEnable = StringUtil.stringToBoolean(parser.getAttribute("enable"));
			strSSLVersion = parser.getAttribute("version");
		}
		return true;
	}
	
	public boolean readScehdulePolicy() {
		return true;
	}

}
