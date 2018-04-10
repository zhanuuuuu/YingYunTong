package com.simplestore;
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

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.ReadConfig;
import Tool.String_Tool;

@WebServlet(description = "单店提交损益", urlPatterns = { "/Simple_Upload_Sun_Yi" })
public class Simple_Upload_Sun_Yi extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data");
		String fage=request.getParameter("fage");
		try {
			int a=0;
			JSONArray array=new JSONArray(data);
			if(fage.equals("0")){
				a=Insert_into_Bao_Sun(GetConnection.getStoreConn(),array);
			}
			else{
				a=Insert_into_Bao_Yi(GetConnection.getStoreConn(),array);
			}
			out.print("{\"resultStatus\":\"" + a + "\"}");
			LoggerUtil.info("{\"resultStatus\":\"" + a + "\"}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + -1 + "\"}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
	
	
	public  int Insert_into_Bao_Sun(Connection conn, JSONArray array) { // 分店验单
		PreparedStatement past = null;
		String strrandom = null;
		double sum = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			PreparedStatement past_cSheetno = conn.prepareStatement("select SheetNo=" + new ReadConfig().getprop().getProperty("DataSource") + ".dbo.f_GenBaoSunSheetno('"+ String_Tool.DataBaseYear() + "','" + obj.getString("cSupplierNo") + "')");
			ResultSet rs1 = past_cSheetno.executeQuery();
			if (rs1.next()) {
				strrandom = rs1.getString("SheetNo");
			}
			LoggerUtil.info(strrandom);
			DB.closeResultSet(rs1);
			DB.closePreparedStatement(past_cSheetno);
			
			LoggerUtil.info(strrandom);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement past1 = conn.prepareStatement(
						"INSERT INTO  wh_LossWarehouseDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fPrice,fMoney,dProduct,cGoodsNo,cUnit) values (?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				double fInMoney = Double.parseDouble(obj1.getString("fMoney"));
				sum = sum + fInMoney;
				past1.setString(7, "" + fInMoney);
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			past = conn.prepareStatement(
					"INSERT INTO  wh_LossWarehouse  (dDate,cSheetno,cSupplierNo,cSupplier,cOperatorNo,cOperator,cWhNo,cWh,dFillin,cFillinTime,cFillEmpNo,cFillEmp,fMoney,cTime) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cSupplierNo"));
			past.setString(4, obj.getString("cSupName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.getString("cWhNo"));
			past.setString(8, obj.getString("cWh"));
			past.setString(9, String_Tool.DataBaseYear_Month_Day());
			past.setString(10, String_Tool.DataBaseH_M_S()); //
			past.setString(11, obj.getString("cOperatorNo")); //
			past.setString(12, obj.getString("cOperator")); //
			past.setString(13, "" + sum);
			past.setString(14, "" + String_Tool.DataBaseH_M_S());
			past.execute();
			conn.commit();
			conn.setAutoCommit(true);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
				return 0;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return 0;
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
	}
	
	public  int Insert_into_Bao_Yi(Connection conn, JSONArray array) { // 分店验单
		PreparedStatement past = null;
		String strrandom = null;
		double sum = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			PreparedStatement past_cSheetno = conn.prepareStatement("select SheetNo=" + new ReadConfig().getprop().getProperty("DataSource") + ".dbo.f_GenSunYiSheetno('"+ String_Tool.DataBaseYear() + "','" + obj.getString("cSupplierNo") + "')");
			ResultSet rs1 = past_cSheetno.executeQuery();
			if (rs1.next()) {
				strrandom = rs1.getString("SheetNo");
			}
			DB.closeResultSet(rs1);
			DB.closePreparedStatement(past_cSheetno);
			
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement past1 = conn.prepareStatement("INSERT INTO  dbo.wh_EffusionWhDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit) values (?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				double fInMoney = Double.parseDouble(obj1.getString("fMoney"));
				sum = sum + fInMoney;
				past1.setString(7, "" + fInMoney);
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			past = conn.prepareStatement(
					"INSERT INTO   dbo.wh_EffusionWh  (dDate,cSheetno,cSupplierNo,cSupplier,cOperatorNo,cOperator,cWhNo,cWh,dFillin,cFillinTime,cFillEmpNo,cFillEmp,fMoney,cTime) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cSupNo"));
			past.setString(4, obj.getString("cSupName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.getString("cWhNo"));
			past.setString(8, obj.getString("cWh"));
			past.setString(9, String_Tool.DataBaseYear_Month_Day());
			past.setString(10, String_Tool.DataBaseH_M_S()); //
			past.setString(11, obj.getString("cOperatorNo")); //
			past.setString(12, obj.getString("cOperator")); //
			past.setString(13, "" + sum);
			past.setString(14, "" + String_Tool.DataBaseH_M_S());
			past.execute();
			conn.commit();
			conn.setAutoCommit(true);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
				return 0;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return 1;
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
	}
}
