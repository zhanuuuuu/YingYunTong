package Online_Manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

@WebServlet(description = "接受用户订单", urlPatterns = { "/Receive_User_Order" })

public class Receive_User_Order extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String cOperatorno = request.getParameter("cOperatorno");
		String cOperatorName = request.getParameter("cOperatorName");
		String cSheetno = request.getParameter("cSheetno");
		String cStoreNo = request.getParameter("cStoreNo");
		String cStoreName = request.getParameter("cStoreName");
		String goodsstr = request.getParameter("goodsarray");
		String Send_Money = request.getParameter("Send_Money");
		Connection conn = null;
		try {
			conn = GetConnection.getStoreConn();
			conn.setAutoCommit(false);
			JSONArray goodsarray = new JSONArray(goodsstr);
			BigDecimal Money = new BigDecimal("0.00");

			PreparedStatement pasto = conn.prepareStatement("select Pay_state from Simple_online.dbo.Order_Table where cSheetno =?  ");
			pasto.setString(1, cSheetno);
			ResultSet pasto_rs1 = pasto.executeQuery();
			if (pasto_rs1.next()) {
				String Pay_state = pasto_rs1.getString("Pay_state").replace(" ", "");
				if (Pay_state.equals("3")) {
					DB.closeResultSet(pasto_rs1);
					DB.closePreparedStatement(pasto);//
					out.print("{\"resultStatus\":\"" + 3 + "\"," + "\"dDate\":" + 3 + "}");//已经配送
					return;
				}
			}
			DB.closeResultSet(pasto_rs1);
			DB.closePreparedStatement(pasto);//
			PreparedStatement Order_Details = conn.prepareStatement(
					"update Simple_online.dbo.Order_Details set RealityNum=?,Reality_Money=?  where cGoodsNo =? ");
			for (int i = 0; i < goodsarray.length(); i++) {
				JSONObject obj = goodsarray.getJSONObject(i);
				String cGoodsNo = obj.getString("cGoodsNo");
				String RealityNum = obj.getString("Num"); // 真正的数量
				String Reality_Money = obj.getString("Last_Money");// 真正的钱
				Order_Details.setString(1, RealityNum);
				Order_Details.setString(2, Reality_Money);
				Order_Details.setString(3, cGoodsNo);
				Money = Money.add(new BigDecimal(Reality_Money));
				Order_Details.addBatch();
			}
			Order_Details.executeBatch();
			DB.closePreparedStatement(Order_Details);//
			LoggerUtil.info(Send_Money);
			Money = Money.add(new BigDecimal(Send_Money));// 商品的真实的钱+运费
			PreparedStatement past = conn.prepareStatement(
					"update  Simple_online.dbo.Order_Table set Pay_state ='2',Reality_All_Money =? where cSheetno= ? ");
			past.setString(1, Money.toString());
			LoggerUtil.info("" + Money.toString());
			past.setString(2, cSheetno);
			past.executeUpdate();
			DB.closePreparedStatement(past);

			PreparedStatement past1 = conn.prepareStatement(
					"insert into  Simple_online.dbo.Store_Receive_Order_Log  (cSheetno,cOperatorNo,cOperator,cStoreNo,cStoreName,dDate) values (?,?,?,?,?,?)");
			past1.setString(1, cSheetno);
			past1.setString(2, cOperatorno);
			past1.setString(3, cOperatorName);
			past1.setString(4, cStoreNo);
			past1.setString(5, cStoreName);
			past1.setString(6, String_Tool.DataBaseTime());
			int a = past1.executeUpdate();
			DB.closePreparedStatement(past1);

			/* 查询订单的商品进行加入销售表 */
			CallableStatement c = conn.prepareCall("{call select_online_Order_goods (?)}");
			c.setString(1, cSheetno);
			LoggerUtil.info(cSheetno);
			ResultSet rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			LoggerUtil.info(array);
			String table = "";// 呵呵哒

			PreparedStatement Pos_Sale = conn.prepareStatement("select cWhNo,cWh,Pos_Sale,Pos_Cost from posmanagement_main.dbo.t_WareHouse where cStoreNo=? ");
			Pos_Sale.setString(1, "000"); // 路发总部
			ResultSet rs3 = Pos_Sale.executeQuery();
			String cWhNo = "";
			if (rs3.next()) {
				table = rs3.getString("Pos_Sale");
				cWhNo = rs3.getString("cWhNo");
			}
			DB.closeResultSet(rs3);
			DB.closePreparedStatement(Pos_Sale);
			LoggerUtil.info(table);
			PreparedStatement past4 = conn
					.prepareStatement(" select sheetno  from " + table + ".dbo.jiesuan  where sheetno=? ");
			past4.setString(1, cSheetno);
			ResultSet rs4 = past4.executeQuery();
			if (rs4.next()) {
				out.print("{\"resultStatus\":\"" + "4" + "\"," + "\"dDate\":" + "已经提交" + "}");
				
				return;
			}
			PreparedStatement past2 = conn.prepareStatement("insert into " + table
					+ ".dbo.t_SaleSheetDetail (dSaleDate,cSaleSheetno,iSeed,cStoreNo,cGoodsNo,cGoodsName,cBarCode,cOperatorno,cOperatorName,cVipCardno,bAuditing,cChkOperno,cChkOper,bSettle,fVipScore,fPrice,fNormalSettle,bVipPrice,fVipPrice,bVipRate,fVipRate,fQuantity,fAgio,fLastSettle0,fLastSettle,cSaleTime,dFinanceDate,bWeight,fNormalVipScore,cStoreName,cBanci_ID,iLineNo_Banci,cBanci,bOnLine) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);

				past2.setString(1, String_Tool.DataBaseYear_Month_Day());
				past2.setString(2, cSheetno);
				past2.setString(3, "" + (i + 1));
				past2.setString(4, obj.getString("cStoreNo"));
				past2.setString(5, obj.getString("cGoodsNo"));
				past2.setString(6, obj.getString("cGoodsName"));
				past2.setString(7, obj.getString("cBarcode"));
				past2.setString(8, cOperatorno);
				past2.setString(9, cOperatorName);
				past2.setString(10, "");// cVipCardno
				past2.setString(11, "0");// bAuditing
				past2.setString(12, "");// cChkOperno
				past2.setString(13, "");// cChkOper
				past2.setString(14, "0");// bSettle
				past2.setString(15, obj.getString("Last_Price"));// fVipScore
				past2.setString(16, obj.getString("Last_Price"));// fPrice
				past2.setString(17, obj.getString("fNormalPrice"));// fNormalSettle
				past2.setString(18, "0");// bVipPrice
				past2.setString(19, obj.getString("Last_Price"));//// fVipPrice
				past2.setString(20, "0");// bVipRate
				past2.setString(21, obj.getString("Last_Price"));// fVipRate
				past2.setString(22, obj.getString("RealityNum"));
				past2.setString(23, "" + 0.00);
				past2.setString(24, obj.getString("Reality_Money"));// fLastSettle0
				past2.setString(25, obj.getString("Reality_Money"));// fLastSettle
				past2.setString(26, String_Tool.DataBaseH_M_S());
				past2.setString(27, String_Tool.DataBaseTime());
				past2.setString(28, obj.getString("bWeight"));
				past2.setString(29, obj.getString("fNormalPrice"));
				past2.setString(30, obj.getString("cStoreName"));// bPost,
				past2.setString(31, "");// cBanci_ID,//String_Tool.DataBaseYear_Month_Day()+"_1"
				past2.setString(32, "");// iLineNo_Banci,+1
				past2.setString(33, "");// cBanci,String_Tool.get_zao_wan_ban()
				past2.setString(34, "1");// bOnLine
				past2.addBatch();
			}
			past2.executeBatch();
			LoggerUtil.info(array.toString());
			
			PreparedStatement select_goods_sale_num=conn.prepareStatement("select cGoodsNo,RealityNum from Simple_online.dbo.Order_Details where cSheetno =? ");
			select_goods_sale_num.setString(1, cSheetno);
			ResultSet rs_sale_num=select_goods_sale_num.executeQuery();
			
			PreparedStatement update_goods_sale_num=conn.prepareStatement("update Posmanagement_main.dbo.t_goods set Sale_number =isnull(Sale_number,0)+? where cGoodsNo=? ");
			while(rs_sale_num.next()){
				String RealityNum=rs_sale_num.getString("RealityNum");
				String cGoodsNo=rs_sale_num.getString("cGoodsNo");
				update_goods_sale_num.setBigDecimal(1, new BigDecimal(RealityNum));
				update_goods_sale_num.setString(2, cGoodsNo);
				update_goods_sale_num.addBatch();
			}
			update_goods_sale_num.executeBatch();
			DB.closeResultSet(rs_sale_num);
			DB.closePreparedStatement(select_goods_sale_num);
			DB.closePreparedStatement(update_goods_sale_num);
			
			
			JSONObject obj = array.getJSONObject(0);

			PreparedStatement past3 = conn.prepareStatement("insert into " + table
					+ ".dbo.jiesuan (zdriqi,sheetno,jstype,cStoreNo,mianzhi,zhekou,zhaoling,"
					+ "shishou,jstime,jiaozhang,jiaozhangtime,"
					+ "shouyinyuanno,shouyinyuanmc,netjiecun,orientmoney,leftmoney,storevalue,detail,bPost,tag_daily,bGroupSale,cWhNo,"
					+ "cStoreName,cBanci,cBanci_ID,iLineNo_Banci,bOnLine)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past3.setString(1, String_Tool.DataBaseYear_Month_Day());
			past3.setString(2, cSheetno);
			past3.setString(3, obj.getString("Describe"));
			past3.setString(4, obj.getString("cStoreNo"));
			past3.setString(5, obj.getString("Reality_All_Money"));
			past3.setString(6, "" + 100.00);
			past3.setString(7, "0");
			past3.setString(8, obj.getString("Reality_All_Money"));
			past3.setString(9, String_Tool.DataBaseTime());
			past3.setString(10, "0");// jiaozhang
			past3.setString(11, String_Tool.DataBaseTime());
			past3.setString(12, cOperatorno);
			past3.setString(13, cOperatorName);
			past3.setString(14, "0");// netjiecun,
			past3.setString(15, "0");// orientmoney,
			past3.setString(16, "0");// leftmoney,
			past3.setString(17, "0");// storevalue,
			past3.setString(18, obj.getString("Describe"));// detail,
			past3.setString(19, "0");// bPost,
			past3.setString(20, "1");// tag_daily
			past3.setString(21, "0");// bPost,
			past3.setString(22, cWhNo);// bPost,
			past3.setString(23, obj.getString("cStoreName"));// bPost,
			past3.setString(24, "");// cBanci,String_Tool.get_zao_wan_ban()
			past3.setString(25, "");// cBanci_ID,String_Tool.DataBaseYear_Month_Day()+"_1"
			past3.setString(26, "");// iLineNo_Banci,+1
			past3.setString(27, "" + 1);// bOnLine
			a = past3.executeUpdate();
			if (a > 0) {
				conn.commit();
				conn.setAutoCommit(true);
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + a + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + "0" + "}");
				conn.rollback();
			}
		} catch (Exception e) {
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
