package Goods_flow_Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import DB.DBupdate;
import DB.GetConnection;

public class Select_ALL_t_Pallet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter(); 
		doPost(request,response);
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
		JSONArray array = null;
		if (!str.equals("")&&str != null) {
			array = DBupdate.Select_ALL_Pallet(GetConnection
					.getStoreConn(),str);
			System.out.println("¹þ¹þ¹þ");
		}
		else {
			array = DBupdate.Select_ALL_Pallet(GetConnection
					.getStoreConn());
		}
		if (array != null && array.length() > 0) {
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
					+ array.toString().replace(" ", "") + "}");
		} else {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
					+ array.toString() + "}");
		}

		System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
				+ array.toString() + "}");

		out.flush();
		out.close();
	}

}
