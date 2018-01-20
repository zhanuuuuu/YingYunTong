package Fen_dian_yan_shou;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import Tool.ResultSet_To_JSON;

import DB.DB;
import DB.GetConnection;

public class Select_t_cStoreGoods_Message extends HttpServlet {
	

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
		String data=request.getParameter("data");
		System.out.println(data);
		Connection conn=null;
		PreparedStatement past=null;
		ResultSet rs=null;
		try { 
			JSONObject obj=new JSONObject(data);
			String cStoreNo=obj.getString("cStore_No");
			String cGoodsNo=obj.getString("cGoodsNo");
			conn=GetConnection.getStoreConn();
			past=conn.prepareStatement("select * from t_cStoreGoods where cStoreNo=? and cGoodsNo=?");
            past.setString(1, cStoreNo);
            past.setString(2, cGoodsNo);
            rs=past.executeQuery();
			JSONArray array =ResultSet_To_JSON.resultSetToJsonArray(rs);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ array.toString() + "}");
			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\","
					+ "\"dDate\":" + array.toString() + "}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
					+ "" + "}");
			e.printStackTrace();
		}
		finally{
			DB.closeRs_Con(rs,past,conn);
		}

		out.flush();
		out.close();
	}

}
