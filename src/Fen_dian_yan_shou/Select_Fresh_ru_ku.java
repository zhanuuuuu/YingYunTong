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
import DB.Fen_dian_Update;
import DB.GetConnection;

public class Select_Fresh_ru_ku extends HttpServlet {

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
		String data = request.getParameter("data");
		try {
			JSONObject obj = new JSONObject(data);
			String No = obj.getString("No");
			String fage = obj.getString("fage");
			JSONArray array = Fen_dian_Update.select_Fresh_ru_ku(
					GetConnection.getStoreConn(), No, fage);
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
			out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dDate\":"
					+ null + "}");
		}
		out.flush();
		out.close();
	}

}
