package Fen_dian_yan_shou;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DBYan_Huo_update;
import DB.GetConnection;

public class upload_men_dian_zhi_pei extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

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
		LoggerUtil.info(""+data);
		try {
			JSONArray array = new JSONArray(data);
			boolean a = DBYan_Huo_update.insert_into_men_dian_zhi_pei(GetConnection.getStoreConn(), array);
			if (a) {
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\""+ "}");
				out.print("{\"resultStatus\":\"" + 1 + "\""+ "}");
			} 
			else {
				LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\""+ "}");
				out.print("{\"resultStatus\":\"" + 0 + "\""+ "}");
			}
		} catch (Exception e) {
			LoggerUtil.info("{\"resultStatus\":\"" + "�������쳣" + "\""+ "}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
