package ModelRas;

import java.util.UUID;

import com.cloopen.rest.sdk.utils.LoggerUtil;

public class text {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//String info=paycode+money+StoreNo+cOSS_No;
		
        String info="195522723403203414"+"90"+"10001"+"PSOSS";
       // ServerSocket s=new ServerSocket();
        
        
        LoggerUtil.info(info);
       // 1929498870035224129001PSOSS
       //   192949887003522412901PSOSS
		LoggerUtil.info(MD5key.getMD5Pass(info));
		LoggerUtil.info(UUID.randomUUID().hashCode());
//1bcc30d9f6c0071fbaba0d4007806738
	}

}
