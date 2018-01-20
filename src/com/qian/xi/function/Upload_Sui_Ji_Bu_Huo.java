package com.qian.xi.function;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import DB.DB;
import DB.DBYan_Huo_update;
import DB.GetConnection;
import Tool.String_Tool;

@WebServlet(description = "提交随机补货", urlPatterns = { "/Upload_Sui_Ji_Bu_Huo" })
public class Upload_Sui_Ji_Bu_Huo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data");
		System.out.println(data);
		try {
			JSONArray array=new JSONArray(data);
			boolean a = insert_into_suiji_buhuo(GetConnection.getStoreConn(), array);
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\""+ "}");
			} 
			else {
				out.print("{\"resultStatus\":\"" + 0 + "\""+ "}");
			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\""+ "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
	
	public static boolean insert_into_suiji_buhuo(Connection conn, JSONArray array) { // 打印价签
		PreparedStatement past = null;
		String strrandom;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = "Random"+obj.getString("cStoreNo") + String_Tool.reformat(); // 单号
			System.out.println(strrandom);
			String sql = "INSERT INTO t_Random_WH_BhApply (dDate,cStoreNo,cSheetNo,cStoreName,cOperatorNo,cOperator,bPrint)values(?,?,?,?,?,?,?)";
			past = conn.prepareStatement(sql);
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, obj.getString("cStoreNo"));
			past.setString(3, strrandom);
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, "0");
			past.execute();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement past1 = conn.prepareStatement(
						"INSERT INTO t_Random_WH_BhApplyDetail (cSheetNo,iLineNo,cGoodsNo,fQuantity) values (?,?,?,?)");
				past1.setString(1, strrandom);
				past1.setString(2, "" + i);
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("fQuantity"));
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			try {
				conn.rollback();
				e.printStackTrace();
				return false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return false;
	}
}
