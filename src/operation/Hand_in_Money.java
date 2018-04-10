package operation;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;
import DB.Operation_update;

public class Hand_in_Money extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data");
		LoggerUtil.info(data);
		try {
			JSONObject obj = new JSONObject(data);
			String RMB = obj.getString("RMB");
			String ZFB = obj.getString("ZFB");
			String WeiXIN = obj.getString("WeiXIN");
			String Chu_Zhi_Ka = obj.getString("Chu_Zhi_Ka");
			String Bank_Union = obj.getString("Bank_Union");
			String cStoreNo = obj.getString("cStoreNo");
			String cStoreName = obj.getString("cStoreName");
			String UserName = obj.getString("UserName");
			String UserNo = obj.getString("UserNo");
			String SuperNo = obj.getString("SuperNo");
			String SuperName = obj.getString("SuperName");
			String Wallet=obj.getString("Wallet");
			boolean a = Operation_update.hand_in_money(
					GetConnection.getStoreConn(), RMB, ZFB, WeiXIN, Chu_Zhi_Ka,
					Bank_Union, cStoreNo, cStoreName, UserName, UserNo,
					SuperNo, SuperName,Wallet);
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + 1
						+ "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + 1
						+ "}");
			}
			LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":"+ 1 + "}");

		} catch (Exception e) {
			e.printStackTrace();
		}

		out.flush();
		out.close();
	}

}
