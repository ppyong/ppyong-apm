package com.devluff.commons.util;

import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	public static boolean isFileExists(String strFilePath) {
		File oTempFile = new File(strFilePath);
		return isFileExists(oTempFile);
	}
	
	public static boolean isFileExists(File oTargetFile) {
		return oTargetFile.exists();
	}
	
	public static boolean writeStringToFile(String strFilePath, String strContent) {
		File file = new File(strFilePath);
		File dir = file.getParentFile();
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strFilePath);
			fos.write(strContent.getBytes());
		}catch (Exception e) {
			logger.error(e.getMessage());
		}finally {
			if(fos != null && fos != null) {
				try {
					fos.close();
				}catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}
		
		return true;
	}
	
	public static boolean deleteFile(String strFilePath) {
		File oTempFile = new File(strFilePath);
		return deleteFile(oTempFile);
	}
	
	public static boolean deleteFile(File oTargetFile) {
		if(!isFileExists(oTargetFile))
			return false;
		
		if(oTargetFile.isDirectory()) {
			File[] arrFile = oTargetFile.listFiles();
			if(arrFile != null) {
				for(File oTempFile : arrFile) {
					logger.debug("Delete folder({})...", oTempFile.getAbsolutePath());
					deleteFile(oTempFile);
				}
			}
		}else {
			oTargetFile.delete();
		}
		return true;
	}
}
