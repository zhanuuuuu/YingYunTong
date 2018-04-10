package DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import Tool.ResultSet_To_JSON;

public class Head_Shop {

	public static String sheng_cheng_rukudan(Connection conn, String yanhuodan_no) { // 生成入库单商品
		CallableStatement c = null;
		try {
			c = conn.prepareCall("{call p_cStockVerToInWarehouse (?,?) }");
			c.setString(1, yanhuodan_no);
			c.registerOutParameter("return", java.sql.Types.LONGNVARCHAR);
			c.execute();
			String fage = c.getString("return");
			return fage;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(c, conn);
		}
		return null;
	}

	public static JSONArray select_union_goods(Connection conn) {//查询箱码关联
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "select a.cGoodsNo,a.cBarcode,b.cBarcode  as  cGoodsNo_minPackage from t_Goods a, (select cGoodsNo,cBarcode,cGoodsNo_minPackage from t_Goods where  ISNULL(cGoodsNo_minPackage,'')<>'' )  b where a.cGoodsNo=b.cGoodsNo_minPackage";
			past = conn.prepareStatement(sql);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return null;
	}
	
	public static JSONArray select_Yi_Pin_duo_ma(Connection conn) {//查询一品多码
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "select cBarcode,cGoodsNo_parent from t_Goods_Union";
			past = conn.prepareStatement(sql);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return null;
	}
	
	public static JSONArray Select_stock(Connection conn, String cBarCode,String cStoreNo) {//在线查询库存
		CallableStatement c=null;
		ResultSet rs=null;
		try {
			c = conn.prepareCall("{ call Select_stock  (?,?) } ");
			c.setString(1, cBarCode);
			c.setString(2, cStoreNo);
			rs = c.executeQuery();
			JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, c, conn);
		}
		return null;
	}
	public static JSONArray select_last_Sale(Connection conn, String cGoodsNo) {//在线查询最近销售
		CallableStatement c=null;
		ResultSet rs=null;
		try {
			c = conn.prepareCall("{ call  p_FIFO_SalesProfit_MultGoods_log_App  (?) } ");
			c.setString(1, cGoodsNo);
			rs = c.executeQuery();
			JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, c, conn);
		}
		return null;
	}


	public static int head_shop_Check_Goods(Connection conn, JSONArray data) {
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
