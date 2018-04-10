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

public class Fen_dian_yan_shou extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("<HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
	  	out.print("  This is ");
	  //out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data").replace("\n", "").replace("\r", "");
		LoggerUtil.info(""+data);
		try { 
			JSONObject obj=new JSONObject(data);
			String AppNo=obj.getString("AppNo");
			JSONArray array = obj.getJSONArray("dData");
			JSONArray array1=obj.getJSONArray("dData1");
			JSONArray array2=obj.getJSONArray("dData2");
			boolean a = Fen_dian_Update.Insert_into_fen_dian_yan_dan(GetConnection.getStoreConn(AppNo), array,array1,array2);
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\"" + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"" + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"" + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
