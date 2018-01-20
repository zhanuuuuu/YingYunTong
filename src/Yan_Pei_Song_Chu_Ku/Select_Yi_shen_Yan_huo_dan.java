package Yan_Pei_Song_Chu_Ku;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import DB.DBYan_Huo_update;
import DB.DBupdate;
import DB.GetConnection;

public class Select_Yi_shen_Yan_huo_dan extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
	    doPost(request, response);
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
		try {
			JSONArray array = DBYan_Huo_update.Select_Yi_Shen_yan_huo_dan(GetConnection.getStoreConn());
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
