package cn.jpush.example;
import java.util.HashMap;
import java.util.Map;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.IOSExtra;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

public class JPushClientExample {
    //在极光注册上传应用的 appKey 和 masterSecret
//	private static  String appKey ="9084c4a8e2ff6a5bc3abf10b";////必填，例如466f7032ac604e02fb7bda89
//	
//	private static  String masterSecret = "45e43733df1dc3069f637d57";//必填，每个应用都对应一个masterSecret
	
	
	private static  String appKey ="23e9298b957cdb05953dff21";////必填，例如466f7032ac604e02fb7bda89
	
	private static  String masterSecret = "fcaa4de1a8f655502d38b6f8";//必填，每个应用都对应一个masterSecret
	
	private static JPushClient jpush = null;

	/*
	 * 保存离线的时长。秒为单位。最多支持10天（864000秒）。
	 * 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。
	 * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒
	 */
	private static long timeToLive =  60 * 60 * 24;  

	public static void main(String[] args) {
		/*
		 * Example1: 初始化,默认发送给android和ios，同时设置离线消息存活时间
		 * jpush = new JPushClient(masterSecret, appKey, timeToLive);
		 * 
		 * Example2: 只发送给android
		 * jpush = new JPushClient(masterSecret, appKey, DeviceEnum.Android);
		 * 
		 * Example3: 只发送给IOS
		 * jpush = new JPushClient(masterSecret, appKey, DeviceEnum.IOS);
		 * 
		 * Example4: 只发送给android,同时设置离线消息存活时间
		 * jpush = new JPushClient(masterSecret, appKey, timeToLive, DeviceEnum.Android);
		 */
 	//	jpush = new JPushClient(masterSecret, appKey, timeToLive);
		/* 
		 * 是否启用ssl安全连接, 可选
		 * 参数：启用true， 禁用false，默认为非ssl连接
		 */
		//jpush.setEnableSSL(true);

		//测试发送消息或者通知
		testSend("呵呵","大家好啦啦啦啦");
	}
	public  static void testSend1(String title,String content) {
		 appKey ="9084c4a8e2ff6a5bc3abf10b";////必填，例如466f7032ac604e02fb7bda89
		
	      masterSecret = "45e43733df1dc3069f637d57";//必填，每个应用都对应一个masterSecret
		
		jpush = new JPushClient(masterSecret, appKey, timeToLive);
	    // 在实际业务中，建议 sendNo 是一个你自己的业务可以处理的一个自增数字。
	    // 除非需要覆盖，请确保不要重复使用。详情请参考 API 文档相关说明。
//	    Integer num= getRandomSendNo();
	    String sendNo="1900192560";
		String msgTitle = title;
		String msgContent = content;
		
		/*
		 * IOS设备扩展参数,
		 * 设置badge，设置声音
		 */

		Map<String, Object> extra = new HashMap<String, Object>();
		IOSExtra iosExtra = new IOSExtra(1, "WindowsLogonSound.wav");
		extra.put("ios", iosExtra);
		//IOS和安卓一起
		MessageResult msgResult = jpush.sendNotificationWithAppKey(sendNo, msgTitle, msgContent, 0, extra);

		//对所有用户发送通知, 更多方法请参考文档
	//	MessageResult msgResult = jpush.sendCustomMessageWithAppKey(sendNo,msgTitle, msgContent);
		
		if (null != msgResult) {
			LoggerUtil.info("服务器返回数据: " + msgResult.toString());
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				LoggerUtil.info("发送成功， sendNo=" + msgResult.getSendno());
			} else {
				LoggerUtil.info("发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg());
			}
		} else {
			LoggerUtil.info("无法获取数据");
		}
		
		
	}

	public  static void testSend(String title,String content) {
		 appKey ="05e7785e99c1ddfb07802f46";////必填，例如466f7032ac604e02fb7bda89
		 masterSecret = "2f432d10dc7e5883cd1f413d";//必填，每个应用都对应一个masterSecret
		jpush = new JPushClient(masterSecret, appKey, timeToLive);
	  
		// 在实际业务中，建议 sendNo 是一个你自己的业务可以处理的一个自增数字。
	    // 除非需要覆盖，请确保不要重复使用。详情请参考 API 文档相关说明。
//	    Integer num= getRandomSendNo();
	    String sendNo="1900192560";
		String msgTitle = title;
		String msgContent = content;
		
		/*
		 * IOS设备扩展参数,
		 * 设置badge，设置声音
		 */

		Map<String, Object> extra = new HashMap<String, Object>();
		IOSExtra iosExtra = new IOSExtra(1, "WindowsLogonSound.wav");
		extra.put("ios", iosExtra);
		//IOS和安卓一起
		MessageResult msgResult = jpush.sendNotificationWithAppKey(sendNo, msgTitle, msgContent, 0, extra);

		//对所有用户发送通知, 更多方法请参考文档
	//	MessageResult msgResult = jpush.sendCustomMessageWithAppKey(sendNo,msgTitle, msgContent);
		
		if (null != msgResult) {
			LoggerUtil.info("服务器返回数据: " + msgResult.toString());
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				LoggerUtil.info("发送成功， sendNo=" + msgResult.getSendno());
			} else {
				LoggerUtil.info("发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg());
			}
		} else {
			LoggerUtil.info("无法获取数据");
		}
		
		
	}
	
    public static final int MAX = Integer.MAX_VALUE;
	public static final int MIN = (int) MAX/2;
	
	/**
	 * 保持 sendNo 的唯一性是有必要的
	 * It is very important to keep sendNo unique.
	 * @return sendNo
	 */
	public static int getRandomSendNo() {
	    return (int) (MIN + Math.random() * (MAX - MIN));
	}
	
}
