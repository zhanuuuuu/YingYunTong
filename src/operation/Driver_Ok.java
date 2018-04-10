package operation;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;
import DB.Operation_update;

public class Driver_Ok extends HttpServlet {

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
		String str = request.getParameter("data");
		LoggerUtil.info(str);
		try {
			JSONObject obj = new JSONObject(str);
			String UserNo=obj.getString("UserNo");
			String DriverName = obj.getString("DriverName");
			String Store_no=obj.getString("Store_no");
			String Store_name = obj.getString("Store_name");
			String cSheetno=obj.getString("cSheetno");
			String a = Operation_update.OK_Send(GetConnection.getStoreConn(),UserNo,DriverName, Store_no,Store_name,cSheetno);
			out.print("{\"resultStatus\":\"" + a + "\"}");
			LoggerUtil.info("{\"resultStatus\":\"" + a + "\"}");
		} catch (JSONException e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
