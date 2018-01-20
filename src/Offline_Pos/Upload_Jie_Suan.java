package Offline_Pos;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
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
import ModelRas.MD5key;
import Tool.ResultSet_To_JSON;

@WebServlet(description = "提交结算", urlPatterns = { "/Upload_Jie_Suan" })
public class Upload_Jie_Suan extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data");
		String data1 = request.getParameter("data1");
		Connection conn = null;
		try {
			JSONArray array = new JSONArray(data);
			JSONArray array1 = new JSONArray(data1);
			conn = GetConnection.getPos_SaleConn();
			conn.setAutoCommit(false);
			PreparedStatement 	past = conn.prepareStatement("insert into jiesuan(zdriqi,sheetno,jstype,cStoreNo,mianzhi,zhekou,zhaoling,shishou,jstime,jiaozhang,jiaozhangtime,jiaozhangdate,shouyinyuanno,shouyinyuanmc,jiaokuantime,shoukuanno,shoukuanname,netjiecun,orientmoney,leftmoney,storevalue,detail,bPost,tag_daily,bGroupSale,cWhNo,bSent,b_ftp,cStoreName,invoiceNo,cBanci_ID,iLineNo_Banci,cBanci,dUpTime,bOnLine)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				past.setString(1,obj.getString("zdriqi"));
				past.setString(2,obj.getString("sheetno"));
				past.setString(3,obj.getString("jstype"));
				past.setString(4,obj.getString("cStoreNo"));
				past.setString(5,obj.getString("mianzhi"));
				past.setString(6,obj.getString("zhekou"));
				past.setString(7,obj.getString("zhaoling"));
				past.setString(8,obj.getString("shishou"));
				past.setString(9,obj.getString("jstime"));
				past.setString(10,obj.getString("jiaozhang"));
				past.setString(11,obj.getString("jiaozhangtime"));
				past.setString(12,obj.getString("jiaozhangdate"));
				past.setString(13,obj.getString("shouyinyuanno"));
				past.setString(14,obj.getString("shouyinyuanmc"));
				past.setString(15,obj.getString("jiaokuantime"));
				past.setString(16,obj.getString("shoukuanno"));
				past.setString(17,obj.getString("shoukuanname"));
				past.setString(18,obj.getString("netjiecun"));
				past.setString(19,obj.getString("orientmoney"));
				past.setString(20,obj.getString("leftmoney"));
				past.setString(21,obj.getString("storevalue"));
				past.setString(22,obj.getString("detail"));
				past.setString(23,obj.getString("bPost"));
				past.setString(24,obj.getString("tag_daily"));
				past.setString(25,obj.getString("bGroupSale"));
				past.setString(26,obj.getString("cWhNo"));
				past.setString(27,obj.getString("bSent"));
				past.setString(28,obj.getString("b_ftp"));
				past.setString(29,obj.getString("cStoreName"));
				past.setString(30,obj.getString("invoiceNo"));
				past.setString(31,obj.getString("cBanci_ID"));
				past.setString(32,obj.getString("iLineNo_Banci"));
				past.setString(33,obj.getString("cBanci"));
				past.setString(34,obj.getString("dUpTime"));
				past.setString(35,obj.getString("bOnLine"));
				past.addBatch();
				if(i%100==0){
					past.executeBatch();
				}
			}
			past.executeBatch();
			DB.closePreparedStatement(past);

		
			PreparedStatement 	past1 = conn.prepareStatement("insert into dbo.t_SaleSheetDetail(dSaleDate,cSaleSheetno,iSeed,cStoreNo,cGoodsNo,cGoodsName,cBarCode,cOperatorno,cOperatorName,cVipCardno,bAuditing,cChkOperno,cChkOper,bSettle,fVipScore,fPrice,fNormalSettle,bVipPrice,fVipPrice,bVipRate,fVipRate,fQuantity,fAgio,fLastSettle0,fLastSettle,cManager,cManagerno,cSaleTime,dFinanceDate,cWorkerno,cWorker,bPost,bChecked,cCheckNo,dCheck,cVipNo,bBalance,jiesuanno,cStationNo,tag_daily,bGroupSale,cWHno,bHidePrice,bHideQty,bWeight,fNormalVipScore,bExchange,fSupRatio_exchange,bPresent,bSend,iPresentTimes,bLimited,fVipScore_cur,bSetMoneyUsed,bDownCurWH,bSent,bScoreRetMoney,b_ftp,cStoreName,fDiscount,fPrice_Exe,fRate_Exe,cBanci_ID,iLineNo_Banci,cBanci,bMoneycard,fMoneyValue,dUpTime,bOnLine)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int j = 0; j < array1.length(); j++) {
				JSONObject obj1=array1.getJSONObject(j);
				past.setString(1,obj1.getString("dSaleDate"));
				past.setString(2,obj1.getString("cSaleSheetno"));
				past.setString(3,obj1.getString("iSeed"));
				past.setString(4,obj1.getString("cStoreNo"));
				past.setString(5,obj1.getString("cGoodsNo"));
				past.setString(6,obj1.getString("cGoodsName"));
				past.setString(7,obj1.getString("cBarCode"));
				past.setString(8,obj1.getString("cOperatorno"));
				past.setString(9,obj1.getString("cOperatorName"));
				past.setString(10,obj1.getString("cVipCardno"));
				past.setString(11,obj1.getString("bAuditing"));
				past.setString(12,obj1.getString("cChkOperno"));
				past.setString(13,obj1.getString("cChkOper"));
				past.setString(14,obj1.getString("bSettle"));
				past.setString(15,obj1.getString("fVipScore"));
				past.setString(16,obj1.getString("fPrice"));
				past.setString(17,obj1.getString("fNormalSettle"));
				past.setString(18,obj1.getString("bVipPrice"));
				past.setString(19,obj1.getString("fVipPrice"));
				past.setString(20,obj1.getString("bVipRate"));
				past.setString(21,obj1.getString("fVipRate"));
				past.setString(22,obj1.getString("fQuantity"));
				past.setString(23,obj1.getString("fAgio"));
				past.setString(24,obj1.getString("fLastSettle0"));
				past.setString(25,obj1.getString("fLastSettle"));
				past.setString(26,obj1.getString("cManager"));
				past.setString(27,obj1.getString("cManagerno"));
				past.setString(28,obj1.getString("cSaleTime"));
				past.setString(29,obj1.getString("dFinanceDate"));
				past.setString(30,obj1.getString("cWorkerno"));
				past.setString(31,obj1.getString("cWorker"));
				past.setString(32,obj1.getString("bPost"));
				past.setString(33,obj1.getString("bChecked"));
				past.setString(34,obj1.getString("cCheckNo"));
				past.setString(35,obj1.getString("dCheck"));
				past.setString(36,obj1.getString("cVipNo"));
				past.setString(37,obj1.getString("bBalance"));
				past.setString(38,obj1.getString("jiesuanno"));
				past.setString(39,obj1.getString("cStationNo"));
				past.setString(40,obj1.getString("tag_daily"));
				past.setString(41,obj1.getString("bGroupSale"));
				past.setString(42,obj1.getString("cWHno"));
				past.setString(43,obj1.getString("bHidePrice"));
				past.setString(44,obj1.getString("bHideQty"));
				past.setString(45,obj1.getString("bWeight"));
				past.setString(46,obj1.getString("fNormalVipScore"));
				past.setString(47,obj1.getString("bExchange"));
				past.setString(48,obj1.getString("fSupRatio_exchange"));
				past.setString(49,obj1.getString("bPresent"));
				past.setString(50,obj1.getString("bSend"));
				past.setString(51,obj1.getString("iPresentTimes"));
				past.setString(52,obj1.getString("bLimited"));
				past.setString(53,obj1.getString("fVipScore_cur"));
				past.setString(54,obj1.getString("bSetMoneyUsed"));
				past.setString(55,obj1.getString("bDownCurWH"));
				past.setString(56,obj1.getString("bSent"));
				past.setString(57,obj1.getString("bScoreRetMoney"));
				past.setString(58,obj1.getString("b_ftp"));
				past.setString(59,obj1.getString("cStoreName"));
				past.setString(60,obj1.getString("fDiscount"));
				past.setString(61,obj1.getString("fPrice_Exe"));
				past.setString(62,obj1.getString("fRate_Exe"));
				past.setString(63,obj1.getString("cBanci_ID"));
				past.setString(64,obj1.getString("iLineNo_Banci"));
				past.setString(65,obj1.getString("cBanci"));
				past.setString(66,obj1.getString("bMoneycard"));
				past.setString(67,obj1.getString("fMoneyValue"));
				past.setString(68,obj1.getString("dUpTime"));
				past.setString(69,obj1.getString("bOnLine"));
				past1.addBatch();
				if(j%100==0){
					past1.executeBatch();
				}
			}
			past1.executeBatch();
			DB.closePreparedStatement(past1);
			conn.commit();
			conn.setAutoCommit(true);
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dData\":\"" + "调用出错" + "\"}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dData\":\"" + "调用出错" + "\"}");
			try {
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
