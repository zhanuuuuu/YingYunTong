package Fen_dian_yan_shou;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import DB.DBYan_Huo_update;
import DB.DBupdate;
import DB.Fen_dian_Update;
import DB.GetConnection;

public class Diao_bo_chu_ku_Select extends HttpServlet {

	
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
		System.out.println(str);
		
		JSONArray array = DBYan_Huo_update.diao_bo_chu_ku_Select(GetConnection.getStoreConn(),str);
		
		if (array != null && array.length() > 0) {
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
					+ array.toString().replace(" ", "") + "}");
		}
		else {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
					+ array.toString() + "}");
		}

		System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
				+ array.toString() + "}");

		out.flush();
		out.close();
	}

}
