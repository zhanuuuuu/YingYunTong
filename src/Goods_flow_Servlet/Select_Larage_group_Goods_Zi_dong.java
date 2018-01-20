package Goods_flow_Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import DB.DBupdate;
import DB.GetConnection;
import Tool.String_Tool;

public class Select_Larage_group_Goods_Zi_dong extends HttpServlet {

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
		// @cStoreNo varchar(32), --门店号
		// @dDate1 datetime, --开始日期2
		// @dDate2 datetime, --结束日期2
		// @cWhNo varchar(32), -- 门店仓库号
		// @GroupTypeNo varchar(32)

		try {
			JSONObject obj = new JSONObject(data);
			String cStoreNo = obj.getString("cStoreNo");
			String dDate1 = obj.optString("dDate1");
			String dDate2 = obj.optString("dDate2");
			String cWhNo = obj.getString("cWhNo");
			String GroupTypeNo = obj.getString("GroupTypeNo").replace("[", ",").replace("]", ",").replace(" ", "");
			JSONArray array = null;
			array = DBupdate.Select_Larage_group_Good_Zi_dong(GetConnection.getStoreConn(), cStoreNo, dDate1, dDate2,cWhNo,GroupTypeNo);

			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ "0" + "}");
			}
			System.out.println(array.toString());
	
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
