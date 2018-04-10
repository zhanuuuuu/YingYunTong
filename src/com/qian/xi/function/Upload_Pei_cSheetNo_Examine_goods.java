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

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.GetcSheetno;
import Tool.ReadConfig;
import Tool.String_Tool;


@WebServlet(description = "提交带单子的验货", urlPatterns = { "/Upload_Pei_cSheetNo_Examine_goods" })
public class Upload_Pei_cSheetNo_Examine_goods extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data");
		String cSheetno=request.getParameter("cSheetno");
		String cSheetStateNo=request.getParameter("cSheetStateNo");
	
		LoggerUtil.info(data);
		LoggerUtil.info(cSheetno);
		LoggerUtil.info(cSheetStateNo);
		Connection conn = null;
		String SheetNo = null;
		double fMoney = 0;
		try {
			JSONArray array = new JSONArray(data);
			conn = GetConnection.getStoreConn();
			conn.setAutoCommit(false);

			JSONObject obj = array.getJSONObject(0);

			/* 取单号正常验货单单号 */
			SheetNo = GetcSheetno.get_commonality_cSheetNo(conn,
					"select SheetNo=" + new ReadConfig().getprop().getProperty("DataSource") + ".dbo.f_GenYHsheetno('"
							+ String_Tool.DataBaseYear() + "','" + obj.getString("cSupplierNo") + "')");


			/* 插入供应商验货表 */
			PreparedStatement past_detail = conn.prepareStatement(
					"insert into WH_StockVerifyDetail(cSheetno,iLineNo,cGoodsNo,cGoodsName,cBarcode,cUnitedNo,fQuantity,fInPrice,fInMoney,fTaxrate,bTax,fTaxPrice,fTaxMoney,fNoTaxPrice,fNoTaxMoney,cUnit,cSpec,fPacks)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past_detail.setString(1, SheetNo);
				past_detail.setString(2, "" + i);
				past_detail.setString(3, obj1.getString("cGoodsNo"));

				/* 取售价 */
				String fNormalPrice = "0.0";
				// select fCKPrice from dbo.t_cStoreGoods      
				PreparedStatement past2 = conn.prepareStatement("select fNormalPrice from t_cStoreGoods where cStoreNo=?  and cGoodsNo=?   ");
				past2.setString(1, obj.getString("cStoreNo"));
				past2.setString(2, obj1.getString("cGoodsNo"));
				ResultSet rs2 = past2.executeQuery();
				if (rs2.next()) {
					fNormalPrice = rs2.getString("fNormalPrice");
				}
				DB.closeResultSet(rs2);
				DB.closePreparedStatement(past2);

				past_detail.setString(4, obj1.getString("cGoodsName"));
				past_detail.setString(5, obj1.getString("cBarcode"));
				past_detail.setString(6, "");// obj1.getString("cUnitedNo")
				past_detail.setString(7, obj1.getString("fQuantity"));

				past_detail.setString(8, obj1.getString("fInPrice"));
				past_detail.setString(9, obj1.getString("fInMoney"));
				double fInMoney = Double.parseDouble(obj1.getString("fInMoney"));
				fMoney = fInMoney + fMoney;
				double fTaxrate = (Double.parseDouble(fNormalPrice) - Double.parseDouble(obj1.getString("fInPrice")))
						/ Double.parseDouble(obj1.getString("fInPrice"));
				past_detail.setString(10, "" + fTaxrate);
				past_detail.setString(11, "1");
				String fTaxPrice = ""
						+ (Double.parseDouble(fNormalPrice) - Double.parseDouble(obj1.getString("fInPrice")));
				past_detail.setString(12, "" + fTaxPrice);// obj1.getString("fTaxPrice")
				String fTaxMoney = ""
						+ (Double.parseDouble(fTaxPrice) * Double.parseDouble(obj1.getString("fQuantity")));
				past_detail.setString(13, fTaxMoney);// obj1.getString("fTaxMoney")
				past_detail.setString(14, "" + fNormalPrice);// obj1.getString("fNoTaxPrice")
				past_detail.setString(15,
						"" + (Double.parseDouble(fNormalPrice) * Double.parseDouble(obj1.getString("fQuantity"))));// obj1.getString("fNoTaxMoney")
				past_detail.setString(16, obj1.optString("cUnit"));
				past_detail.setString(17, obj1.optString("cSpec"));
				past_detail.setString(18, "1");// obj1.getString("fPacks")
			//	past_detail.setString(19, obj1.getString("Original_fQuantity"));
				past_detail.addBatch();
			}
			past_detail.executeBatch();
			DB.closePreparedStatement(past_detail);

			PreparedStatement past = conn.prepareStatement(
					"insert into WH_StockVerify(cStoreNo,cStoreName,dDate,cSheetNO,cSupplierNo,cSupplier,cOperatorNo,cOperator,cFillEmpNo,cFillEmp,dFillin,cFillinTime,cStockDptno,cStockDpt,fMoney,cWhNo,cWh,cTime,cSheetno_stock,cSheetStateNo)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, obj.getString("cStoreNo"));//
			past.setString(2, obj.getString("cStoreName"));
			past.setString(3, String_Tool.DataBaseYear_Month_Day());
			past.setString(4, SheetNo);// obj.getString("cSheetNO");
			past.setString(5, obj.getString("cSupplierNo"));
			past.setString(6, obj.getString("cSupplier"));
			past.setString(7, obj.getString("cOperatorNo"));
			past.setString(8, obj.getString("cOperator"));
			past.setString(9, obj.getString("cOperatorNo"));
			past.setString(10, obj.getString("cOperator"));
			past.setString(11, String_Tool.DataBaseTime());
			past.setString(12, String_Tool.DataBaseH_M_S());// obj.getString("cFillinTime")
			past.setString(13, "");// obj.getString("cStockDptno")
			past.setString(14, "");// obj.getString("cStockDpt")
			past.setString(15, "" + fMoney);// obj.getString("fMoney")
			past.setString(16, obj.getString("cWhNo"));
			past.setString(17, obj.getString("cWh"));
			past.setString(18, String_Tool.DataBaseH_M_S());// obj.getString("cTime")
			past.setString(19,cSheetno);// 
			past.setString(20, cSheetStateNo);
			past.execute();
			DB.closePreparedStatement(past);
			
			String sql="update WH_Stock  set bReceive='1' where cSheetno = ? ";
			if(cSheetStateNo.equals("5")){
				sql="update dbo.wh_RbdWarehouse  set bReceive='1' where cSheetno = ?";
			}
			PreparedStatement past2 = conn.prepareStatement(sql);
			past2.setString(1, cSheetno);
			int a = past2.executeUpdate();
			LoggerUtil.info("影响行数"+a);
			DB.closePreparedStatement(past2);
			
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":\"" + SheetNo + "\"}");

			LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":\"" + SheetNo + "\"}");
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			try {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":\"" + SheetNo + "\"}");
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
			DB.closeConn(conn);
		}
	}
}
