package Tool;

import org.apache.log4j.Logger;

import DB.GetConnection;

public class GetLog {
	
	public static <T> Logger getLogger (Class<T> a){
		Logger logger = Logger.getLogger(a);
		return logger;
	}
	
	public static void main(String args[]){
		System.out.println(123);
		try{
		     System.out.println(3/0);;
		}catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger(GetLog.class).error(e.getCause());
			System.out.println();
			GetLog.getLogger(GetLog.class).error(e.getLocalizedMessage());
//			System.out.println(e.getMessage());
//			System.out.println(e.toString());
		}
		
	}
}
