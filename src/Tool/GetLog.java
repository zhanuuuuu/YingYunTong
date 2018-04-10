package Tool;

import org.apache.log4j.Logger;

import com.cloopen.rest.sdk.utils.LoggerUtil;

public class GetLog {
	
	public static <T> Logger getLogger (Class<T> a){
		Logger logger = Logger.getLogger(a);
		return logger;
	}
	
	public static void main(String args[]){
		LoggerUtil.info(123);
		try{
		     LoggerUtil.info(3/0);;
		}catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger(GetLog.class).error(e.getCause());
			LoggerUtil.info("");
			GetLog.getLogger(GetLog.class).error(e.getLocalizedMessage());
//			LoggerUtil.info(e.getMessage());
//			LoggerUtil.info(e.toString());
		}
		
	}
}
