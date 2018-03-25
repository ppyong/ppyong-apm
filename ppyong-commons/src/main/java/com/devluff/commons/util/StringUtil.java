package com.devluff.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {
	
	public static Logger logger = LoggerFactory.getLogger(StringUtil.class);
	
	public static String convertNullToEmptyString(String str) {
		return str == null ? "" : str;
	}

	public static boolean stringToBoolean(String str) {
		if(str != null && str.length() > 0) {
			if(str.equals("1"))
				return true;
			else
				return false;
		}
		return false;
	}
	
	public static int stringToInt(String str) {
		if(str != null && str.length() > 0) {
			try {
				return Integer.parseInt(str);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
		}else {
			return 0;
		}
	}
	
	public static String intToString(int i) {
		return String.valueOf(i);
	}
}
