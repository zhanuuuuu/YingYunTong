package com.qian.xi.function;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import DB.DB;
import DB.GetConnection;
import Tool.GetcSheetno;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

@WebServlet(description = "提交千禧配送验货单", urlPatterns = { "/Upload_Qian_Xi_Pei_Song_Chu_ku_Dan" })
public class Upload_Qian_Xi_Pei_Song_Chu_ku_Dan extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn = GetConnection.getStoreConn();
		String data = request.getParameter("data");
		System.out.println(data);
		String strrandom = "";
		double sum = 0;
		try {
			JSONArray array = new JSONArray(data);
			JSONObject obj = array.getJSONObject(0);
			conn.setAutoCommit(false);
			PreparedStatement past_judge = conn.prepareStatement("select * from wh_cStoreOutWarehouse where cSheetno=? and bReceive='1' ");
			past_judge.setString(1, obj.getString("cSheetno"));
			ResultSet rs = past_judge.executeQuery();
			if (rs.next()) {
				
				out.print("{\"resultStatus\":\"" + 2 + "\"" + "}");
				out.flush();
				DB.closeResultSet(rs);
				DB.closePreparedStatement(past_judge);
				conn.commit();
				conn.setAutoCommit(true);
				return;
			}
			
			//此处用于单号的生成
			strrandom = GetcSheetno.getfen_dian_yan_zong_bu_no(conn, String_Tool.DataBaseYear(),obj.getString("cCustomerNo"));
			PreparedStatement past_detail = conn.prepareStatement(
					"INSERT INTO wh_StockVerifyDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit,fQuantity_stock) values (?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past_detail.setString(1, "" + strrandom);
				past_detail.setString(2, "" + (i + 1));
				past_detail.setString(3, obj1.getString("cGoodsName"));
				past_detail.setString(4, obj1.getString("cBarcode"));
				past_detail.setString(5, obj1.getString("Now_Num"));
				past_detail.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				double fInMoney = Double.parseDouble(obj1.getString("fInMoney"));
				sum = sum + fInMoney;
				past_detail.setString(7, "" + String_Tool.String_IS_Two(fInMoney));
				past_detail.setString(8, String_Tool.DataBaseTime());
				past_detail.setString(9, obj1.getString("cGoodsNo"));
				past_detail.setString(10, obj1.optString("cUnit"));//
				past_detail.setString(11, obj1.getString("fQuantity"));//
				past_detail.addBatch();
				if (i % 200 == 0) {
					past_detail.executeBatch();
				}
			}
			past_detail.executeBatch();
			DB.closePreparedStatement(past_detail);


			PreparedStatement past = conn.prepareStatement("INSERT INTO wh_StockVerify  (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,cWhNo,cWh,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,cSheetno_stock,cSupplierNo,cSupplier,fMoney,bExamin,bPeisong) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cCustomerNo"));
			past.setString(4, obj.getString("cCustomer"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));//
			past.setString(8, obj.optString("cwh"));//
			past.setString(9, String_Tool.DataBaseYear_Month_Day()); // 
			past.setString(10, String_Tool.DataBaseH_M_S()); //
			past.setString(11, String_Tool.DataBaseH_M_S()); // 
			past.setString(12, obj.getString("cOperatorNo")); // 
			past.setString(13, obj.getString("cOperator")); // 
			past.setString(14, obj.getString("cSheetno")); // 
			past.setString(15, obj.getString("cStoreNo")); // 
			past.setString(16, obj.getString("cStoreName")); // 
			past.setString(17, "" + sum);
			past.setString(18, "0");
			past.setString(19, "1");
			past.execute();
			DB.closePreparedStatement(past);
			
			PreparedStatement past_U = conn.prepareStatement("update wh_cStoreOutWarehouse set bReceive ='1' where cSheetno=? ");
			past_U.setString(1, obj.getString("cSheetno"));
			past_U.executeUpdate();
			DB.closePreparedStatement(past_U);
			
			out.print("{\"resultStatus\":\"" + 1 + "\""+ "}");
			
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			try {
				conn.rollback();
				out.print("{\"resultStatus\":\"" + 0 + "\""+ "}");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DB.closeConn(conn);
			out.flush();
			out.close();
		
		}
	}
}
