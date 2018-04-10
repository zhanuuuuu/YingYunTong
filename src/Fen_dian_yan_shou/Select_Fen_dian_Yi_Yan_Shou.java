package Fen_dian_yan_shou;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.Fen_dian_Update;
import DB.GetConnection;

public class Select_Fen_dian_Yi_Yan_Shou extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
			String AppNo = obj.getString("AppNo");
           //先总部掉，再从掉
			//JSONArray array = Fen_dian_Update.Select_Yi_Shen_Fen_dian_yan_dan(GetConnection.getStoreConn(), AppNo);
			//LoggerUtil.info("到此");
			//if (array.length() == 0) {
				JSONArray array = Fen_dian_Update.Select_Yi_Shen_Fen_dian_yan_dan(GetConnection.getStoreConn(AppNo), AppNo);
			//	LoggerUtil.info("到此1");
			//}
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
						+ array.toString() + "}");
			}
			LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\","	+ "\"dDate\":" + array.toString() + "}");

		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
