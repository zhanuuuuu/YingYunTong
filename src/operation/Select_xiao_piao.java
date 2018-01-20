package operation;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import DB.GetConnection;
import DB.Operation_update;

public class Select_xiao_piao extends HttpServlet {

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
		System.out.println(data);
		try {
			JSONObject obj = new JSONObject(data);
			String cGoods = obj.getString("cGoods");
			String cStoreNo = obj.getString("AppNo");
			String startdate = obj.getString("dData");
			String enddate = obj.getString("dData1");
			JSONArray array = Operation_update.Select_Xiao_Piao(
					GetConnection.getStoreConn(), cGoods, cStoreNo, startdate,
					enddate);
			if(array!=null&&array.length()>0){
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString().replace(" ", "") + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString() + "}");
			}

		} catch (Exception e) {
		   e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
