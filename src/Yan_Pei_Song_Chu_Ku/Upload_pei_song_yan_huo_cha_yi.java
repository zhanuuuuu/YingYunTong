package Yan_Pei_Song_Chu_Ku;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import DB.DBYan_Huo_update;
import DB.GetConnection;

public class Upload_pei_song_yan_huo_cha_yi extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
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
		String data = request.getParameter("data").replace(' ', '+').replace("\n", "").replace("\r", "");
		System.out.println(data);
		System.out.println("≈‰ÀÕ—Èªı≤Ó“Ï");
//		try {
//			JSONArray array=new JSONArray(data);
//			System.out.println(array.toString());
//			//boolean a = DBYan_Huo_update.insert_into_yan_huo_cha_yi_dan(GetConnection.getStoreConn(), array);
//			if (a) {
//				out.print("{\"resultStatus\":\"" + 1 + "\""+ "}");
//			} 
//			else {
//				out.print("{\"resultStatus\":\"" + 0 + "\""+ "}");
//			}
//			System.out.println("{\"resultStatus\":\"" + 1 + "\""+ "}");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		out.flush();
		out.close();
	}

}
