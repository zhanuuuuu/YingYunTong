package cn.jpush.example;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.IOSExtra;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

public class JPushClientExample {
    //�ڼ���ע���ϴ�Ӧ�õ� appKey �� masterSecret
//	private static  String appKey ="9084c4a8e2ff6a5bc3abf10b";////�������466f7032ac604e02fb7bda89
//	
//	private static  String masterSecret = "45e43733df1dc3069f637d57";//���ÿ��Ӧ�ö���Ӧһ��masterSecret
	
	
	private static  String appKey ="23e9298b957cdb05953dff21";////�������466f7032ac604e02fb7bda89
	
	private static  String masterSecret = "fcaa4de1a8f655502d38b6f8";//���ÿ��Ӧ�ö���Ӧһ��masterSecret
	
	private static JPushClient jpush = null;

	/*
	 * �������ߵ�ʱ������Ϊ��λ�����֧��10�죨864000�룩��
	 * 0 ��ʾ����Ϣ���������ߡ������û��������Ϸ�������ǰ�������û��������յ�����Ϣ��
	 * �˲������������ʾĬ�ϣ�Ĭ��Ϊ����1���������Ϣ��86400��
	 */
	private static long timeToLive =  60 * 60 * 24;  

	public static void main(String[] args) {
		/*
		 * Example1: ��ʼ��,Ĭ�Ϸ��͸�android��ios��ͬʱ����������Ϣ���ʱ��
		 * jpush = new JPushClient(masterSecret, appKey, timeToLive);
		 * 
		 * Example2: ֻ���͸�android
		 * jpush = new JPushClient(masterSecret, appKey, DeviceEnum.Android);
		 * 
		 * Example3: ֻ���͸�IOS
		 * jpush = new JPushClient(masterSecret, appKey, DeviceEnum.IOS);
		 * 
		 * Example4: ֻ���͸�android,ͬʱ����������Ϣ���ʱ��
		 * jpush = new JPushClient(masterSecret, appKey, timeToLive, DeviceEnum.Android);
		 */
 	//	jpush = new JPushClient(masterSecret, appKey, timeToLive);
		/* 
		 * �Ƿ�����ssl��ȫ����, ��ѡ
		 * ����������true�� ����false��Ĭ��Ϊ��ssl����
		 */
		//jpush.setEnableSSL(true);

		//���Է�����Ϣ����֪ͨ
		testSend("�Ǻ�","��Һ���������");
	}
	public  static void testSend1(String title,String content) {
		 appKey ="9084c4a8e2ff6a5bc3abf10b";////�������466f7032ac604e02fb7bda89
		
	      masterSecret = "45e43733df1dc3069f637d57";//���ÿ��Ӧ�ö���Ӧһ��masterSecret
		
		jpush = new JPushClient(masterSecret, appKey, timeToLive);
	    // ��ʵ��ҵ���У����� sendNo ��һ�����Լ���ҵ����Դ����һ���������֡�
	    // ������Ҫ���ǣ���ȷ����Ҫ�ظ�ʹ�á�������ο� API �ĵ����˵����
//	    Integer num= getRandomSendNo();
	    String sendNo="1900192560";
		String msgTitle = title;
		String msgContent = content;
		
		/*
		 * IOS�豸��չ����,
		 * ����badge����������
		 */

		Map<String, Object> extra = new HashMap<String, Object>();
		IOSExtra iosExtra = new IOSExtra(1, "WindowsLogonSound.wav");
		extra.put("ios", iosExtra);
		//IOS�Ͱ�׿һ��
		MessageResult msgResult = jpush.sendNotificationWithAppKey(sendNo, msgTitle, msgContent, 0, extra);

		//�������û�����֪ͨ, ���෽����ο��ĵ�
	//	MessageResult msgResult = jpush.sendCustomMessageWithAppKey(sendNo,msgTitle, msgContent);
		
		if (null != msgResult) {
			System.out.println("��������������: " + msgResult.toString());
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				System.out.println("���ͳɹ��� sendNo=" + msgResult.getSendno());
			} else {
				System.out.println("����ʧ�ܣ� �������=" + msgResult.getErrcode() + ", ������Ϣ=" + msgResult.getErrmsg());
			}
		} else {
			System.out.println("�޷���ȡ����");
		}
		
		
	}

	public  static void testSend(String title,String content) {
		 appKey ="05e7785e99c1ddfb07802f46";////�������466f7032ac604e02fb7bda89
		 masterSecret = "2f432d10dc7e5883cd1f413d";//���ÿ��Ӧ�ö���Ӧһ��masterSecret
		jpush = new JPushClient(masterSecret, appKey, timeToLive);
	  
		// ��ʵ��ҵ���У����� sendNo ��һ�����Լ���ҵ����Դ����һ���������֡�
	    // ������Ҫ���ǣ���ȷ����Ҫ�ظ�ʹ�á�������ο� API �ĵ����˵����
//	    Integer num= getRandomSendNo();
	    String sendNo="1900192560";
		String msgTitle = title;
		String msgContent = content;
		
		/*
		 * IOS�豸��չ����,
		 * ����badge����������
		 */

		Map<String, Object> extra = new HashMap<String, Object>();
		IOSExtra iosExtra = new IOSExtra(1, "WindowsLogonSound.wav");
		extra.put("ios", iosExtra);
		//IOS�Ͱ�׿һ��
		MessageResult msgResult = jpush.sendNotificationWithAppKey(sendNo, msgTitle, msgContent, 0, extra);

		//�������û�����֪ͨ, ���෽����ο��ĵ�
	//	MessageResult msgResult = jpush.sendCustomMessageWithAppKey(sendNo,msgTitle, msgContent);
		
		if (null != msgResult) {
			System.out.println("��������������: " + msgResult.toString());
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				System.out.println("���ͳɹ��� sendNo=" + msgResult.getSendno());
			} else {
				System.out.println("����ʧ�ܣ� �������=" + msgResult.getErrcode() + ", ������Ϣ=" + msgResult.getErrmsg());
			}
		} else {
			System.out.println("�޷���ȡ����");
		}
		
		
	}
	
    public static final int MAX = Integer.MAX_VALUE;
	public static final int MIN = (int) MAX/2;
	
	/**
	 * ���� sendNo ��Ψһ�����б�Ҫ��
	 * It is very important to keep sendNo unique.
	 * @return sendNo
	 */
	public static int getRandomSendNo() {
	    return (int) (MIN + Math.random() * (MAX - MIN));
	}
	
}
