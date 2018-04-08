package com.devluff.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.devluff.commons.markup.XmlParser;
import com.devluff.commons.util.StringUtil;

@Service
public class ApplicationConfig {
	
	private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
	
	private String strServerConfigFilePath;
	private int nServerPORT; 
	
	public boolean loadConfig() {
		try {
		    strServerConfigFilePath = "server_connection.xml";
			readServerConnectionConfig();
		}catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	public int getServerPORT() {
		return this.nServerPORT;
	}
	
	private boolean readServerConnectionConfig() {
		XmlParser parser = new XmlParser();
		
		if(!parser.loadXml(strServerConfigFilePath)) 
			return false;
		
		if(!parser.findElement("server")) 
			return false;
		
		parser.intoToElement();
		
		if(!parser.findElement("port"))
			return false;
		
		nServerPORT = StringUtil.stringToInt(parser.getContent());
		
//		if(parser.findElement("ssl")) {
//			bSSLEnable = StringUtil.stringToBoolean(parser.getAttribute("enable"));
//			strSSLVersion = parser.getAttribute("version");
//		}
		parser.outOfElement();
		return true;
	}

}
