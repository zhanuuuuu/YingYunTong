package Fen_dian_yan_shou;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import DB.DBYan_Huo_update;
import DB.GetConnection;

public class Select_ru_ku_dan_Googs extends HttpServlet {


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
			String fage = obj.getString("fage");
			JSONArray array = null;
			if (fage.equals("0")) {
				String cStoreNo = obj.getString("cStoreNo");
				String dDate1 = obj.getString("dDate1");
				String dDate2 = obj.getString("dDate2");
				array = DBYan_Huo_update.select_ru_ku_goods(GetConnection.getStoreConn(), cStoreNo, dDate1, dDate2);
			} else {
				String No = obj.getString("No");
				array = DBYan_Huo_update.select_ru_ku_goods(GetConnection.getStoreConn(), No);
			}
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
						+ array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
						+ array.toString() + "}");
			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\","
					+ "\"dDate\":" + array.toString() + "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
