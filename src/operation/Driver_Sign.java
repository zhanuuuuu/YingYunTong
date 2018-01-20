package operation;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DB.GetConnection;
import DB.Operation_update;

public class Driver_Sign extends HttpServlet {

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
		String str=request.getParameter("data");
		System.out.println(str);
		try {
			JSONObject obj = new JSONObject(str);
			String TruckLicenseTag=obj.getString("TruckLicenseTag");   //³µºÅ
			String DriverNo=obj.getString("user_no");         //  user_noÓÃ»§±àºÅ¾ÍÊÇµÇÂ¼±àºÅ±àºÅ
			String user_name=obj.getString("user_name");
			String fage=obj.getString("fage");
			String  a = Operation_update.Driver_Sign_in(GetConnection.getStoreConn(),TruckLicenseTag, DriverNo,fage,user_name);
			out.print("{\"resultStatus\":\"" + a + "\"}");
			System.out.println("{\"resultStatus\":\"" + a + "\"}");
		} catch (JSONException e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
