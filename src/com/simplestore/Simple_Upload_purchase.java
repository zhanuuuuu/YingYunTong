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

import DB.DB;
import DB.GetConnection;
import Tool.ReadConfig;
import Tool.String_Tool;

@WebServlet(description = "单店提交提交采购", urlPatterns = { "/Simple_Upload_purchase" })
public class Simple_Upload_purchase extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("name");
		System.out.println(data);
		Connection conn = null;
		String SheetNo = null;
		double fMoney = 0;
		try {
			JSONArray array = new JSONArray(data);
			conn = GetConnection.getStoreConn();
			conn.setAutoCommit(false);

			JSONObject obj = array.getJSONObject(0);

			PreparedStatement past1 = conn.prepareStatement(
					"select SheetNo=" + new ReadConfig().getprop().getProperty("DataSource") + ".dbo.f_GenCaiGousheetno('"+ String_Tool.DataBaseYear() + "','" + obj.getString("cSupplierNo") + "')");
			ResultSet rs1 = past1.executeQuery();
			if (rs1.next()) {
				SheetNo = rs1.getString("SheetNo");
			}
			System.out.println(SheetNo);
			DB.closeResultSet(rs1);
			DB.closePreparedStatement(past1);

			PreparedStatement past_detail = conn.prepareStatement(
					"insert into WH_StockDetail(cSheetno,iLineNo,cGoodsNo,cGoodsName,cBarcode,cUnitedNo,fQuantity,fInPrice,fInMoney,fTaxrate,bTax,fTaxPrice,fTaxMoney,fNoTaxPrice,fNoTaxMoney,cUnit,cSpec,fQty_Cur,fPacks)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past_detail.setString(1, SheetNo);
				past_detail.setString(2, "" + i);
				past_detail.setString(3, obj1.getString("cGoodsNo"));

				/* 取售价 */
				String fNormalPrice = "0.0";
				PreparedStatement past2 = conn.prepareStatement("select fNormalPrice from t_goods where cGoodsNo=?   ");
				past2.setString(1, obj1.getString("cGoodsNo"));
				ResultSet rs2 = past2.executeQuery();
				if (rs2.next()) {
					fNormalPrice = rs2.getString("fNormalPrice");
				}
				DB.closeResultSet(rs2);
				DB.closePreparedStatement(past2);

				past_detail.setString(4, obj1.getString("cGoodsName"));
				past_detail.setString(5, obj1.getString("cBarcode"));
				past_detail.setString(6, "0");// obj1.getString("cUnitedNo")
				past_detail.setString(7, obj1.getString("fQuantity"));
				
				
				System.out.println(obj1.getString("fQuantity"));
				System.out.println(obj1.getString("fInPrice"));
				System.out.println(obj1.getString("fInMoney"));
				
				
				past_detail.setString(8, obj1.getString("fInPrice"));
				past_detail.setString(9, obj1.getString("fInMoney"));
				double fInMoney = Double.parseDouble(obj1.getString("fInMoney"));
				fMoney = fInMoney + fMoney;
				double fTaxrate = (Double.parseDouble(fNormalPrice) - Double.parseDouble(obj1.getString("fInPrice")))/ Double.parseDouble(obj1.getString("fInPrice"));
				past_detail.setString(10, String_Tool.String_IS_Two(fTaxrate));
				System.out.println(String_Tool.String_IS_Two(fTaxrate));
				past_detail.setString(11, "1");
				String fTaxPrice = ""+ (Double.parseDouble(fNormalPrice) - Double.parseDouble(obj1.getString("fInPrice")));
				past_detail.setString(12, "" + String_Tool.String_IS_Two(fTaxPrice));// obj1.getString("fTaxPrice")
				System.out.println(String_Tool.String_IS_Two(fTaxPrice));
				String fTaxMoney = ""+ (Double.parseDouble(fTaxPrice) * Double.parseDouble(obj1.getString("fQuantity")));
				past_detail.setString(13, String_Tool.String_IS_Two(fTaxMoney));// obj1.getString("fTaxMoney")
				System.out.println(String_Tool.String_IS_Two(fTaxMoney));
				past_detail.setString(14, String_Tool.String_IS_Two(fNormalPrice));// obj1.getString("fNoTaxPrice")
				System.out.println(String_Tool.String_IS_Two(fNormalPrice));
				past_detail.setString(15,
						"" + String_Tool.String_IS_Two((Double.parseDouble(fNormalPrice) * Double.parseDouble(obj1.getString("fQuantity")))));// obj1.getString("fNoTaxMoney")
				System.out.println(String_Tool.String_IS_Two((Double.parseDouble(fNormalPrice) * Double.parseDouble(obj1.getString("fQuantity")))));
				past_detail.setString(16, obj1.optString("cUnit"));
				past_detail.setString(17, obj1.optString("cSpec"));
				past_detail.setString(18, obj1.optString("fQty_Cur"));
				past_detail.setString(19, "1");// obj1.getString("fPacks")
				past_detail.addBatch();
			}
			past_detail.executeBatch();
			DB.closePreparedStatement(past_detail);

			PreparedStatement past = conn.prepareStatement(
					"insert into WH_Stock(dDate,cSheetNO,cSupplierNo,cSupplier,cOperatorNo,cOperator,cFillEmpNo,cFillEmp,dFillin,cFillinTime,iDays,cStockDptno,cStockDpt,fMoney,cWhNo,cWh,dDeadLine,cTime)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseTime());
			past.setString(2, SheetNo);// obj.getString("cSheetNO")
			past.setString(3, obj.getString("cSupplierNo"));
			past.setString(4, obj.getString("cSupplier"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.getString("cOperatorNo"));
			past.setString(8, obj.getString("cOperator"));
			past.setString(9, String_Tool.DataBaseTime());
			past.setString(10, String_Tool.DataBaseH_M_S());// obj.getString("cFillinTime")
			past.setString(11, "");// obj.getString("iDays")
			past.setString(12, "");// obj.getString("cStockDptno")
			past.setString(13, "");// obj.getString("cStockDpt")
			past.setString(14, "" + String_Tool.String_IS_Two(fMoney));// obj.getString("fMoney")
			past.setString(15, obj.getString("cWhNo"));
			past.setString(16, obj.getString("cWh"));
			past.setString(17, String_Tool.DataBaseTime());// obj.getString("dDeadLine")
			past.setString(18, String_Tool.DataBaseH_M_S());// obj.getString("cTime")
			past.execute();
			DB.closePreparedStatement(past);
			out.print("{\"resultStatus\":\"" + 1 + "\"}");
			System.out.println("{\"resultStatus\":\"" + 1 + "\"}");
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			try {
				out.print("{\"resultStatus\":\"" + 0 + "\"}");
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}
}
