package com.devluff.commons.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
	
	Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	public static String convertDateToString(Date oDate) {
		return convertDateToStringByFormat(oDate, "yyyy-MM-dd hh:mm:ss");
	}
	
	public static String convertDateToStringByFormat(Date oDate, String format) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(oDate);
		} catch (Exception e) {
			
			return "1970-01-01 00:00:00";
		}
	}
}
