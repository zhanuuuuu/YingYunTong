package Fen_dian_yan_shou;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import DB.Fen_dian_Update;
import DB.GetConnection;

public class SelectcSheetno extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

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
		try {
			String cStoreNo = request.getParameter("cStoreNo");
			String dDate1 = request.getParameter("dDate1");
			String dDate2 = request.getParameter("dDate2");
			String fage = request.getParameter("fage"); // "0是报损所有单 1是报益所有单"
			if (fage.equals("0")) {
				JSONArray array = Fen_dian_Update.Select_Bao_Sun_Dan(GetConnection.getStoreConn(), cStoreNo,dDate1,dDate2);
				if (array != null && array.length() > 0) {
					out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
							+ array.toString() + "}");
					System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
							+ array.toString() + "}");
				} else {
					out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
							+ array.toString() + "}");
					System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
							+ array.toString() + "}");
				}
			} else {
				JSONArray array = Fen_dian_Update.Select_Bao_Yi_Dan(
						GetConnection.getStoreConn(), cStoreNo,dDate1,dDate2);

				if (array != null && array.length() > 0) {
					out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
							+ array.toString() + "}");
					System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
							+ array.toString() + "}");
				} else {
					out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
							+ array.toString() + "}");
					System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ array.toString() + "}");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
