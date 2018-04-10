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

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.String_Tool;

@WebServlet(description = "提交试扫商品", urlPatterns = { "/Upload_Try_Sweep_Goods" })
public class Upload_Try_Sweep_Goods extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data_have");
		String data1=request.getParameter("data_nothave");
		LoggerUtil.info(data);
		LoggerUtil.info(data1);
		try {
			JSONArray array=new JSONArray(data);
			JSONArray array1=new JSONArray(data1);
			boolean a = Try_Sweep_Goods(GetConnection.getStoreConn(), array,array1);
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\""+ "}");
			} 
			else {
				out.print("{\"resultStatus\":\"" + 0 + "\""+ "}");
			}
			LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\""+ "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
	
	public static boolean Try_Sweep_Goods(Connection conn, JSONArray array,JSONArray array1) { // 打印价签
		PreparedStatement past = null;
		String strrandom;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = "try_sw"+obj.getString("cStoreNo") + String_Tool.reformat(); // 单号
			LoggerUtil.info(strrandom);
			String sql = "INSERT INTO t_Try_Sweep_Goods (dDate,cStoreNo,cSheetNo,cStoreName,cOperatorNo,cOperator,bPrint)values(?,?,?,?,?,?,?)";
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
						"INSERT INTO t_Try_Sweep_Goods_haveDetail (cSheetNo,iLineNo,cGoodsNo,cBarcode,cGoodsName,fNormalPrice,bPrint) values (?,?,?,?,?,?,?)");
				past1.setString(1, strrandom);
				past1.setString(2, "" + i);
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("cGoodsName"));
				past1.setString(6, obj1.getString("fNormalPrice"));
				past1.setString(7, "0");
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			for (int i = 0; i < array1.length(); i++) {
				JSONObject obj1 = array1.getJSONObject(i);
				PreparedStatement past1 = conn.prepareStatement(
						"INSERT INTO t_Try_Sweep_Goods_nothaveDetail (cSheetNo,iLineNo,cBarcode,fNormalPrice,bPrint) values (?,?,?,?,?)");
				past1.setString(1, strrandom);
				past1.setString(2, "" + i);
				past1.setString(3, obj1.getString("cBarcode"));
				past1.setString(4, obj1.getString("fNormalPrice"));
				past1.setString(5, "0");
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
