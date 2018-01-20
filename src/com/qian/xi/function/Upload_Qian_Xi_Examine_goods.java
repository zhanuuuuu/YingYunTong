package com.qian.xi.function;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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
import Tool.ReadConfig;
import Tool.String_Tool;

@WebServlet(description = "千禧验货分拣", urlPatterns = { "/Upload_Qian_Xi_Examine_goods" })
public class Upload_Qian_Xi_Examine_goods extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("name");
		String cStoreData = request.getParameter("cStoreData");// 集合套集合
		String Diff_data=request.getParameter("Diff_data");//订单内的商品
		String Other_data=request.getParameter("Other_data");//订单外的商品
		String cGroupTypeNo=request.getParameter("cGroupTypeNo");
		String cGroupTypeName=request.getParameter("cGroupTypeName");
		Connection conn = null;
		String SheetNo = null;
		double fMoney = 0;
		String cStoreSheetNo = null;// 门店商品表单号
		double cStorefMoney = 0;
		try {
			JSONArray array = new JSONArray(data);
			conn = GetConnection.getStoreConn();
			conn.setAutoCommit(false);

			JSONObject obj = array.getJSONObject(0);

			/* 取单号正常验货单单号 */
			SheetNo = GetcSheetno.get_commonality_cSheetNo(conn,
					"select SheetNo=" + new ReadConfig().getprop().getProperty("DataSource") + ".dbo.f_GenYHsheetno_Ct('"
							+ String_Tool.DataBaseYear() + "','" + obj.getString("cSupplierNo") + "')");

//			PreparedStatement forbid_past = conn.prepareStatement("select * from  WH_Stock where  bReceive='1' and cSheetno=? ");
//			forbid_past.setString(1, obj.getString("cSheetNo"));
//			ResultSet rs = forbid_past.executeQuery();
//			if (rs.next()) {// 已经提交
//				out.print("{\"resultStatus\":\"" + 2 + "\"," + "\"data\":\"" + SheetNo + "\"}");
//				return;
//			}
//			DB.closeResultSet(rs);
//			DB.closePreparedStatement(forbid_past);

			/* 插入供应商验货表 */
			PreparedStatement past_detail = conn.prepareStatement(
					"insert into wh_StockVerifyDetail_Ct(cSheetno,iLineNo,cGoodsNo,cGoodsName,cBarcode,cUnitedNo,fQuantity,fInPrice,fInMoney,fTaxrate,bTax,fTaxPrice,fTaxMoney,fNoTaxPrice,fNoTaxMoney,cUnit,cSpec,fPacks)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past_detail.setString(1, SheetNo);
				past_detail.setString(2, "" + i);
				past_detail.setString(3, obj1.getString("cGoodsNo"));

				/* 取售价 */
				String fNormalPrice = "0.0";
				// select fCKPrice from dbo.t_cStoreGoods
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
				past_detail.addBatch();
				if(i%100==0){
					past_detail.executeBatch();
				}
			}
			past_detail.executeBatch();
			DB.closePreparedStatement(past_detail);
		
			PreparedStatement past = conn.prepareStatement(
					"insert into wh_StockVerify_Ct(cStoreNo,cStoreName,dDate,cSheetNO,cSupplierNo,cSupplier,cOperatorNo,cOperator,cFillEmpNo,cFillEmp,dFillin,cFillinTime,cStockDptno,cStockDpt,fMoney,cWhNo,cWh,cTime,cSheetStateNo,cSheetState,cSheetno_stock,cGroupTypeNo,cGroupTypeName)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
			past.setString(19, "3");// obj.getString("cTime")
			past.setString(20, "郑州直通验货单");// obj.getString("cTime")
			past.setString(21, obj.getString("cSheetNo"));// obj.getString("cTime")
			past.setString(22, cGroupTypeNo);// obj.getString("cTime")
			past.setString(23, cGroupTypeName);// obj.getString("cTime")
			past.execute();
			DB.closePreparedStatement(past);

			String No = GetcSheetno.get_commonality_cSheetNo(conn,
					"select SheetNo=" + new ReadConfig().getprop().getProperty("DataSource") + ".dbo.CreateOrderID()");

			PreparedStatement past1 = conn.prepareStatement("INSERT INTO t_DirectSheetNoRelevance(cSheetNo_0,cSheetNo_1,operaTime,operaNo,operaName,opreaContent,iflag,idate,cSupNo) values(?,?,?,?,?,?,?,?,?) ");
			past1.setString(1, No);
			past1.setString(2, SheetNo);
			past1.setString(3, String_Tool.DataBaseTime());
			past1.setString(4, obj.getString("cOperatorNo"));
			past1.setString(5, obj.getString("cOperator"));
			past1.setString(6, "POS_验货单关联直通验货单汇总单");
			past1.setString(7, "2");
			past1.setString(8, String_Tool.DataBaseYear_Month_Day());
			past1.setString(9, obj.getString("cSupplierNo"));
			past1.execute();
			DB.closePreparedStatement(past1);

			JSONArray cStoreData_array = new JSONArray(cStoreData);
			String No1 = GetcSheetno.get_commonality_cSheetNo(conn, "select SheetNo="+ new ReadConfig().getprop().getProperty("DataSource") + ".dbo.CreateOrderID()");
			for (int i = 0; i < cStoreData_array.length(); i++) {
				cStorefMoney = 0.0;
				/* 取单号各个门店验货单单号 */
				//cStoreSheetNo = GetcSheetno.get_commonality_cSheetNo(conn,"select SheetNo=" + new ReadConfig().getprop().getProperty("DataSource")+ ".dbo.f_GenYHsheetno_St_CT('" + String_Tool.DataBaseYear() + "','"+ obj.getString("cSupplierNo") + "')");
				cStoreSheetNo =String_Tool.reformat() + obj.getString("cSupplierNo");
				cStoreSheetNo=UUID.randomUUID().toString().replace("-", "");
				JSONObject obj1 = cStoreData_array.getJSONObject(i);
				JSONArray Detail_St_array = obj1.getJSONArray("Detail_St");
//wh_StockVerifyDetail_St
				
			
				PreparedStatement past_detail_St = conn.prepareStatement("insert into wh_St_CtockVerifyDetail_St_Ct(cSheetno,iLineNo,cGoodsNo,cGoodsName,cBarcode,cUnitedNo,fQuantity,fInPrice,fInMoney,fTaxrate,bTax,fTaxPrice,fTaxMoney,fNoTaxPrice,fNoTaxMoney,cUnit,cSpec,fPacks,dProduct)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

				for (int j = 0; j < Detail_St_array.length(); j++) {
					/* 插入门店要供应商表 */
					JSONObject obj_str = Detail_St_array.getJSONObject(j);

					System.out.println("单号"+cStoreSheetNo+"序号"+(j));

					past_detail_St.setString(1, cStoreSheetNo);
					past_detail_St.setString(2, "" + (j+1));
					past_detail_St.setString(3, obj_str.getString("cGoodsNo"));

					/* 取售价 */
					String fNormalPrice = "0.0";
					// select fCKPrice from dbo.t_cStoreGoods
					PreparedStatement past2 = conn
							.prepareStatement("select top 1 fNormalPrice from t_goods where cGoodsNo=?   ");
					past2.setString(1, obj_str.getString("cGoodsNo"));
					ResultSet rs2 = past2.executeQuery();
					if (rs2.next()) {
						fNormalPrice = rs2.getString("fNormalPrice");
					}
					DB.closeResultSet(rs2);
					DB.closePreparedStatement(past2);

					past_detail_St.setString(4, obj_str.getString("cGoodsName"));
					past_detail_St.setString(5, obj_str.getString("cBarcode"));
					past_detail_St.setString(6, "");// obj_str.getString("cUnitedNo")
					past_detail_St.setString(7, obj_str.getString("fQuantity"));

					past_detail_St.setString(8, obj_str.getString("fInPrice"));
					past_detail_St.setString(9, obj_str.getString("fInMoney"));
					double fInMoney = Double.parseDouble(obj_str.getString("fInMoney"));
					cStorefMoney = fInMoney + fMoney;
					double fTaxrate = (Double.parseDouble(fNormalPrice)
							- Double.parseDouble(obj_str.getString("fInPrice")))
							/ Double.parseDouble(obj_str.getString("fInPrice"));
					past_detail_St.setString(10, "" + fTaxrate);
					past_detail_St.setString(11, "1");
					String fTaxPrice = ""
							+ (Double.parseDouble(fNormalPrice) - Double.parseDouble(obj_str.getString("fInPrice")));
					past_detail_St.setString(12, "" + fTaxPrice);// obj_str.getString("fTaxPrice")
					String fTaxMoney = ""
							+ (Double.parseDouble(fTaxPrice) * Double.parseDouble(obj_str.getString("fQuantity")));
					past_detail_St.setString(13, fTaxMoney);// obj_str.getString("fTaxMoney")
					past_detail_St.setString(14, "" + fNormalPrice);// obj_str.getString("fNoTaxPrice")
					past_detail_St.setString(15, ""
							+ (Double.parseDouble(fNormalPrice) * Double.parseDouble(obj_str.getString("fQuantity"))));// obj_str.getString("fNoTaxMoney")
					past_detail_St.setString(16, obj_str.optString("cUnit"));
					past_detail_St.setString(17, obj_str.optString("cSpec"));
					past_detail_St.setString(18, "1");// obj_str.getString("fPacks")
					past_detail_St.setString(19, String_Tool.DataBaseYear_Month_Day());//
					past_detail_St.addBatch();
					if(j%20==0){
						past_detail_St.executeBatch();
					}
				}
				past_detail_St.executeBatch();
				DB.closePreparedStatement(past_detail_St);
				
			
				PreparedStatement past_Store = conn.prepareStatement(//wh_CtockVerify_St_Ct
						"insert into wh_St_CtockVerify_St_Ct(cStoreNo,cStoreName,dDate,cSheetNO,cSupplierNo,cSupplier,cOperatorNo,cOperator,cFillEmpNo,cFillEmp,dFillin,cFillinTime,cStockDptno,cStockDpt,fMoney,cWhNo,cWh,cTime,cGroupTypeNo,cGroupTypeName,cSheetno_Stock)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				past_Store.setString(1, obj1.getString("cStoreNo"));
				past_Store.setString(2, obj1.getString("cStoreName"));
				past_Store.setString(3, String_Tool.DataBaseYear_Month_Day());
				past_Store.setString(4, cStoreSheetNo);// obj1.getString("cSheetNO");
				past_Store.setString(5, obj1.getString("cSupplierNo"));
				past_Store.setString(6, obj1.getString("cSupplier"));
				past_Store.setString(7, obj1.getString("cOperatorNo"));
				past_Store.setString(8, obj1.getString("cOperator"));
				past_Store.setString(9, obj1.getString("cOperatorNo"));
				past_Store.setString(10, obj1.getString("cOperator"));
				past_Store.setString(11, String_Tool.DataBaseTime());
				past_Store.setString(12, String_Tool.DataBaseH_M_S());// obj1.getString("cFillinTime")
				past_Store.setString(13, "");// obj1.getString("cStockDptno")
				past_Store.setString(14, "");// obj1.getString("cStockDpt")
				past_Store.setString(15, "" + cStorefMoney);// obj1.getString("fMoney")
				past_Store.setString(16, obj1.optString("cWhNo"));
				past_Store.setString(17, obj1.optString("cWh"));
				past_Store.setString(18, String_Tool.DataBaseH_M_S());// obj.getString("cTime")
			
				past_Store.setString(19, cGroupTypeNo);
				past_Store.setString(20, cGroupTypeName);// obj.getString("cTime")
				past_Store.setString(21,  obj.getString("cSheetNo"));// obj.getString("cTime")
				past_Store.execute();
				DB.closePreparedStatement(past_Store);

				// 生成单据（生成函数：dbo.CreateOrderID()

			
				PreparedStatement past11 = conn.prepareStatement(
						"INSERT INTO t_DirectSheetNoRelevance(cSheetNo_0,cSheetNo_1,operaTime,operaNo,operaName,opreaContent,iflag,idate,cSupNo,cGroupTypeNo) values(?,?,?,?,?,?,?,?,?,?) ");
				past11.setString(1, No1);
				past11.setString(2, cStoreSheetNo);
				past11.setString(3, String_Tool.DataBaseTime());
				past11.setString(4, obj1.getString("cOperatorNo"));
				past11.setString(5, obj1.getString("cOperator"));
				past11.setString(6, "POS_ST验货单关联直通验货单汇总单");
				past11.setString(7, "1");
				past11.setString(8, String_Tool.DataBaseYear_Month_Day());
				past11.setString(9, obj1.getString("cSupplierNo"));
				past11.setString(10, cGroupTypeNo);
				past11.execute();
				DB.closePreparedStatement(past11);
			}

			
			//差异表
			JSONArray Diff_data_array=new JSONArray(Diff_data);
			PreparedStatement wh_StockVerifyDetail_Diff_past = conn.prepareStatement("insert into wh_StockVerifyDetail_Other_Ct(cSheetno,iLineNo,cGoodsNo,cGoodsName,cBarcode,cUnitedNo,fQuantity,fInPrice,fInMoney,fTaxrate,bTax,fTaxPrice,fTaxMoney,fNoTaxPrice,fNoTaxMoney,cUnit,cSpec,fPacks,cStockSheetno,cOtherSheetno)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			for (int i = 0; i < Diff_data_array.length(); i++) {
				JSONObject obj1 = Diff_data_array.getJSONObject(i);
				wh_StockVerifyDetail_Diff_past.setString(1, SheetNo);
				wh_StockVerifyDetail_Diff_past.setString(2, "" + i);
				wh_StockVerifyDetail_Diff_past.setString(3, obj1.getString("cGoodsNo"));

				/* 取售价 */
				String fNormalPrice = "0.0";
				// select fCKPrice from dbo.t_cStoreGoods
				PreparedStatement past2 = conn.prepareStatement("select fNormalPrice from t_goods where cGoodsNo=?   ");
				past2.setString(1, obj1.getString("cGoodsNo"));
				ResultSet rs2 = past2.executeQuery();
				if (rs2.next()) {
					fNormalPrice = rs2.getString("fNormalPrice");
				}
				DB.closeResultSet(rs2);
				DB.closePreparedStatement(past2);

				wh_StockVerifyDetail_Diff_past.setString(4, obj1.getString("cGoodsName"));
				wh_StockVerifyDetail_Diff_past.setString(5, obj1.getString("cBarcode"));
				wh_StockVerifyDetail_Diff_past.setString(6, "");// obj1.getString("cUnitedNo")
				wh_StockVerifyDetail_Diff_past.setString(7, obj1.getString("fQuantity"));

				wh_StockVerifyDetail_Diff_past.setString(8, obj1.getString("fInPrice"));
				wh_StockVerifyDetail_Diff_past.setString(9, obj1.getString("fInMoney"));
				double fInMoney = Double.parseDouble(obj1.getString("fInMoney"));
				fMoney = fInMoney + fMoney;
				double fTaxrate = (Double.parseDouble(fNormalPrice) - Double.parseDouble(obj1.getString("fInPrice")))
						/ Double.parseDouble(obj1.getString("fInPrice"));
				wh_StockVerifyDetail_Diff_past.setString(10, "" + fTaxrate);
				wh_StockVerifyDetail_Diff_past.setString(11, "1");
				String fTaxPrice = ""
						+ (Double.parseDouble(fNormalPrice) - Double.parseDouble(obj1.getString("fInPrice")));
				wh_StockVerifyDetail_Diff_past.setString(12, "" + fTaxPrice);// obj1.getString("fTaxPrice")
				String fTaxMoney = ""
						+ (Double.parseDouble(fTaxPrice) * Double.parseDouble(obj1.getString("fQuantity")));
				wh_StockVerifyDetail_Diff_past.setString(13, fTaxMoney);// obj1.getString("fTaxMoney")
				wh_StockVerifyDetail_Diff_past.setString(14, "" + fNormalPrice);// obj1.getString("fNoTaxPrice")
				wh_StockVerifyDetail_Diff_past.setString(15,
						"" + (Double.parseDouble(fNormalPrice) * Double.parseDouble(obj1.getString("fQuantity"))));// obj1.getString("fNoTaxMoney")
				wh_StockVerifyDetail_Diff_past.setString(16, obj1.optString("cUnit"));
				wh_StockVerifyDetail_Diff_past.setString(17, obj1.optString("cSpec"));
				wh_StockVerifyDetail_Diff_past.setString(18, "1");// obj1.getString("fPacks")
				wh_StockVerifyDetail_Diff_past.setString(19, obj.getString("cSheetNo"));
				wh_StockVerifyDetail_Diff_past.setString(20, "Diff"+String_Tool.reformat());// obj1.getString("fPacks")
				wh_StockVerifyDetail_Diff_past.addBatch();
			}
			wh_StockVerifyDetail_Diff_past.executeBatch();
			DB.closePreparedStatement(wh_StockVerifyDetail_Diff_past);
			
			
			
			//订单以外的商品
			JSONArray Other_data_array=new JSONArray(Other_data);
			PreparedStatement wh_StockVerifyDetail_Other_past = conn.prepareStatement("insert into wh_StockVerifyDetail_Other_Ct(cSheetno,iLineNo,cGoodsNo,cGoodsName,cBarcode,cUnitedNo,fQuantity,fInPrice,fInMoney,fTaxrate,bTax,fTaxPrice,fTaxMoney,fNoTaxPrice,fNoTaxMoney,cUnit,cSpec,fPacks,cStockSheetno,cOtherSheetno)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			for (int i = 0; i < Other_data_array.length(); i++) {
				JSONObject obj1 = Other_data_array.getJSONObject(i);
				wh_StockVerifyDetail_Other_past.setString(1, SheetNo);
				wh_StockVerifyDetail_Other_past.setString(2, "" + i);
				wh_StockVerifyDetail_Other_past.setString(3, obj1.getString("cGoodsNo"));

				/* 取售价 */
				String fNormalPrice = "0.0";
				// select fCKPrice from dbo.t_cStoreGoods
				PreparedStatement past2 = conn.prepareStatement("select fNormalPrice from t_goods where cGoodsNo=?   ");
				past2.setString(1, obj1.getString("cGoodsNo"));
				ResultSet rs2 = past2.executeQuery();
				if (rs2.next()) {
					fNormalPrice = rs2.getString("fNormalPrice");
				}
				DB.closeResultSet(rs2);
				DB.closePreparedStatement(past2);

				wh_StockVerifyDetail_Other_past.setString(4, obj1.getString("cGoodsName"));
				wh_StockVerifyDetail_Other_past.setString(5, obj1.getString("cBarcode"));
				wh_StockVerifyDetail_Other_past.setString(6, "");// obj1.getString("cUnitedNo")
				wh_StockVerifyDetail_Other_past.setString(7, obj1.getString("fQuantity"));

				wh_StockVerifyDetail_Other_past.setString(8, obj1.getString("fInPrice"));
				wh_StockVerifyDetail_Other_past.setString(9, obj1.getString("fInMoney"));
				double fInMoney = Double.parseDouble(obj1.getString("fInMoney"));
				fMoney = fInMoney + fMoney;
				double fTaxrate = (Double.parseDouble(fNormalPrice) - Double.parseDouble(obj1.getString("fInPrice")))
						/ Double.parseDouble(obj1.getString("fInPrice"));
				wh_StockVerifyDetail_Other_past.setString(10, "" + fTaxrate);
				wh_StockVerifyDetail_Other_past.setString(11, "1");
				String fTaxPrice = ""
						+ (Double.parseDouble(fNormalPrice) - Double.parseDouble(obj1.getString("fInPrice")));
				wh_StockVerifyDetail_Other_past.setString(12, "" + fTaxPrice);// obj1.getString("fTaxPrice")
				String fTaxMoney = ""
						+ (Double.parseDouble(fTaxPrice) * Double.parseDouble(obj1.getString("fQuantity")));
				wh_StockVerifyDetail_Other_past.setString(13, fTaxMoney);// obj1.getString("fTaxMoney")
				wh_StockVerifyDetail_Other_past.setString(14, "" + fNormalPrice);// obj1.getString("fNoTaxPrice")
				wh_StockVerifyDetail_Other_past.setString(15,
						"" + (Double.parseDouble(fNormalPrice) * Double.parseDouble(obj1.getString("fQuantity"))));// obj1.getString("fNoTaxMoney")
				wh_StockVerifyDetail_Other_past.setString(16, obj1.optString("cUnit"));
				wh_StockVerifyDetail_Other_past.setString(17, obj1.optString("cSpec"));
				wh_StockVerifyDetail_Other_past.setString(18, "1");// obj1.getString("fPacks")
				
				
				wh_StockVerifyDetail_Other_past.setString(19, obj.getString("cSheetNo"));
				wh_StockVerifyDetail_Other_past.setString(20, "Other"+String_Tool.reformat());// obj1.getString("fPacks")
				wh_StockVerifyDetail_Other_past.addBatch();
			}
			wh_StockVerifyDetail_Other_past.executeBatch();
			DB.closePreparedStatement(wh_StockVerifyDetail_Other_past);
			
			
			conn.commit();
			conn.setAutoCommit(true);
			
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":\"" + SheetNo + "\"}");

			System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":\"" + SheetNo + "\"}");
			
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
