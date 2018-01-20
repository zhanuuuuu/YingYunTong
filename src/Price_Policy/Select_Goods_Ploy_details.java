package Price_Policy;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import Tool.String_Tool;

import DB.DB_Price_Policy;
import DB.DBupdate;
import DB.GetConnection;

public class Select_Goods_Ploy_details extends HttpServlet {

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
			String date1 = obj.getString("dDate1");
			String date2 = obj.getString("dDate2");
			String cStoreNo = obj.getString("cStoreNo");
			String cGoodsNo = obj.optString("cGoodsNo");
			String cPloyNo  = obj.optString("cPloyNo");
			System.out.println(cStoreNo+cGoodsNo+cPloyNo );
			JSONArray array=null;
			if(!String_Tool.isEmpty(cGoodsNo)){
				array = DB_Price_Policy.Select_GoodsPloy(
						GetConnection.getStoreConn(), date1, date2, cStoreNo,
						cGoodsNo);
			}
			else if(String_Tool.isEmpty(cPloyNo)){
				array = DB_Price_Policy.Select_GoodsPloy(
    					GetConnection.getStoreConn(), cStoreNo);
			}
			
			else if(!String_Tool.isEmpty(cPloyNo)){
				array = DB_Price_Policy.Select_GoodsPloy(
    					GetConnection.getStoreConn(), cStoreNo,cPloyNo);
				System.out.println("大所發生的");
			}
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString().replace(" ", "") + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ array.toString() + "}");
			}
			System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
						+ array.toString() + "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
