package Tool;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class httpTool {
	
	static HashMap<String, String> map=new HashMap<String, String>();
	static {
		PutPort();
	}
	public static String Get_Result(HttpServletRequest request, HttpServletResponse response) {
		//String url=request.getParameter("url");
		Enumeration<String> e = request.getParameterNames(); 
		String a = new String();
		System.out.println("啦啦");
		while (e.hasMoreElements()) {
		
			String paramName = (String) e.nextElement();
			System.out.println("呵呵key"+paramName );
			String paramValue = request.getParameterValues(paramName)[0];
			a=a+paramName +"="+paramValue+"&";
		}
		String data =a.toString();
		System.out.println(data);
		//String str = httpTool.POST(url, data);
		return data;
	}

//	public static String POST(String name, String data) {
//		try {
//			System.out.println(name);
//			String IP=new ReadConfig().getipprop().getProperty("IP");
//			String path=IP+"warelucent/"+map.get(name);
//			System.out.println(path);
//			URL url = new URL(path);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setDoOutput(true);
//			conn.setRequestMethod("POST");
//			OutputStream out = conn.getOutputStream();
//			out.write(data.getBytes("UTF-8"));
//			InputStream in = conn.getInputStream();
//			ByteArrayOutputStream byyut = new ByteArrayOutputStream();
//			int len = 0;
//			byte b[] = new byte[1024];
//			while ((len = in.read(b)) != -1) {
//				byyut.write(b, 0, len);
//			}
//			String strname = new String(byyut.toByteArray(), "UTF-8");
//			return strname;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	public static void PutPort(){
		map.put("001", "Select_Vip_Message"); //查询储值卡
		map.put("002", "Vip_AddScore");       //会员卡积分
		map.put("003", "Select_Moneycard"); //查询储值卡信息
		map.put("004", "MoneyCard_Add_Subtract"); //储值卡加减钱
		map.put("005", "Account_checking"); //储值卡对账
	}
}
