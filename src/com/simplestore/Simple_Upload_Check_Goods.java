package com.simplestore;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import DB.DB;
import DB.GetConnection;
import DB.Head_Shop;

@WebServlet(description = "查询总部所有的信息", urlPatterns = { "/Simple_Upload_Check_Goods" })
public class Simple_Upload_Check_Goods extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("name");
		System.out.println(data);
		try {
			JSONArray array=new JSONArray(data);
			int str=head_shop_Check_Goods(GetConnection.getStoreConn(),array);
			out.print("{\"resultStatus\":\"" + str + "\"," + "\"dDate\":\"" +"" + "\"}");
			System.out.println("{\"resultStatus\":\"" + str + "\"," + "\"dDate\":\"" +"" + "\"}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
	
	
	public int head_shop_Check_Goods(Connection conn, JSONArray data) {
		Statement past = null;
		try {
			conn.setAutoCommit(false);
			String sql = "if (select OBJECT_ID('tempdb..#temp_CheckList'))is not null drop table #temp_CheckList create table #temp_CheckList(dPiCiDate datetime,cSheetNO varchar(32),iLineNo int IDENTITY(1,1) ,cGoodsNo varchar(32),fQuantity money,finprice money,cOperator varchar(32),cOperatorNo varchar(32),cSupplierNo varchar(32),cZoneNo varchar(32))";
			past = conn.createStatement();
			past.addBatch(sql);
			for (int i = 0; i < data.length(); i++) {
				JSONObject obj = data.getJSONObject(i);
				sql = "INSERT INTO #temp_CheckList (cGoodsNo,fQuantity,finprice,cOperator,cOperatorNo,cSupplierNo,cZoneNo) values('"
						+ obj.getString("cGoodsNo") + "'" + "," + "'" + obj.getString("fQuantity") + "'" + "," + "'"
						+ obj.getString("fNormalPrice") + "'" + "," + "'" + obj.getString("cOperator") + "'" + "," + "'"
						+ obj.getString("cOperatorNo") + "'" + "," + "'" + obj.getString("cCheckTaskNo") + "'" + ","
						+ "'" + obj.getString("cZoneNo") + "'" + ")";
				past.addBatch(sql);
				if (i % 200 == 0) {
					past.executeBatch();
				}
			}
			past.executeBatch();
			CallableStatement c = conn.prepareCall("{ call check_goods () } ");
			ResultSet rs = c.executeQuery();
			String rtn = "0";
			while (rs.next()) {
				rtn = rs.getString("rtn");
			}
			if (rtn.equals("1")) {
				conn.commit();
				conn.setAutoCommit(true);
				return 1;
			} else {
				conn.rollback();
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return -1;
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
	}
}
