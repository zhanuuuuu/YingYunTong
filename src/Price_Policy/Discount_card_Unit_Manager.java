package Price_Policy;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

public class Discount_card_Unit_Manager extends HttpServlet {

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
		Connection conn = GetConnection.getStoreConn();
		String data = request.getParameter("data");
		System.out.println(data);
		CallableStatement c = null;
		try {
			JSONObject obj = new JSONObject(data);
			c = conn.prepareCall("{call p_UpcStoreGoodsDazhe (?,?,?,?,?,?)}");
			c.setString(1, obj.getString("cStoreNo"));
			c.setString(2, obj.getString("cGoodsNo"));
			c.setString(3, obj.getString("bDazhe"));
			c.setString(4, obj.getString("cUnit"));
			c.setString(5, obj.getString("cOpertNo"));
			c.setString(6, obj.getString("cOpertName"));
			c.execute();
			out.print("{\"resultStatus\":\"" + 1 + "\"" + "}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(c, conn);
		}
	

	}
}
