package Goods_flow_Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import DB.DBupdate;
import DB.GetConnection;
import Fen_dian_yan_shou.Upload_Fen_dian_ru_ku;

public class Upload_Request_Order extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data");
		System.out.println(data);
		try {
			JSONObject obj = new JSONObject(data);
			JSONArray array = obj.getJSONArray("dData");
			System.out.println(array.toString());
			boolean a = DBupdate.insert_into_group_Good(GetConnection.getStoreConn(), array);
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\"" + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
			}
		} catch (Exception e) {

			out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
			Logger.getLogger(Upload_Request_Order.class).error(e.getLocalizedMessage() + data);
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}