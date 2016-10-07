package cn.yau.utils;

import org.apache.log4j.Logger;

public final class Log4jUtils {
	public static Logger logger;
	
	private Log4jUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static Logger getLogger(Class<?> clazz){
		return Logger.getLogger(clazz);
	}
}
