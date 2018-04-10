package DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import Tool.GetcSheetno;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

public class Fen_dian_Update {

	public static boolean Insert_into_fen_dian_yan_dan(Connection conn, JSONArray array, JSONArray array1,
			JSONArray array2) { // 分店验单
		PreparedStatement past = null;
		String strrandom;
		double sum = 0;
		Connection conn0 = GetConnection.getStoreConn();// 总部的连接
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式 //这是门店的连接
			conn0.setAutoCommit(false);
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.getfen_dian_yan_zong_bu_no(conn, String_Tool.DataBaseYear(),
					obj.getString("cCustomerNo"));
			LoggerUtil.info(strrandom);

			for (int i = 0; i < array1.length(); i++) {
				JSONObject obj1 = array1.getJSONObject(i);
				String sql8 = "select bStoreYhShip from wh_ShipOutYhSheetDetail where cSheetno=? and cSheetNo_YH=? and cSheetNo_Pallet=? and  cStoreNo= ? and bStoreYhShip='1' ";
				PreparedStatement past8 = conn0.prepareStatement(sql8);
				past8.setString(1, obj1.getString("zhuang_che_no"));
				past8.setString(2, obj1.getString("cSheetNo_YH"));
				past8.setString(3, obj1.getString("cSheetNo_Pallet"));
				past8.setString(4, obj.getString("cCustomerNo"));// 开始的时候,这个是获取店铺的编号
				ResultSet rs8 = past8.executeQuery();
				if (rs8.next()) {
					LoggerUtil.info("已经验货");
					DB.closeResultSet(rs8);
					DB.closePreparedStatement(past8);
					return true;
				}
				DB.closeResultSet(rs8);
				DB.closePreparedStatement(past8);
			}
			// String sql8 = "select bStoreYhShip from wh_ShipOutYhSheetDetail
			// where cSheetno=? and cSheetNo_YH=? and cSheetNo_Pallet=? and
			// cStoreNo= ? and bStoreYhShip='1' ";
			// PreparedStatement past8 = conn0.prepareStatement(sql8);
			// past8.setString(1, obj.getString("zhuang_che_no"));
			// past8.setString(2, obj.getString("cSheetNo_YH"));
			// past8.setString(3, obj.getString("cSheetNo_Pallet"));
			// past8.setString(4, obj.getString("cCustomerNo"));//
			// 开始的时候,这个是获取店铺的编号
			// ResultSet rs8 = past8.executeQuery();
			// if (rs8.next()) {
			// LoggerUtil.info("已经验货");
			// DB.closeResultSet(rs8);
			// DB.closePreparedStatement(past8);
			// return true;
			// }
			// DB.closeResultSet(rs8);
			// DB.closePreparedStatement(past8);

			// 门店验货的时候主键冲突好找商品，解决办法，从主库下发到前置机
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement past_s = conn.prepareStatement("select * from t_goods where cGoodsNo=? ");
				past_s.setString(1, obj1.getString("cGoodsNo"));
				ResultSet rs = past_s.executeQuery();
				if (!rs.next()) {
					LoggerUtil.info("此条码没有" + obj1.getString("cGoodsNo"));
				} else {
					LoggerUtil.info("此条码有" + obj1.getString("cGoodsNo"));
				}
				DB.closeRs_Con(rs, past_s, null);
			}
			PreparedStatement past1 = conn.prepareStatement(
					"INSERT INTO wh_StockVerifyDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit) values (?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement past0 = conn0
					.prepareStatement("update wh_cStoreOutWarehouse set bReceive ='1' where cOutSerNo=? ");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);

				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				double fInMoney = Double.parseDouble(obj1.getString("fInMoney"));
				sum = sum + fInMoney;
				past1.setString(7, "" + String_Tool.String_IS_Two(fInMoney));
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.addBatch();
				if (i % 200 == 0) {
					past1.executeBatch();
				}

				// 把商品对应的出库单变为1
				past0.setString(1, obj1.getString("cOutserno"));
				past0.addBatch();

				if (i % 200 == 0) {
					past0.executeBatch();
				}
			}
			past1.executeBatch();
			past0.executeBatch();
			DB.closePreparedStatement(past1);
			DB.closePreparedStatement(past0);

			past = conn.prepareStatement(
					"INSERT INTO wh_StockVerify  (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,cWhNo,cWh,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,cSheetno_stock,cSupplierNo,cSupplier,fMoney,bExamin) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cCustomerNo"));
			past.setString(4, obj.getString("cCustomer"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cwh"));
			past.setString(9, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(10, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(11, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(12, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(13, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			past.setString(14, obj.getString("cSheetno")); // dFillin,cFillinTime,cTime
			past.setString(15, obj.getString("cStoreNo")); // dFillin,cFillinTime,cTime
			past.setString(16, obj.getString("cStoreName")); // dFillin,cFillinTime,cTime
			past.setString(17, "" + sum);
			past.setString(18, obj.getString("yishenhe"));
			past.execute();

			PreparedStatement past21 = conn0.prepareStatement(
					"UPDATE  wh_ShipOutYhSheetDetail set bStoreYhShip ='1' where cSheetno =? and cSheetNo_YH =? and cStoreNo =?");
			for (int i = 0; i < array1.length(); i++) {
				JSONObject obj1 = array1.getJSONObject(i);
				String zhuang_che_no = obj1.getString("zhuang_che_no");
				String cStoreNo = obj.getString("cCustomerNo");// 开始的时候,这个是获取店铺的编号
				String cSheetNo_YH = obj1.getString("cSheetNo_YH");
				String cSheetNo_Pallet = obj1.getString("cSheetNo_Pallet");

				past21.setString(1, zhuang_che_no);
				past21.setString(2, cSheetNo_YH);
				past21.setString(3, cStoreNo);
				past21.addBatch();
				if (i % 200 == 0) {
					past21.executeBatch();
				}
			}
			DB.closePreparedStatement(past21);

			PreparedStatement past3 = conn0.prepareStatement(
					"UPDATE  t_DistributionBox  set  cStore_No =? , cStore_Name=? , user_no =? , [user_name]=?, recovery_time=?  where cDistBoxNo =? ");
			for (int i = 0; i < array2.length(); i++) {
				JSONObject obj2 = array2.getJSONObject(i);
				String cDistBoxNo_name = obj2.getString("cDistBoxNo_name");
				String cPalletNo_name = obj2.optString("cPalletNo_name");
				String user_no = obj2.optString("user_no");
				String user_name = obj2.optString("user_name");
				String user_type = obj2.optString("user_type");

				past3.setString(1, obj.getString("cCustomerNo"));
				past3.setString(2, obj.getString("cCustomer"));
				past3.setString(3, user_no);
				past3.setString(4, user_name);
				past3.setString(5, String_Tool.DataBaseTime());
				past3.setString(6, cDistBoxNo_name);
				past3.addBatch();
				if (i % 200 == 0) {
					past3.executeBatch();
				}
			}
			DB.closePreparedStatement(past3);
			conn.commit();
			conn.setAutoCommit(true);
			conn0.commit();
			conn0.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			try {
				conn.rollback();
				conn0.rollback();
				e.printStackTrace();
				return false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn0);
			DB.closeConn(conn);
		}
		return false;
	}

	public static JSONArray Select_Yi_Shen_Fen_dian_yan_dan(Connection conn, String cStoreNo) { // 查询已经审核的分店验收单,但是还没入库
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call Select_Fen_dian_Yi_Yan_Shou (?) }");
			c.setString(1, cStoreNo);
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(c, conn);
		}
		return null;
	}

	public static int Insert_Return_Pallet(Connection conn, String user_no, String user_name, String cStore_No,
			String cStore_Name, String iQty, String cDistDev_Return, String TruckLicenseTag, String cDriverNo,
			String cDriverName, String TruckNo) {
		String sql = "INSERT INTO t_DistDev_Return (dDate,cSheetNo,cDistDev_Return,iQty,cStoreNo,cStoreName,cOperNo_Store,cOperName_Store,cDriverNo,cDriverName,TruckLicenseTag,TruckNo,dDateTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		ResultSet rs = null;
		String cSheetNo = null;
		PreparedStatement past = null;
		try {
			cSheetNo = cStore_No + "-" + String_Tool.reformat();
			past = conn.prepareStatement(sql);
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, cSheetNo);
			past.setString(3, cDistDev_Return);
			past.setString(4, iQty);
			past.setString(5, cStore_No);
			past.setString(6, cStore_Name);
			past.setString(7, user_no);
			past.setString(8, user_name);
			past.setString(9, cDriverNo);
			past.setString(10, cDriverName);
			past.setString(11, TruckLicenseTag);
			past.setString(12, TruckNo);
			past.setString(13, String_Tool.DataBaseTime());
			int a = past.executeUpdate();
			return a;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
	}

	public static boolean Insert_into_Diao_bo_chu_ku(Connection conn, JSONArray array) { // 分店验单
		PreparedStatement past = null;
		String strrandom;
		double sum = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.get_diao_bo_chu_ku_no(conn, String_Tool.DataBaseYear(), obj.getString("cWhNo"));
			LoggerUtil.info(strrandom);

			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement past1 = conn.prepareStatement(
						"INSERT INTO wh_TfrWarehouseDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit) values (?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				past1.setString(6, "" + (String_Tool.String_IS_Four(Double.parseDouble(obj1.getString("fInPrice")))));
				double fInMoney = Double.parseDouble(obj1.getString("fInMoney"));
				sum = sum + fInMoney;
				past1.setString(7, "" + String_Tool.String_IS_Four(fInMoney));
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			past = conn.prepareStatement(
					"INSERT INTO wh_TfrWarehouse  (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,cWhNo,cWh,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,fMoney,cInStoreNo,cInStoreName,bExamin,cInWhNo,cInWh) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(10, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(11, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(12, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(13, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			past.setString(14, "" + sum);
			past.setString(15, obj.getString("cSupplierNo")); // 这是要转出的门店编号
			past.setString(16, obj.getString("cSupplier")); // dFillin,cFillinTime,cTime
			past.setString(17, obj.getString("bExamin"));
			past.setString(18, obj.getString("cInWhNo"));
			past.setString(19, obj.getString("cInWh"));
			past.execute();
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			try {
				conn.rollback();
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

	public static JSONArray select_Fresh_ru_ku(Connection conn, String No, String fage) {
		PreparedStatement past = null;
		ResultSet rs = null;
		JSONArray array = null;
		try {
			String sql = "";
			if (fage.equals("0")) {
				sql = " select dDate,cSheetno,cOutSerNo  from wh_cStoreOutWarehouse  where ISNULL (bfresh,0)=1 and ISNULL (bReceive,0)=0 and cCustomerNo=? ";
				past = conn.prepareStatement(sql);
				past.setString(1, No); // 店铺编号
				rs = past.executeQuery();
				array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			} else {
				sql = "  select a.dDate,a.cSheetno,a.cCustomerNo,a.cCustomer,a.cOperator,a.cOperatorNo,a.cFillEmp,a.cFillEmpNo,a.dFillin,a.cStockDptno,a.cStockDpt,a.fMoney,a.cWhNo,a.cWh,a.cStoreNo,a.cStoreName,a.cOutSerNo,b.cGoodsNo,b.cGoodsName,b.cBarcode,b.cUnitedNo,b.fQuantity,b.fInPrice,b.fInMoney,b.dProduct,b.cUnit,b.cSpec from wh_cStoreOutWarehouse a, wh_cStoreOutWarehouseDetail b where  a.cSheetno=b.cSheetno and a.cSheetno=? ";
				past = conn.prepareStatement(sql);
				past.setString(1, No); //
				rs = past.executeQuery();
				array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			}
			return array;
		} catch (Exception e) {
			return null;
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
	}

	public static boolean Insert_into_Diao_bo_ru_ku(Connection conn, JSONArray array) { // 分店验单
		PreparedStatement past = null;
		String strrandom;
		double sum = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.get_diao_bo_ru_ku_no(conn, String_Tool.DataBaseYear(), obj.getString("cWhNo"));
			LoggerUtil.info(strrandom);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement past1 = conn.prepareStatement(
						"INSERT INTO wh_TfrInWarehouseDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit) values (?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				double fInMoney = Double.parseDouble(obj1.getString("fInMoney"));
				sum = sum + fInMoney;
				past1.setString(7, "" + String_Tool.String_IS_Four(fInMoney));
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			past = conn.prepareStatement("INSERT INTO Wh_TfrInWarehouse  (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,cWhNo,cWh,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,fMoney,cInStoreNo,cInStoreName,bExamin,cInWhNo,cInWh,dbSheetNo) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cInStoreNo"));
			past.setString(4, obj.getString("cInStoreName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(10, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(11, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(12, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(13, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			past.setString(14, "" + sum);
			past.setString(15, obj.getString("cStoreNo")); // 和入库正好相反
			past.setString(16, obj.getString("cStoreName")); // dFillin,cFillinTime,cTime
			past.setString(17, "1");
			past.setString(18, obj.getString("cInWhNo"));
			past.setString(19, obj.getString("cInWh")); // dbSheetNo
			past.setString(20, obj.getString("cSheetno")); // dbSheetNo
			past.execute();

			PreparedStatement past2 = conn
					.prepareStatement("UPDATE wh_TfrWarehouse set bReceive ='1' where cSheetno = ? ");
			past2.setString(1, obj.getString("cSheetno"));
			int a = past2.executeUpdate();
			DB.closePreparedStatement(past2);
			if (a > 0) {
				conn.commit();
				conn.setAutoCommit(true);
				return true;
			} else {
				conn.rollback();
				return false;
			}
		} catch (Exception e) {
			try {
				conn.rollback();
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

	public static boolean Insert_into_Bao_Sun(Connection conn, JSONArray array) { // 分店验单
		PreparedStatement past = null;
		String strrandom;
		double sum = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.get_bo_sun_no(conn, String_Tool.DataBaseYear(), obj.getString("cSupNo"));//cSupNo  cStoreNo
			LoggerUtil.info(strrandom);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement past1 = conn.prepareStatement(
						"INSERT INTO  wh_LossWarehouseDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fPrice,fMoney,dProduct,cGoodsNo,cUnit,cSupNo,cSupName) values (?,?,?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fPrice"))));
				double fInMoney = Double.parseDouble(obj1.getString("fMoney"));
				sum = sum + fInMoney;
				past1.setString(7, "" + fInMoney);
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.setString(11, obj1.getString("cSupNo"));
				past1.setString(12, obj1.getString("cSupName"));
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			past = conn.prepareStatement(
					"INSERT INTO  wh_LossWarehouse  (dDate,cSheetno,cStoreNo,cStoreName,cSupplierNo,cSupplier,cOperatorNo,cOperator,cWhNo,cWh,dFillin,cFillinTime,cFillEmpNo,cFillEmp,fMoney,cTime) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
//			past.setString(5, obj.getString("cStoreNo"));
//			past.setString(6, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cSupNo"));
			past.setString(6, obj.getString("cSupName"));
			past.setString(7, obj.getString("cOperatorNo"));
			past.setString(8, obj.getString("cOperator"));
			past.setString(9, obj.getString("cWhNo"));
			past.setString(10, obj.getString("cWh"));
			past.setString(11, String_Tool.DataBaseYear_Month_Day());
			past.setString(12, String_Tool.DataBaseH_M_S()); //
			past.setString(13, obj.getString("cOperatorNo")); //
			past.setString(14, obj.getString("cOperator")); //
			past.setString(15, "" + sum);
			past.setString(16, "" + String_Tool.DataBaseH_M_S());
			past.execute();
			LoggerUtil.info("到此1");
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				LoggerUtil.info("到此0");
				conn.rollback();
				return false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			LoggerUtil.info("到此1");
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
	}

	public static boolean Insert_into_Bao_Yi(Connection conn, JSONArray array) { // 分店验单
		PreparedStatement past = null;
		String strrandom;
		double sum = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.get_bo_yi_no(conn, String_Tool.DataBaseYear(), obj.getString("cStoreNo"));
			LoggerUtil.info(strrandom);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement past1 = conn.prepareStatement(
						"INSERT INTO  dbo.wh_EffusionWhDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit,cSupNo,cSupName) values (?,?,?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				double fInMoney = Double.parseDouble(obj1.getString("fInMoney"));
				sum = sum + fInMoney;
				past1.setString(7, "" + fInMoney);
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.setString(11, obj1.getString("cSupNo"));
				past1.setString(12, obj1.getString("cSupName"));
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			past = conn.prepareStatement(
					"INSERT INTO   dbo.wh_EffusionWh  (dDate,cSheetno,cStoreNo,cStoreName,cSupplierNo,cSupplier,cOperatorNo,cOperator,cWhNo,cWh,dFillin,cFillinTime,cFillEmpNo,cFillEmp,fMoney,cTime) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cStoreNo"));
			past.setString(6, obj.getString("cStoreName"));
			past.setString(7, obj.getString("cOperatorNo"));
			past.setString(8, obj.getString("cOperator"));
			past.setString(9, obj.getString("cWhNo"));
			past.setString(10, obj.getString("cWh"));
			past.setString(11, String_Tool.DataBaseYear_Month_Day());
			past.setString(12, String_Tool.DataBaseH_M_S()); //
			past.setString(13, obj.getString("cOperatorNo")); //
			past.setString(14, obj.getString("cOperator")); //
			past.setString(15, "" + sum);
			past.setString(16, "" + String_Tool.DataBaseH_M_S());
			past.execute();
			LoggerUtil.info("到此1");
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				LoggerUtil.info("到此0");
				conn.rollback();
				return false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			LoggerUtil.info("到此1");
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
	}

	public static JSONArray Select_Bao_Sun(Connection conn, String cStoreNo, String beginDate, String endDate) { // 查询已经审核的分店验收单,但是还没入库
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select a.cStoreNo,a.cSheetno,sum(c.fQuantity) as fQuantity from wh_LossWarehouse a,t_Store b,wh_LossWarehouseDetail c where a.cStoreNo=b.cStoreNo and a.bExamin='0'  and a.cStoreNo=? and b.cParentNo<>'--' and a.cSheetno=c.cSheetno and a.dDate between ? and ? group by a.cSheetno,a.cStoreNo");
			past.setString(1, cStoreNo);
			past.setString(2, beginDate);
			past.setString(3, endDate);
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

	public static JSONArray Select_Bao_Yi(Connection conn, String cStoreNo, String beginDate, String endDate) { // 查询已经审核的分店验收单,但是还没入库
		PreparedStatement past = null;
		ResultSet rs = null;
		try {

			if (cStoreNo.equals("000")) { // 总部用
				past = conn.prepareStatement(" select a.cStoreName,b.* from t_Store a,(select a.cStoreNo,sum(c.fQuantity) as fQuantity from wh_EffusionWh a,t_Store b,wh_EffusionWhDetail c  where a.cStoreNo=b.cStoreNo and a.bExamin='0' and b.cParentNo<>'--'  and a.cSheetno=c.cSheetno and a.dDate between ? and ? group by a.cStoreNo) b where a.cStoreNo=b.cStoreNo");

				past.setString(1, beginDate);
				past.setString(2, endDate);

				rs = past.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
				return array;
			} else { // 门店用
				past = conn.prepareStatement(
						"select a.cStoreNo,a.cSheetno,sum(c.fQuantity) as fQuantity from wh_EffusionWh a,t_Store b,wh_EffusionWhDetail c where a.cStoreNo=b.cStoreNo and a.cStoreNo=? and a.bExamin='0' and b.cParentNo<>'--' and a.cSheetno=c.cSheetno and a.dDate between ? and ? group by a.cSheetno,a.cStoreNo");
				past.setString(1, cStoreNo);
				past.setString(2, beginDate);
				past.setString(3, endDate);
				rs = past.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
				return array;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return null;
	}

	public static boolean Insert_into_Return_Spplier(Connection conn, JSONArray array) { // 退货给供应商
		PreparedStatement past = null;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			String cSheetno = GetcSheetno.get_tui_huo_dao_gong_ying_shang_no(conn, String_Tool.DataBaseYear(),
					obj.getString("cSupplierNo"));
			String price = "0";
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				price = String_Tool.isJia(Double.parseDouble(price), Double.parseDouble(obj1.getString("fInMoney")));

				PreparedStatement past1 = conn.prepareStatement(
						"INSERT INTO dbo.wh_RbdWarehouseDetail (cSheetno,iLineNo,cGoodsNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,cUnit,cSpec,dProduct,fTimes) values(?,?,?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, cSheetno);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("cGoodsName"));
				past1.setString(5, obj1.getString("cBarcode"));
				past1.setString(6, obj1.getString("fQuantity"));
				past1.setString(7, obj1.getString("fInPrice"));
				past1.setString(8, obj1.getString("fInMoney"));
				past1.setString(9, obj1.getString("cUnit"));
				past1.setString(10, obj1.getString("cSpec"));
				past1.setString(11, String_Tool.DataBaseTime());
				past1.setString(12, "0.00");
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			past = conn.prepareStatement(
					"INSERT INTO wh_RbdWarehouse  (dDate,cSheetno,cSupplierNo,cSupplier,cOperatorNo,cOperator,cStoreNo,cStoreName,cFillEmpNo,cFillEmp,dFillin,cFillinTime,cStockDptno,cStockDpt,fMoney,bAgree,cExaminerNo,cExaminer,bExamin,cWhNo,cWh,bPost,cTime,bAccount,bAccount_log) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, cSheetno);
			past.setString(3, obj.getString("cSupplierNo"));
			past.setString(4, obj.getString("cSupplier"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.getString("cStoreNo"));
			past.setString(8, obj.getString("cStoreName"));
			past.setString(9, obj.getString("cOperatorNo"));
			past.setString(10, obj.getString("cOperator"));
			past.setString(11, String_Tool.DataBaseTime());
			past.setString(12, String_Tool.DataBaseH_M_S());
			past.setString(13, "0");
			past.setString(14, "");
			past.setString(15, price);
			past.setString(16, "0");
			past.setString(17, obj.getString("cOperatorNo"));
			past.setString(18, obj.getString("cOperator"));
			past.setString(19, "0");
			past.setString(20, obj.getString("cWhNo"));
			past.setString(21, obj.getString("cWh"));
			past.setString(22, "0");
			past.setString(23, String_Tool.DataBaseH_M_S());
			past.setString(24, "0");
			past.setString(25, "0");

			past.execute();
			LoggerUtil.info("到此1");
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				LoggerUtil.info("到此0");
				conn.rollback();
				return false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			LoggerUtil.info("到此1");
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
	}

	// 返回当前门店所有报损单
	public static JSONArray Select_Bao_Sun_Dan(Connection conn, String cStoreNo, String beginDate, String endDate) { // 查询已经审核的分店验收单,但是还没入库
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			LoggerUtil.info("zz");
			past = conn.prepareStatement(
					"select cSheetno,SUM(fQuantity) as fQuantity from wh_LossWarehouseDetail where cSheetno in(select cSheetno from wh_LossWarehouse where cStoreNo=? and dDate between ? and ?) group by cSheetno");
			past.setString(1, cStoreNo);
			past.setString(2, beginDate);
			past.setString(3, endDate);
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

	// 返回当前门店所有报益单子
	public static JSONArray Select_Bao_Yi_Dan(Connection conn, String cStoreNo, String beginDate, String endDate) { // 查询已经审核的分店验收单,但是还没入库
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			LoggerUtil.info("zz");
			past = conn.prepareStatement(
					"select cSheetno,SUM(fQuantity) as fQuantity from wh_EffusionWhDetail where cSheetno in(select cSheetno from wh_EffusionWh where cStoreNo=? and dDate between ? and ?) group by cSheetno");
			past.setString(1, cStoreNo);
			past.setString(2, beginDate);
			past.setString(3, endDate);
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

	// 查询报损商品详情
	public static JSONArray Select_Bao_Sun_Goods(Connection storeConn, String cStoreNo) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = storeConn.prepareStatement("select * from wh_LossWarehouseDetail where cSheetno=? ");
			past.setString(1, cStoreNo);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(storeConn);
		}
		return null;
	}

	// 查询商品报益详情
	public static JSONArray Select_Bao_Yi_Goods(Connection storeConn, String cStoreNo) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {

			past = storeConn.prepareStatement("select * from wh_EffusionWhDetail where cSheetno=? ");
			past.setString(1, cStoreNo);

			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(storeConn);
		}
		return null;
	}
	/**
	 * 
	 * @param storeConn  采购单
	 * @param cStoreNo
	 * @param dBeginDate
	 * @param dEndDate
	 * @return
	 */
	public static JSONArray Select_Zhi_Pei_Supplier(Connection storeConn, String cStoreNo, String dBeginDate,
			String dEndDate) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = storeConn.prepareStatement(
"select cSupplierNo,cSupplier,cSheetno from WH_Stock where dDate between ? and ? and cStoreNo=? and bExamin=1 and  isnull(bReceive,0)='0' and isnull(isheetType,0)=0  ");// and
																																			// bExamin='1'
			past.setString(1, dBeginDate);
			past.setString(2, dEndDate);
			past.setString(3, cStoreNo);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(storeConn);
		}
		return null;
	}
	
	/**
	 * 
	 * @param storeConn  查询供应商单号
	 * @param cStoreNo
	 * @param dBeginDate
	 * @param dEndDate
	 * @return
	 */
	public static JSONArray select_Supper_Goods_SheetNo_function(Connection storeConn, String cStoreNo, String dBeginDate,
			String dEndDate) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = storeConn.prepareStatement(
"select cSheetno,cSupplierNo=cStoreNo,cSupplier=cStoreName,ck=cWhNo+CHAR(32)+cWh from WH_cStoreReturnGoods  where dDate between ? and ?  and isnull(bPresent,0)<>1 and isnull(bExamin,0)=1 ");// and
																																			// bExamin='1'
			past.setString(1, dBeginDate);
			past.setString(2, dEndDate);
			//past.setString(3, cStoreNo);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(storeConn);
		}
		return null;
	}

	// 查询门店直配采购单的商品
	public static JSONArray Select_Zhi_Pei_Goods(Connection storeConn, String cSheetno) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = storeConn.prepareStatement("select * from WH_StockDetail where cSheetno=?");
			past.setString(1, cSheetno);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(storeConn);
		}
		return null;
	}

	public static JSONArray Select_Supplier(Connection storeConn) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = storeConn.prepareStatement(
					" select cSupNo,cSupName,cPassword,cSupAbbName,cPhone,cFax  cHezuoFangshi,cHelpCode  from t_Supplier");
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, storeConn);
		}
		return null;
	}

	public static JSONArray select_returnGoodsD(Connection storeConn, String cSheetno) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = storeConn.prepareStatement(
"select * from WH_cStoreReturnGoodsDetail where cSheetno=? ");// and
																																			// bExamin='1'
			
			past.setString(1, cSheetno);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(storeConn);
		}
		return null;
	}

	public static boolean Insert_into_Diao_bo_chu_ku(Connection conn, JSONArray array, String cBeizhu1) {
		PreparedStatement past = null;
		String strrandom;
		double sum = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.get_diao_bo_chu_ku_no(conn, String_Tool.DataBaseYear(), obj.getString("cWhNo"));
			LoggerUtil.info(strrandom);

			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement past1 = conn.prepareStatement(
						"INSERT INTO wh_TfrWarehouseDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit) values (?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				past1.setString(6, "" + (String_Tool.String_IS_Four(Double.parseDouble(obj1.getString("fInPrice")))));
				double fInMoney = Double.parseDouble(obj1.getString("fInMoney"));
				sum = sum + fInMoney;
				past1.setString(7, "" + String_Tool.String_IS_Four(fInMoney));
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			past = conn.prepareStatement(
					"INSERT INTO wh_TfrWarehouse  (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,cWhNo,cWh,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,fMoney,cInStoreNo,cInStoreName,bExamin,cInWhNo,cInWh,cBeizhu1) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(10, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(11, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(12, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(13, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			past.setString(14, "" + sum);
			past.setString(15, obj.getString("cSupplierNo")); // 这是要转出的门店编号
			past.setString(16, obj.getString("cSupplier")); // dFillin,cFillinTime,cTime
			past.setString(17, obj.getString("bExamin"));
			past.setString(18, obj.getString("cInWhNo"));
			past.setString(19, obj.getString("cInWh"));
			past.setString(20, cBeizhu1);
			past.execute();
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			try {
				conn.rollback();
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

	public static boolean Insert_into_Return_Spplier(Connection conn, JSONArray array, String cBeizhu1) {
		PreparedStatement past = null;
		String strrandom;
		double sum = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.get_diao_bo_chu_ku_no(conn, String_Tool.DataBaseYear(), obj.getString("cWhNo"));
			LoggerUtil.info(strrandom);

			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement past1 = conn.prepareStatement(
						"INSERT INTO wh_TfrWarehouseDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit) values (?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				past1.setString(6, "" + (String_Tool.String_IS_Four(Double.parseDouble(obj1.getString("fInPrice")))));
				double fInMoney = Double.parseDouble(obj1.getString("fInMoney"));
				sum = sum + fInMoney;
				past1.setString(7, "" + String_Tool.String_IS_Four(fInMoney));
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			past = conn.prepareStatement(
					"INSERT INTO wh_TfrWarehouse  (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,cWhNo,cWh,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,fMoney,cInStoreNo,cInStoreName,bExamin,cInWhNo,cInWh,cBeizhu1) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(10, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(11, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(12, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(13, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			past.setString(14, "" + sum);
			past.setString(15, obj.getString("cSupplierNo")); // 这是要转出的门店编号
			past.setString(16, obj.getString("cSupplier")); // dFillin,cFillinTime,cTime
			past.setString(17, obj.getString("bExamin"));
			past.setString(18, obj.getString("cInWhNo"));
			past.setString(19, obj.getString("cInWh"));
			past.setString(20, cBeizhu1);
			past.execute();
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			try {
				conn.rollback();
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

	public static boolean Insert_into_Return_Spplier1(Connection conn, JSONArray array, String cBeizhu1) {
		PreparedStatement past = null;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			String cSheetno = GetcSheetno.get_tui_huo_dao_gong_ying_shang_no(conn, String_Tool.DataBaseYear(),
					obj.getString("cSupplierNo"));
			String price = "0";
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				price = String_Tool.isJia(Double.parseDouble(price), Double.parseDouble(obj1.getString("fInMoney")));

				PreparedStatement past1 = conn.prepareStatement(
						"INSERT INTO dbo.wh_RbdWarehouseDetail (cSheetno,iLineNo,cGoodsNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,cUnit,cSpec,dProduct,fTimes) values(?,?,?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, cSheetno);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("cGoodsName"));
				past1.setString(5, obj1.getString("cBarcode"));
				past1.setString(6, obj1.getString("fQuantity"));
				past1.setString(7, obj1.getString("fInPrice"));
				past1.setString(8, obj1.getString("fInMoney"));
				past1.setString(9, obj1.getString("cUnit"));
				past1.setString(10, obj1.getString("cSpec"));
				past1.setString(11, String_Tool.DataBaseTime());
				past1.setString(12, "0.00");
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			past = conn.prepareStatement(
					"INSERT INTO wh_RbdWarehouse  (dDate,cSheetno,cSupplierNo,cSupplier,cOperatorNo,cOperator,cStoreNo,cStoreName,cFillEmpNo,cFillEmp,dFillin,cFillinTime,cStockDptno,cStockDpt,fMoney,bAgree,cExaminerNo,cExaminer,bExamin,cWhNo,cWh,bPost,cTime,bAccount,bAccount_log,cBeizhu1) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, cSheetno);
			past.setString(3, obj.getString("cSupplierNo"));
			past.setString(4, obj.getString("cSupplier"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.getString("cStoreNo"));
			past.setString(8, obj.getString("cStoreName"));
			past.setString(9, obj.getString("cOperatorNo"));
			past.setString(10, obj.getString("cOperator"));
			past.setString(11, String_Tool.DataBaseTime());
			past.setString(12, String_Tool.DataBaseH_M_S());
			past.setString(13, "0");
			past.setString(14, "");
			past.setString(15, price);
			past.setString(16, "0");
			past.setString(17, obj.getString("cOperatorNo"));
			past.setString(18, obj.getString("cOperator"));
			past.setString(19, "0");
			past.setString(20, obj.getString("cWhNo"));
			past.setString(21, obj.getString("cWh"));
			past.setString(22, "0");
			past.setString(23, String_Tool.DataBaseH_M_S());
			past.setString(24, "0");
			past.setString(25, "0");//cBeizhu1
			past.setString(26, cBeizhu1);//cBeizhu1
			past.execute();
			LoggerUtil.info("到此1");
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				LoggerUtil.info("到此0");
				conn.rollback();
				return false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			LoggerUtil.info("到此1");
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
	}

}
