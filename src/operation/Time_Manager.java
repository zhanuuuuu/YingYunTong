package operation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import Tool.String_Tool;

import DB.DB;
import DB.GetConnection;

public class Time_Manager extends HttpServlet {

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
		Connection conn =null;
		ResultSet rs = null;
		String cStoreNo = null;
		PreparedStatement past = null;
		try {
			conn=GetConnection.getStoreConn();
			JSONObject obj=new JSONObject(data);
			String cstoreNo=obj.getString("cStoreNo");
			String fage=obj.getString("fage");
			if(fage.equals("0")){
				past = conn
					.prepareStatement("select cStoreNo from t_Store where cStoreNo=? and dbo.getTimeStr(GETDATE()) between bgntime and endtime");
			}
			else{
				past = conn
						.prepareStatement("select cStoreNo from t_Store where cStoreNo=? and dbo.getTimeStr(GETDATE()) between bgntime and DATEADD(minute , LastMin, endtime)");
			}
			past.setString(1, cstoreNo);
			rs = past.executeQuery();
			while (rs.next()) {
				cStoreNo = rs.getString("cStoreNo");
			}
			if (String_Tool.isEmpty(cStoreNo)) {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"cStoreNo\":\""
						+ cStoreNo + "\"}");
			} else {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"cStoreNo\":\""
						+ cStoreNo + "\"}");
			}
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"cStoreNo\":\""
					+ cStoreNo + "\"}");
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		out.flush();
		out.close();
	}

}
