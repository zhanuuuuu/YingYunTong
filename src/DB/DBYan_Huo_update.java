package DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import Tool.GetcSheetno;
import Tool.ReadConfig;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

public class DBYan_Huo_update {

	public static JSONArray Tuo_Pan_Select_Box(Connection conn, // 从更具托盘号,查询箱子
			String no) { // 查询出已审核出库单的所有商品
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = " select cDistBoxNo ,cDistBoxNo_SortSheetNo, a.cStoreName,a.cStoreNo from wh_PalletSheet a ,wh_PalletSheetDetail  b where a.cSheetno=b.cSheetno and b.cPalletNo=?"; // //要明白此时的托盘都是已经审核的
			past = conn.prepareStatement(sql);
			past.setString(1, no);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, null, conn);
		}
		return null;
	}

	public static JSONArray Box_Select_Goods(Connection conn, // 根据箱子.或者分拣单,查商品
			String no) { // 查询出已审核出库单的所有商品
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call compare_Goods_fQuantity (?)}");
			c.setString(1, no);
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

	public static JSONArray Pallet_Select_Goods(Connection conn, // 根据托盘单号查商品
			String no) { // 查询出已审核出库单的所有商品
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call compare_Goods_fQuantity_from_pallet_no (?)}");
			c.setString(1, no);
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

	public static JSONArray Tuo_Pan_Select_Box(Connection conn) { // 更新已审核托盘单
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "select a.cSheetno,a.cStoreNo,a.cStoreName,a.cWhNo,a.cwh,b.cPalletNo,b.cDistBoxNo,b.cDistBoxNo_SortSheetNo  from wh_PalletSheet a,wh_PalletSheetDetail b where a.cSheetno=b.cSheetno and ISNULL(bExamin,0)=1"; // 更新已审核托盘单
			past = conn.prepareStatement(sql);

			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, null, conn);
		}
		return null;
	}

	public static String Yi_Yan_Tuo_Pan_Dan(Connection conn) { // 已验托盘单商品
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call yi_shen_tuo_pan_dan_Out }");
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			String array1 = "";
			if (c.getMoreResults()) {// 这个判断会自动指向下一个结果集
				ResultSet rs1 = c.getResultSet();
				array1 = ResultSet_To_JSON.resultSetToJson(rs1);
			}
			String str = "";
			if (array.length() > 0) {
				str = "{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "," + "\"dDate1\":"
						+ array1 + "}";
			} else {
				str = "{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "," + "\"dDate1\":"
						+ array1 + "}";
			}
			System.out.println(str);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(c, conn);
		}
		return null;
	}

	public static boolean insert_into_yan_huo_dan(Connection conn, JSONArray array) { // 提交配送验货单
		PreparedStatement past = null;
		String strrandom;
		PreparedStatement past1 = null;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			past = conn.prepareStatement(
					"INSERT INTO wh_cStoreOutVerifySheet (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,cWhNo,cWh,cSheetNo_Pallet,cCustomerNo,cCustomer,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,bExamin) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			strrandom = GetcSheetno.getGenPei_Song_Yan_Huo_dan_no(conn, String_Tool.DataBaseYear(),
					obj.getString("cCustomerNo"));
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, obj.getString("cSheetNo_Pallet"));
			past.setString(10, obj.getString("cCustomerNo"));
			past.setString(11, obj.getString("cCustomer"));
			past.setString(12, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(14, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(15, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(16, obj.getString("cOperator"));
			past.setString(17, obj.getString("bExamin"));
			past.execute();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past1 = conn.prepareStatement(
						"INSERT INTO wh_cStoreOutVerifySheetDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit,cUnitedNo,fPrice_SO,cOutiLineNo) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("Fen_Jian_Num"));
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				past1.setString(7, "" + Double.parseDouble(obj1.getString("fInMoney")));
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.setString(11, obj1.getString("cUnitedNo"));
				past1.setString(12, obj1.getString("Xu_Qiu_Num"));
				past1.setString(13, obj1.getString("iLineNo"));
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			PreparedStatement past2 = conn.prepareStatement("Update wh_PalletSheet set bReceive='1' where cSheetno=?");
			past2.setString(1, obj.getString("cSheetNo_Pallet"));
			past2.executeUpdate();
			DB.closePreparedStatement(past2);
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

	public static boolean insert_into_yan_huo_dan(Connection conn, JSONArray array_goods, JSONArray array1_pallet,JSONArray array_Cha_yi, JSONArray array_chu_ku_no) { // 提交配送验货单,
		String strrandom;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array_goods.getJSONObject(0);
			
			
			for (int i = 0; i < array1_pallet.length(); i++) {
				PreparedStatement past_select = conn.prepareStatement("select * from  wh_PalletSheet   where cSheetno=? and  bReceive='1' ");
				JSONObject obj1 = array1_pallet.getJSONObject(i);
				past_select.setString(1, obj1.getString("cSheetno"));
			    ResultSet rs=past_select.executeQuery();
				if(rs.next()){
					DB.closeResultSet(rs);
					DB.closePreparedStatement(past_select);
					System.out.println(obj1.getString("cSheetno"));
					System.out.println("此托盘单已经提交配送验货");
					return false;
				}
				DB.closeResultSet(rs);
				DB.closePreparedStatement(past_select);
				
			}
			PreparedStatement past = conn.prepareStatement("INSERT INTO wh_cStoreOutVerifySheet (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,cWhNo,cWh,cSheetNo_Pallet,cCustomerNo,cCustomer,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,bExamin,cOutSerNo) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			strrandom = GetcSheetno.getGenPei_Song_Yan_Huo_dan_no(conn, String_Tool.DataBaseYear(),obj.getString("cCustomerNo"));
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, "");
			past.setString(10, obj.getString("cCustomerNo"));
			past.setString(11, obj.getString("cCustomer"));
			past.setString(12, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(14, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(15, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(16, obj.getString("cOperator"));
			past.setString(17, obj.getString("bExamin"));
			past.setString(18, obj.getString("cOutSerNo"));
			past.execute();
			DB.closePreparedStatement(past);
			System.out.println(strrandom);

			PreparedStatement past1 = conn.prepareStatement(
					"INSERT INTO wh_cStoreOutVerifySheetDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit,cUnitedNo,fPrice_SO,cOutiLineNo) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < array_goods.length(); i++) {
				JSONObject obj1 = array_goods.getJSONObject(i);
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("Fen_Jian_Num"));
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				past1.setString(7, "" + Double.parseDouble(obj1.getString("fInMoney")));
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.setString(11, obj1.getString("cUnitedNo"));
				past1.setString(12, obj1.getString("Xu_Qiu_Num"));
				past1.setString(13, obj1.getString("iLineNo"));
				past1.addBatch();
				if (i % 200 == 0) {
					past1.executeBatch();
				}
			}
			past1.executeBatch();
			DB.closePreparedStatement(past1);

			PreparedStatement past31 = conn.prepareStatement("INSERT INTO  wh_cStoreOutVerifySheet_Pallet (dDate,cSheetNo,cSheetNo_Pallet)values(?,?,?)");
			PreparedStatement past2 = conn.prepareStatement("Update wh_PalletSheet set bReceive='1' where cSheetno=?");
			
			for (int i = 0; i < array1_pallet.length(); i++) {

				JSONObject obj1 = array1_pallet.getJSONObject(i);

				past31.setString(1, String_Tool.DataBaseYear_Month_Day());
				past31.setString(2, strrandom);
				past31.setString(3, obj1.getString("cSheetno"));
				past31.addBatch();
				if (i % 200 == 0) {
					past31.executeBatch();
				}
				past2.setString(1, obj1.getString("cSheetno"));
				past2.addBatch();
				if (i % 200 == 0) {
					past2.executeBatch();
				}
			}
			past2.executeBatch();
			past31.executeBatch();
			DB.closePreparedStatement(past2);
			DB.closePreparedStatement(past31);

			for (int i = 0; i < array_chu_ku_no.length(); i++) {
				JSONObject obj1 = array_chu_ku_no.getJSONObject(i);
				PreparedStatement past3 = conn.prepareStatement("Update wh_cStoreOutWarehouse  set bShip='1' where cSheetno=?");
				past3.setString(1, obj1.getString("cSheetno_Out"));
				past3.executeUpdate();
				DB.closePreparedStatement(past3);
			}

			insert_into_yan_huo_cha_yi_dan(conn, array_Cha_yi, strrandom);
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

			DB.closeConn(conn);
		}
		return false;
	}

	public static boolean insert_into_yan_huo_cha_yi_dan(Connection conn, JSONArray array, String cSheetno) { // 提交配送验差异单
		String strrandom;
		try {
			JSONObject obj = array.getJSONObject(0);
			PreparedStatement past = conn
					.prepareStatement("INSERT INTO wh_cStoreOutVerifySheet_Diff (dDate,cSheetno,cStoreNo,"
							+ "cStoreName,cOperatorNo,cOperator,cWhNo,cWh,jiesuanno,cCustomerNo,cCustomer,dFillin,cFillinTime,cFillEmpNo,cFillEmp) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			strrandom = GetcSheetno.get_peisong_cha_yi_no(conn, String_Tool.DataBaseYear(),
					obj.getString("cCustomerNo"));
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, cSheetno); // 配送出库验货单
			past.setString(10, obj.getString("cCustomerNo"));
			past.setString(11, obj.getString("cCustomer"));
			past.setString(12, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(14, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(15, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			past.execute();
			DB.closePreparedStatement(past);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement past1 = conn.prepareStatement(
						"INSERT INTO wh_cStoreOutVerifySheetDetail_Diff (cSheetno,iLineNo,cGoodsName,cbarcode,fQuantity_Sort,fQuantity_Out,fQuantity,cGoodsNo,cUnitedNo,cOutiLineNo) values (?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				String Fen_Jian_Num = obj1.getString("Fen_Jian_Num");
				past1.setString(5, Fen_Jian_Num);
				String Xu_Qiu_Num = obj1.getString("Xu_Qiu_Num");
				past1.setString(6, Xu_Qiu_Num);
				double fQuantity = Double.parseDouble(Fen_Jian_Num) - Double.parseDouble(Xu_Qiu_Num);
				past1.setString(7, "" + fQuantity);
				past1.setString(8, obj1.getString("cGoodsNo"));
				past1.setString(9, obj1.getString("cUnitedNo"));
				past1.setString(10, obj1.getString("iLineNo"));
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static JSONArray Select_Yi_Shen_zhuang_che_dan(Connection conn, String storeno) { // 更新已审核装车单
		CallableStatement c = null;
		ResultSet rs = null;
		try {// '1001',
			c = conn.prepareCall("{call p_GetcStoreShipShenHeNotYh_Wei2 (?)}");
			c.setString(1, storeno);
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
			DB.closeConn(conn);
		}
		return null;
	}

	public static JSONArray Select_Yi_Shen_yan_huo_dan(Connection conn) { // 查询已审核验货单
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call  yi_shen_yan_huo_dan_goods2 }");
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
			DB.closeConn(conn);
		}
		return null;
	}

	public static boolean insert_into_ru_ku_dan(Connection conn, JSONArray array) { // 提交门店入库
		PreparedStatement past = null;
		String strrandom;
		double fmoney = 0;
		double num = 0;
		ResultSet rs = null;
		String bReceive = "";
		try {
			JSONObject object0 = array.getJSONObject(0);
			String cSheetno = object0.getString("cSheetno");
			Connection conn0 = GetConnection.getStoreConn(object0.getString("cStoreNo"));
			PreparedStatement past0 = conn0.prepareStatement(" select top 1 bReceive from wh_StockVerify  where cSheetno = ? ");
			past0.setString(1, cSheetno);
			rs = past0.executeQuery();
			while (rs.next()) {
				bReceive = rs.getString("bReceive");
			}
			DB.closeResultSet(rs);
			DB.closeConn(conn0);
			System.out.println("提交入库接口bReceive的状态"+bReceive);
			if (!String_Tool.isEmpty(bReceive) && bReceive.equals("1")) {//已经入库了返回true
				return true;
			} else {
				try {
					
					//Now_fQuantity
					conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
					JSONObject obj = array.getJSONObject(0);
					strrandom = GetcSheetno.getfen_dian_ru_ku_dan_no(conn, String_Tool.DataBaseYear(),obj.getString("cSupplierNo"));
					System.out.println(obj.getString("cSupplierNo"));
					System.out.println(strrandom);
					PreparedStatement past1 = conn.prepareStatement("INSERT INTO wh_InWarehouseDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit,cUnitedNo,fQty_Original) values (?,?,?,?,?,?,?,?,?,?,?,?)");
					PreparedStatement past11 = conn.prepareStatement("update t_cStoreGoods set  fCKPrice=?,cCkPriceInSheetno=? where cStoreNo=? and cGoodsNo=? ");
					
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj1 = array.getJSONObject(i);
						past1.setString(1, "" + strrandom);
						past1.setString(2, "" + (i + 1));
						past1.setString(3, obj1.getString("cGoodsName"));
						past1.setString(4, obj1.getString("cBarcode"));
						past1.setString(5, obj1.getString("fQuantity"));
						past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
						past1.setString(7, "" + Double.parseDouble(obj1.getString("fInMoney")));
						fmoney = Double.parseDouble(obj1.getString("fInMoney")) + fmoney;
						num = Double.parseDouble(obj1.getString("fQuantity")) + num;
						String dProduct = obj1.getString("dProduct");
						if (String_Tool.isEmpty(dProduct)) {
							dProduct = String_Tool.DataBaseYear_Month_Day();
						}
						past1.setString(8, dProduct);
						past1.setString(9, obj1.getString("cGoodsNo"));
						past1.setString(10, obj1.optString("cUnit"));//
						past1.setString(11, obj1.optString("cUnitedNo"));
						past1.setString(12, obj1.optString("Now_fQuantity"));
						past1.addBatch();
						if (i % 100 == 0) {
							past1.executeBatch();
						}
						// 更改
						past11.setString(1, obj1.getString("fInPrice"));
						past11.setString(2, strrandom);
						past11.setString(3, obj.getString("cStoreNo"));
						past11.setString(4, obj1.getString("cGoodsNo"));
						past11.addBatch();

						if (i % 200 == 0) {
							past11.executeBatch();
						}
					}
					past1.executeBatch();
					past11.executeBatch();
					DB.closePreparedStatement(past1);
					DB.closePreparedStatement(past11);
					past = conn.prepareStatement(
							"INSERT INTO wh_InWarehouse  (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,cWhNo,cWh,jiesuanno,cSupplierNo,cSupplier,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,fMoney,fQty,bExamin) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					past.setString(1, String_Tool.DataBaseYear_Month_Day());
					past.setString(2, "" + strrandom);
					past.setString(3, obj.getString("cStoreNo"));
					past.setString(4, obj.getString("cStoreName"));
					past.setString(5, obj.getString("cOperatorNo"));
					past.setString(6, obj.getString("cOperator"));
					past.setString(7, obj.optString("cWhNo"));
					past.setString(8, obj.optString("cWh"));
					past.setString(9, obj.getString("cSheetno"));
					past.setString(10, obj.getString("cSupplierNo"));
					past.setString(11, obj.getString("cSupplier"));
					past.setString(12, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
					past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
					past.setString(14, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
					past.setString(15, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
					past.setString(16, obj.getString("cOperator"));
					past.setString(17, "" + fmoney);
					past.setString(18, "" + num);
					past.setString(19, "1");
					past.execute();

					// 把分店的验货单变为已经结束
					Connection conn1 = GetConnection.getStoreConn(obj.getString("cStoreNo"));
					PreparedStatement past2 = conn1
							.prepareStatement("UPdate wh_StockVerify  set bReceive ='1' where cSheetno = ? ");
					past2.setString(1, obj.getString("cSheetno"));
					int a = past2.executeUpdate();
					System.out.println(a);
					DB.closePreparedStatement(past2);
					DB.closeConn(conn1);

					// 把总部的验货单变为已经接收
					PreparedStatement past3 = conn
							.prepareStatement("UPdate wh_StockVerify  set bReceive ='1' where cSheetno = ? ");
					past3.setString(1, obj.getString("cSheetno"));
					int b = past3.executeUpdate();
					System.out.println(b);
					DB.closePreparedStatement(past3);
					if (a > 0 || b > 0) {
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeConn(conn);
		}
		return false;
	}

	public static boolean insert_into_fresh_ru_ku_dan(Connection conn, JSONArray array) { // 提交门店入库
		PreparedStatement past = null;
		String strrandom;
		PreparedStatement past1 = null;
		double fmoney = 0;
		double num = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.getfen_dian_ru_ku_dan_no(conn, String_Tool.DataBaseYear(),obj.getString("cStoreNo"));
			System.out.println(strrandom);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past1 = conn.prepareStatement(
						"INSERT INTO wh_InWarehouseDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit,cUnitedNo) values (?,?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				past1.setString(7, "" + Double.parseDouble(obj1.getString("fInMoney")));
				fmoney = Double.parseDouble(obj1.getString("fInMoney")) + fmoney;
				num = Double.parseDouble(obj1.getString("fQuantity")) + num;
				String dProduct = obj1.getString("dProduct");
				if (String_Tool.isEmpty(dProduct)) {
					dProduct = String_Tool.DataBaseYear_Month_Day();
				}
				past1.setString(8, dProduct);
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.setString(11, obj1.optString("cUnitedNo"));
				past1.execute();
				DB.closePreparedStatement(past1);

				// 更改
				PreparedStatement past11 = conn.prepareStatement(
						"update t_cStoreGoods set  fCKPrice=?,cCkPriceInSheetno=? where cStoreNo=? and cGoodsNo=? ");
				past11.setString(1, obj1.getString("fInPrice"));
				past11.setString(2, strrandom);
				past11.setString(3, obj.getString("cStoreNo"));
				past11.setString(4, obj1.getString("cGoodsNo"));
				past11.execute();
				DB.closePreparedStatement(past11);
			}
			past = conn.prepareStatement(
					"INSERT INTO wh_InWarehouse  (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,cWhNo,cWh,jiesuanno,cSupplierNo,cSupplier,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,fMoney,fQty,bExamin) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, obj.getString("dDate"));
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cCustomerNo"));
			past.setString(4, obj.getString("cCustomer"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, obj.getString("cSheetno"));
			past.setString(10, obj.getString("cStoreNo"));
			past.setString(11, obj.getString("cStoreName"));
			past.setString(12, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(14, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(15, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(16, obj.getString("cOperator"));
			past.setString(17, "" + fmoney);
			past.setString(18, "" + num);
			past.setString(19, "1");
			past.execute();

			PreparedStatement past2 = conn.prepareStatement(
					"UPdate wh_cStoreOutWarehouse   set  bShip='1', bOutYH='1',  bReceive ='1' where cSheetno = ? ");
			past2.setString(1, obj.getString("cSheetno"));
			int a = past2.executeUpdate();
			System.out.println(a);
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

	public static boolean insert_into_fen_dan_tui_huo(Connection conn, JSONArray array) { // 提交分店退货
		PreparedStatement past = null;
		String strrandom;
		double Sum_Money = 0;
		PreparedStatement past1 = null;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.getfen_dian_tui_huo_no(conn, String_Tool.DataBaseYear(),
					obj.getString("cClientNo"));
			System.out.println(strrandom);
			past1 = conn.prepareStatement(
					"INSERT INTO WH_cStoreReturnGoodsDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit) values (?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);

				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				double fQuantity=Double.parseDouble(obj1.getString("fQuantity"));
				double fInPrice= Double.parseDouble(obj1.getString("fInPrice"));
				double fInMoney=fQuantity*fInPrice;
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				past1.setString(7, "" + Double.parseDouble(obj1.getString("fInMoney")));
				Sum_Money = fInMoney + Sum_Money;
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.addBatch();
				if (i % 200 == 0) {
					past1.executeBatch();
				}
			}
			past1.executeBatch();
			DB.closePreparedStatement(past1);
			past = conn.prepareStatement(
					"INSERT INTO WH_cStoreReturnGoods (dDate,cSheetno,cClientNo,cClient,cOperatorNo,cOperator,cWhNo,cWh,cStoreNo,cStoreName,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,bExamin,fMoney,cSupNo,cSupName) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cClientNo"));
			past.setString(4, obj.getString("cClient"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, obj.getString("cStoreNo"));
			past.setString(10, obj.getString("cStoreName"));
			past.setString(11, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(12, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(14, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(15, obj.getString("cOperator"));
			past.setString(16, obj.optString("bExamin"));
			past.setString(17, "" + Sum_Money);
			past.setString(18,obj.optString("cSupplierNo"));
			past.setString(19,obj.optString("cSupplier"));
			past.execute();
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

	public static boolean insert_into_diao_bo_chu_ku(Connection conn, JSONArray array) { // 调拨出库
		PreparedStatement past = null;
		String strrandom;
		double fInMoney = 0;
		PreparedStatement past1 = null;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.getfen_dian_tui_huo_no(conn, String_Tool.DataBaseYear(),obj.getString("cSupplierNo"));
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past1 = conn.prepareStatement(
						"INSERT INTO WH_cStoreReturnGoodsDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit) values (?,?,?,?,?,?,?,?,?,?)");
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				past1.setString(7, "" + Double.parseDouble(obj1.getString("fInMoney")));
				fInMoney = Double.parseDouble(obj1.getString("fInMoney")) + fInMoney;
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			past = conn.prepareStatement(
					"INSERT INTO WH_cStoreReturnGoods (dDate,cSheetno,cClientNo,cClient,cOperatorNo,cOperator,cWhNo,cWh,cStoreNo,cStoreName,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,bExamin,fMoney) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cSupplierNo"));
			past.setString(4, obj.getString("cSupplier"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, obj.getString("cStoreNo"));
			past.setString(10, obj.getString("cStoreName"));
			past.setString(11, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(12, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(14, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(15, obj.getString("cOperator"));
			past.setString(16, "1");
			past.setString(17, "" + fInMoney);
			past.execute();
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

	public static JSONArray diao_bo_chu_ku_Select(Connection conn, String no) { // //
																				// 调拨出库查询
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call diao_bo_chu_ku_Select (?)}");
			c.setString(1, no);
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

	public static JSONArray select_ru_ku_goods(Connection conn, String no, String dDate1, String dDate2) { // //

		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = " select dDate,cSheetno from   wh_InWarehouse where cStoreNo=? and dDate between ? and  ?";
			past = conn.prepareStatement(sql);
			past.setString(1, no);
			past.setString(2, dDate1);
			past.setString(3, dDate2);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		return null;
	}

	public static JSONArray select_ru_ku_goods(Connection conn, String no) { // //

		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "select  cGoodsNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,cUnit,cSpec,fPrice_SO from   wh_InWarehouseDetail where cSheetno=? ";
			past = conn.prepareStatement(sql);
			past.setString(1, no);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		return null;
	}

	public static boolean insert_into_print_price(Connection conn, JSONArray array) { // 打印价签
		PreparedStatement past = null;
		String strrandom;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = obj.getString("cStoreNo") + String_Tool.reformat(); // 单号
			System.out.println(strrandom);
			String sql = "INSERT INTO t_StorePrint (dDate,cStoreNo,cSheetNo,cStoreName,cOperatorNo,cOperator,bPrint)values(?,?,?,?,?,?,?)";
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
						"INSERT INTO t_StorePrintDetail (cSheetNo,iLineNo,cGoodsNo,fQuantity) values (?,?,?,?)");
				past1.setString(1, strrandom);
				past1.setString(2, "" + i);
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("fQuantity"));
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

	public static boolean insert_into_men_dian_zhi_pei(Connection conn, JSONArray array) { // 打印价签H+
		PreparedStatement past = null;
		String strrandom;
		double fMoney = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.get_men_dian_zhi_pei_no(conn, String_Tool.DataBaseYear(),obj.getString("cSupplierNo"));
			System.out.println(strrandom);
			PreparedStatement past1 = conn.prepareStatement(
					"INSERT INTO wh_StoreInWarehouseDetail (cSheetNo,iLineNo,cGoodsNo,cBarcode,cGoodsName,fQuantity,fInPrice,fInMoney,fTaxPrice,fTaxMoney,fNoTaxPrice,fNoTaxMoney,dProduct,fTimes) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			double fQty = 0.0;
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past1.setString(1, strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("cGoodsName"));
				past1.setString(6, obj1.getString("fQuantity"));
				PreparedStatement past11 = conn.prepareStatement("select fInMoney from dbo.t_Supplier_goods where cGoodsNo=? and cSupNo =? ");
				past11.setString(1, obj1.getString("cGoodsNo"));
				past11.setString(2, obj1.getString("cSupplierNo"));
				ResultSet rs = past11.executeQuery();
				String fInPrice1=obj1.getString("fInPrice");
				String fInPrice = "0.0";
				if (rs.next()) {
					fInPrice = rs.getString("fInMoney");
				}
				if(Double.parseDouble(fInPrice1)<Double.parseDouble(fInPrice)){
					fInPrice=fInPrice1;
				}
				DB.closeResultSet(rs);
				DB.closePreparedStatement(past11);

				past1.setString(7, fInPrice);
				double fInMoney = Double.parseDouble(obj1.getString("fQuantity")) * Double.parseDouble(fInPrice);
				past1.setString(8, "" + String_Tool.String_IS_Two(fInMoney));

				String fNormalPrice = obj1.getString("fNormalPrice");
				if (String_Tool.isEmpty(fNormalPrice)) {
					fNormalPrice = "" + 0.0;
				}
				if (String_Tool.isEmpty(fInPrice)) {
					fInPrice = "" + 0.0;
				}
				String fQuantity = obj1.getString("fQuantity");
				if (String_Tool.isEmpty(fQuantity)) {
					fQuantity = "" + 0.0;
				}
				fQty = fQty + Double.parseDouble(fQuantity);
				double faxPrice = Double.parseDouble(fNormalPrice) - Double.parseDouble(fInPrice);
				past1.setString(9, "" + String_Tool.String_IS_Two(faxPrice));
				past1.setString(10, "" + String_Tool.String_IS_Two(faxPrice * Double.parseDouble(fQuantity)));
				past1.setString(11, "" + String_Tool.String_IS_Two(fNormalPrice));
				past1.setString(12, "" + String_Tool
						.String_IS_Two(Double.parseDouble(fNormalPrice) * (Double.parseDouble(fQuantity))));
				past1.setString(13, "" + obj1.optString("dProduct")); //
				System.out.println(obj1.optString("dProduct"));
				past1.setString(14, "" + obj1.optString("fTimes"));
				fMoney = Double.parseDouble(String_Tool.String_IS_Two(fInMoney)) + fMoney;
				past1.addBatch();
			}
			past1.executeBatch();
			DB.closePreparedStatement(past1);
			String sql = "INSERT INTO wh_StoreInWarehouse (dDate,cSheetNo,cSupplierNo,cSupplier,cOperatorNo,cOperator,cWhNo,cWh,fQty,cStoreNo,cStoreName,bReceive,dFillin,cFillinTime,cTime,fMoney,cFillEmpNo,cFillEmp)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			past = conn.prepareStatement(sql);
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, strrandom);
			past.setString(3, obj.getString("cSupplierNo"));
			past.setString(4, obj.getString("cSupplier"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.getString("cWhNo"));
			past.setString(8, obj.getString("cWh"));
			past.setString(9, "" + String_Tool.String_IS_Two(fQty));
			past.setString(10, obj.getString("cStoreNo"));
			past.setString(11, obj.getString("cStoreName"));
			past.setString(12, "0");
			past.setString(13, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(14, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(15, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(16, "" + fMoney); // dFillin,cFillinTime,cTime
			past.setString(17, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(18, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			// past.setString(19, ""+fQty); // dFillin,cFillinTime,cTime
			past.execute();
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

	public static boolean insert_into_men_dian_zhi_peiLizhao(Connection conn, JSONArray array) { // 门店直配李钊
		PreparedStatement past = null;
		String strrandom;
		double fMoney = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.get_men_dian_zhi_pei_no(conn, String_Tool.DataBaseYear(),obj.getString("cSupplierNo"));
			System.out.println(strrandom);
			PreparedStatement past1 = conn.prepareStatement("INSERT INTO wh_StoreInWarehouseDetail (cSheetNo,iLineNo,cGoodsNo,cBarcode,cGoodsName,fQuantity,fInPrice,fInMoney,fTaxPrice,fTaxMoney,fNoTaxPrice,fNoTaxMoney,dProduct,fTimes) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			double fQty = 0.0;
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past1.setString(1, strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("cGoodsName"));
				past1.setString(6, obj1.getString("fQuantity"));
				PreparedStatement past11 = conn.prepareStatement("select fCKPrice from t_cStoreGoods where cGoodsNo=? and cStoreNo =? ");
				past11.setString(1, obj1.getString("cGoodsNo"));
				past11.setString(2, obj1.getString("cStoreNo"));
				ResultSet rs = past11.executeQuery();
				String fInPrice = "0.0";
				if (rs.next()) {
					fInPrice = rs.getString("fCKPrice");
				}
				DB.closeResultSet(rs);
				DB.closePreparedStatement(past11);

				past1.setString(7, fInPrice);
				
				double fInMoney = Double.parseDouble(obj1.getString("fQuantity")) * Double.parseDouble(fInPrice);
				past1.setString(8, "" + String_Tool.String_IS_Two(fInMoney));

				String fNormalPrice = obj1.getString("fNormalPrice");
				if (String_Tool.isEmpty(fNormalPrice)) {
					fNormalPrice = "" + 0.0;
				}
				if (String_Tool.isEmpty(fInPrice)) {
					fInPrice = "" + 0.0;
				}
				String fQuantity = obj1.getString("fQuantity");
				if (String_Tool.isEmpty(fQuantity)) {
					fQuantity = "" + 0.0;
				}
				fQty = fQty + Double.parseDouble(fQuantity);
				double faxPrice = Double.parseDouble(fNormalPrice) - Double.parseDouble(fInPrice);
				past1.setString(9, "" + String_Tool.String_IS_Two(faxPrice));
				past1.setString(10, "" + String_Tool.String_IS_Two(faxPrice * Double.parseDouble(fQuantity)));
				past1.setString(11, "" + String_Tool.String_IS_Two(fNormalPrice));
				past1.setString(12, "" + String_Tool
						.String_IS_Two(Double.parseDouble(fNormalPrice) * (Double.parseDouble(fQuantity))));
				past1.setString(13, "" + obj1.optString("dProduct")); //
				System.out.println(obj1.optString("dProduct"));
				past1.setString(14, "" + obj1.optString("fTimes"));
				fMoney = Double.parseDouble(String_Tool.String_IS_Two(fInMoney)) + fMoney;
				past1.addBatch();
			}
			past1.executeBatch();
			DB.closePreparedStatement(past1);
			String sql = "INSERT INTO wh_StoreInWarehouse (dDate,cSheetNo,cSupplierNo,cSupplier,cOperatorNo,cOperator,cWhNo,cWh,fQty,cStoreNo,cStoreName,bReceive,dFillin,cFillinTime,cTime,fMoney,cFillEmpNo,cFillEmp)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			past = conn.prepareStatement(sql);
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, strrandom);
			past.setString(3, obj.getString("cSupplierNo"));
			past.setString(4, obj.getString("cSupplier"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.getString("cWhNo"));
			past.setString(8, obj.getString("cWh"));
			past.setString(9, "" + String_Tool.String_IS_Two(fQty));
			past.setString(10, obj.getString("cStoreNo"));
			past.setString(11, obj.getString("cStoreName"));
			past.setString(12, "0");
			past.setString(13, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(14, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(15, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(16, "" + String_Tool.String_IS_Two(fMoney)); // dFillin,cFillinTime,cTime
			past.setString(17, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(18, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			// past.setString(19, ""+fQty); // dFillin,cFillinTime,cTime
			past.execute();
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

	public static boolean uploadFenPurchaseOrder_Zhi_Pei(Connection conn, JSONArray array, String cSheetno) { //
		PreparedStatement past = null;
		String strrandom;
		double fMoney = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.get_men_dian_zhi_pei_no(conn, String_Tool.DataBaseYear(),obj.getString("cSupplierNo"));
			System.out.println(strrandom);
			PreparedStatement past1 = conn.prepareStatement("INSERT INTO wh_InWarehouseDetail (cSheetNo,iLineNo,cGoodsNo,cBarcode,cGoodsName,fQuantity,fInPrice,fInMoney,fTaxPrice,fTaxMoney,fNoTaxPrice,fNoTaxMoney,fPrice_SO,fTimes) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			double fQty = 0.0;
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement pastp = conn.prepareStatement(
						"select cPloyNo,cGoodsNo,fPrice_SO,dDateStart,dDateEnd from t_PloyOfSale where cStoreNo=? and dbo.getDayStr(getdate()) BETWEEN dDateStart and dDateEnd AND cGoodsNo=? ");
				pastp.setString(1, obj.getString("cStoreNo"));
				pastp.setString(2, obj1.getString("cGoodsNo"));
				ResultSet rsp = pastp.executeQuery();
				String fPrice_SO = "0.0";
				if (rsp.next()) {
					fPrice_SO = rsp.getString("fPrice_SO");
				}
				past1.setString(1, strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("cGoodsName"));
				past1.setString(6, obj1.getString("fQuantity"));
				past1.setString(7, obj1.getString("fInPrice"));
				past1.setString(8, obj1.getString("fInMoney"));
				String fNormalPrice = obj1.getString("fNormalPrice");
				if (String_Tool.isEmpty(fNormalPrice)) {
					fNormalPrice = "" + 0.0;
				}
				String fInPrice = obj1.getString("fInPrice");
				if (String_Tool.isEmpty(fInPrice)) {
					fInPrice = "" + 0.0;
				}
				String fQuantity = obj1.getString("fQuantity");
				if (String_Tool.isEmpty(fQuantity)) {
					fQuantity = "" + 0.0;
				}
				fQty = fQty + Double.parseDouble(fQuantity);
				double faxPrice = Double.parseDouble(fNormalPrice) - Double.parseDouble(fInPrice);
				past1.setString(9, "" + faxPrice);
				past1.setString(10, "" + faxPrice * Double.parseDouble(fQuantity));
				past1.setString(11, fNormalPrice);
				past1.setString(12, "" + Double.parseDouble(fNormalPrice) * (Double.parseDouble(fQuantity)));
				past1.setString(13, fPrice_SO);
				past1.setString(14, obj1.optString("fTimes"));
				fMoney = Double.parseDouble(obj1.getString("fInMoney")) + fMoney;
				past1.addBatch();
			}
			past1.executeBatch();
			DB.closePreparedStatement(past1);
			String sql = "INSERT INTO wh_InWarehouse (dDate,cSheetNo,cSupplierNo,cSupplier,cOperatorNo,cOperator,cWhNo,cWh,fQty,cStoreNo,cStoreName,dFillin,cFillinTime,cTime,fMoney,cFillEmpNo,cFillEmp)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			past = conn.prepareStatement(sql);
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, strrandom);
			past.setString(3, obj.getString("cSupplierNo"));
			past.setString(4, obj.getString("cSupplier"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.getString("cWhNo"));
			past.setString(8, obj.getString("cWh"));
			past.setString(9, "" + fQty);
			past.setString(10, obj.getString("cStoreNo"));
			past.setString(11, obj.getString("cStoreName"));
			past.setString(12, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(14, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(15, "" + fMoney); // dFillin,cFillinTime,cTime
			past.setString(16, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(17, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			past.execute();
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
	/**
	 * ****************************************************ZMY修改***************************************************
	 * @param conn
	 * @param array
	 * @param cSheetno
	 * @return
	 */
	public static boolean uploadQianxiFenPurchaseRu_Ku(Connection conn, JSONArray array, String cSheetno) { //
		PreparedStatement past = null;
		String strrandom;
		double fMoney = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			
			strrandom = GetcSheetno.get_commonality_cSheetNo(conn,
					"select SheetNo=" + new ReadConfig().getprop().getProperty("DataSource") + ".dbo.f_GenZsInsheetno('"
							+ String_Tool.DataBaseYear() + "','" + obj.getString("cSupplierNo") + "')");

			System.out.println(strrandom);
			PreparedStatement past1 = conn.prepareStatement("INSERT INTO wh_InWarehouseDetail (cSheetNo,iLineNo,cGoodsNo,cBarcode,cGoodsName,fQuantity,fInPrice,fInMoney,fTaxPrice,fTaxMoney,fNoTaxPrice,fNoTaxMoney,fPrice_SO,fTimes) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			double fQty = 0.0;
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement pastp = conn.prepareStatement(
						"select cPloyNo,cGoodsNo,fPrice_SO,dDateStart,dDateEnd from t_PloyOfSale where cStoreNo=? and dbo.getDayStr(getdate()) BETWEEN dDateStart and dDateEnd AND cGoodsNo=? ");
				pastp.setString(1, obj.getString("cStoreNo"));
				pastp.setString(2, obj1.getString("cGoodsNo"));
				ResultSet rsp = pastp.executeQuery();
				String fPrice_SO = "0.0";
				if (rsp.next()) {
					fPrice_SO = rsp.getString("fPrice_SO");
				}
				past1.setString(1, strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("cGoodsName"));
				past1.setString(6, obj1.getString("fQuantity"));
				past1.setString(7, obj1.getString("fInPrice"));
				past1.setString(8, obj1.getString("fInMoney"));
				String fNormalPrice = obj1.getString("fNormalPrice");
				if (String_Tool.isEmpty(fNormalPrice)) {
					fNormalPrice = "" + 0.0;
				}
				String fInPrice = obj1.getString("fInPrice");
				if (String_Tool.isEmpty(fInPrice)) {
					fInPrice = "" + 0.0;
				}
				String fQuantity = obj1.getString("fQuantity");
				if (String_Tool.isEmpty(fQuantity)) {
					fQuantity = "" + 0.0;
				}
				fQty = fQty + Double.parseDouble(fQuantity);
				double faxPrice = Double.parseDouble(fNormalPrice) - Double.parseDouble(fInPrice);
				past1.setString(9, "" + faxPrice);
				past1.setString(10, "" + faxPrice * Double.parseDouble(fQuantity));
				past1.setString(11, fNormalPrice);
				past1.setString(12, "" + Double.parseDouble(fNormalPrice) * (Double.parseDouble(fQuantity)));
				past1.setString(13, fPrice_SO);
				past1.setString(14, obj1.optString("fTimes"));
				fMoney = Double.parseDouble(obj1.getString("fInMoney")) + fMoney;
				past1.addBatch();
			}
			past1.executeBatch();
			DB.closePreparedStatement(past1);
			String sql = "INSERT INTO wh_InWarehouse (dDate,cSheetNo,cSupplierNo,cSupplier,cOperatorNo,cOperator,cWhNo,cWh,fQty,cStoreNo,cStoreName,dFillin,cFillinTime,cTime,fMoney,cFillEmpNo,cFillEmp)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			past = conn.prepareStatement(sql);
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, strrandom);
			past.setString(3, obj.getString("cSupplierNo"));
			past.setString(4, obj.getString("cSupplier"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.getString("cWhNo"));
			past.setString(8, obj.getString("cWh"));
			past.setString(9, "" + fQty);
			past.setString(10, obj.getString("cStoreNo"));
			past.setString(11, obj.getString("cStoreName"));
			past.setString(12, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(14, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(15, "" + fMoney); // dFillin,cFillinTime,cTime
			past.setString(16, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(17, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			past.execute();

			PreparedStatement past2 = conn.prepareStatement("update WH_Stock  set bReceive='1' where cSheetno = ? ");
			past2.setString(1, cSheetno);
			int a = past2.executeUpdate();
			DB.closePreparedStatement(past2);

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
	
	public static boolean uploadMen_Dian_Chang_Jia_Cai_Gou(Connection conn, JSONArray array,String cStoreNo,String cStoreName) { //
		PreparedStatement past = null;
		String strrandom;
		double fMoney = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.get_men_dian_chang_jia_cai_gou_no(conn, String_Tool.DataBaseYear(),obj.getString("cSupplierNo"));
			System.out.println(strrandom);
			PreparedStatement past1 = conn.prepareStatement("INSERT INTO  WH_StockDetail_lP (cSheetNo,iLineNo,cGoodsNo,cBarcode,cGoodsName,fQuantity,fInPrice,fInMoney,fTaxPrice,fTaxMoney,fNoTaxPrice,fNoTaxMoney,fPrice_SO,fTimes) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			double fQty = 0.0;
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement pastp = conn.prepareStatement(
						"select cPloyNo,cGoodsNo,fPrice_SO,dDateStart,dDateEnd from t_PloyOfSale where cStoreNo=? and dbo.getDayStr(getdate()) BETWEEN dDateStart and dDateEnd AND cGoodsNo=? ");
				pastp.setString(1, cStoreNo);
				pastp.setString(2, obj1.getString("cGoodsNo"));
				ResultSet rsp = pastp.executeQuery();
				String fPrice_SO = "0.0";
				if (rsp.next()) {
					fPrice_SO = rsp.getString("fPrice_SO");
				}
				past1.setString(1, strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("cGoodsName"));
				past1.setString(6, obj1.getString("fQuantity"));
				past1.setString(7, obj1.getString("fInPrice"));
				past1.setString(8, obj1.getString("fInMoney"));
				String fNormalPrice ="0.0";
				if (String_Tool.isEmpty(fNormalPrice)) {
					fNormalPrice = "" + 0.0;
				}
				String fInPrice = obj1.getString("fInPrice");
				if (String_Tool.isEmpty(fInPrice)) {
					fInPrice = "" + 0.0;
				}
				String fQuantity = obj1.getString("fQuantity");
				if (String_Tool.isEmpty(fQuantity)) {
					fQuantity = "" + 0.0;
				}
				fQty = fQty + Double.parseDouble(fQuantity);
				double faxPrice = Double.parseDouble(fNormalPrice) - Double.parseDouble(fInPrice);
				past1.setString(9, "" + faxPrice);
				past1.setString(10, "" + faxPrice * Double.parseDouble(fQuantity));
				past1.setString(11, fNormalPrice);
				past1.setString(12, "" + Double.parseDouble(fNormalPrice) * (Double.parseDouble(fQuantity)));
				past1.setString(13, fPrice_SO);
				past1.setString(14, obj1.optString("fTimes"));
				fMoney = Double.parseDouble(obj1.getString("fInMoney")) + fMoney;
				past1.addBatch();
			}
			past1.executeBatch();
			DB.closePreparedStatement(past1);
			String sql = "INSERT INTO WH_Stock_Lp (dDate,cSheetNo,cSupplierNo,cSupplier,cOperatorNo,cOperator,cWhNo,cWh,cStoreNo,cStoreName,bReceive,dFillin,cFillinTime,cTime,fMoney,cFillEmpNo,cFillEmp)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			past = conn.prepareStatement(sql);
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, strrandom);
			past.setString(3, obj.getString("cSupplierNo"));
			past.setString(4, obj.getString("cSupplier"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.getString("cWhNo"));
			past.setString(8, obj.getString("cWh"));
			past.setString(9, cStoreNo);
			past.setString(10, cStoreName);
			past.setString(11, "0");
			past.setString(12, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(14, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(15, "" + fMoney); // dFillin,cFillinTime,cTime
			past.setString(16, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(17, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			// past.setString(19, ""+fQty); // dFillin,cFillinTime,cTime
			past.execute();

//			PreparedStatement past2 = conn.prepareStatement("update WH_Stock_Lp  set bReceive='1' where cSheetno = ? ");
//			past2.setString(1, cSheetno);
//			int a = past2.executeUpdate();

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
	
	
	public static boolean Qianxi_Chang_Jia_Cai_Gou(Connection conn, JSONArray array,String cStoreNo,String cStoreName) { //
		PreparedStatement past = null;
		String strrandom;
		double fMoney = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.get_men_dian_chang_jia_cai_gou_no(conn, String_Tool.DataBaseYear(),obj.getString("cSupplierNo"));
			System.out.println(strrandom);
			PreparedStatement past1 = conn.prepareStatement("INSERT INTO  WH_StockDetail (cSheetNo,iLineNo,cGoodsNo,cBarcode,cGoodsName,fQuantity,fInPrice,fInMoney,fTaxPrice,fTaxMoney,fNoTaxPrice,fNoTaxMoney,fPrice_SO,fTimes) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			double fQty = 0.0;
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				PreparedStatement pastp = conn.prepareStatement(
						"select cPloyNo,cGoodsNo,fPrice_SO,dDateStart,dDateEnd from t_PloyOfSale where cStoreNo=? and dbo.getDayStr(getdate()) BETWEEN dDateStart and dDateEnd AND cGoodsNo=? ");
				pastp.setString(1, cStoreNo);
				pastp.setString(2, obj1.getString("cGoodsNo"));
				ResultSet rsp = pastp.executeQuery();
				String fPrice_SO = "0.0";
				if (rsp.next()) {
					fPrice_SO = rsp.getString("fPrice_SO");
				}
				past1.setString(1, strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("cGoodsName"));
				past1.setString(6, obj1.getString("fQuantity"));
				past1.setString(7, obj1.getString("fInPrice"));
				past1.setString(8, obj1.getString("fInMoney"));
				String fNormalPrice ="0.0";
				if (String_Tool.isEmpty(fNormalPrice)) {
					fNormalPrice = "" + 0.0;
				}
				String fInPrice = obj1.getString("fInPrice");
				if (String_Tool.isEmpty(fInPrice)) {
					fInPrice = "" + 0.0;
				}
				String fQuantity = obj1.getString("fQuantity");
				if (String_Tool.isEmpty(fQuantity)) {
					fQuantity = "" + 0.0;
				}
				fQty = fQty + Double.parseDouble(fQuantity);
				double faxPrice = Double.parseDouble(fNormalPrice) - Double.parseDouble(fInPrice);
				past1.setString(9, "" + faxPrice);
				past1.setString(10, "" + faxPrice * Double.parseDouble(fQuantity));
				past1.setString(11, fNormalPrice);
				past1.setString(12, "" + Double.parseDouble(fNormalPrice) * (Double.parseDouble(fQuantity)));
				past1.setString(13, fPrice_SO);
				past1.setString(14, obj1.optString("fTimes"));
				fMoney = Double.parseDouble(obj1.getString("fInMoney")) + fMoney;
				past1.addBatch();
			}
			past1.executeBatch();
			DB.closePreparedStatement(past1);
			String sql = "INSERT INTO WH_Stock (dDate,cSheetNo,cSupplierNo,cSupplier,cOperatorNo,cOperator,cWhNo,cWh,cStoreNo,cStoreName,bReceive,dFillin,cFillinTime,cTime,fMoney,cFillEmpNo,cFillEmp)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			past = conn.prepareStatement(sql);
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, strrandom);
			past.setString(3, obj.getString("cSupplierNo"));
			past.setString(4, obj.getString("cSupplier"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.getString("cWhNo"));
			past.setString(8, obj.getString("cWh"));
			past.setString(9, cStoreNo);
			past.setString(10, cStoreName);
			past.setString(11, "0");
			past.setString(12, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(14, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(15, "" + fMoney); // dFillin,cFillinTime,cTime
			past.setString(16, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(17, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			// past.setString(19, ""+fQty); // dFillin,cFillinTime,cTime
			past.execute();

//			PreparedStatement past2 = conn.prepareStatement("update WH_Stock_Lp  set bReceive='1' where cSheetno = ? ");
//			past2.setString(1, cSheetno);
//			int a = past2.executeUpdate();

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

	public static boolean insert_into_fen_dan_tui_huo(Connection conn, JSONArray array, String cBeizhu1) {
		// TODO Auto-generated method stub
		PreparedStatement past = null;
		String strrandom;
		double Sum_Money = 0;
		PreparedStatement past1 = null;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.getfen_dian_tui_huo_no(conn, String_Tool.DataBaseYear(),
					obj.getString("cClientNo"));
			System.out.println(strrandom);
			past1 = conn.prepareStatement(
					"INSERT INTO WH_cStoreReturnGoodsDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit) values (?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);

				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				double fQuantity=Double.parseDouble(obj1.getString("fQuantity"));
				double fInPrice= Double.parseDouble(obj1.getString("fInPrice"));
				double fInMoney=fQuantity*fInPrice;
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				past1.setString(7, "" + Double.parseDouble(obj1.getString("fInMoney")));
				Sum_Money = fInMoney + Sum_Money;
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.addBatch();
				if (i % 200 == 0) {
					past1.executeBatch();
				}
			}
			past1.executeBatch();
			DB.closePreparedStatement(past1);
			past = conn.prepareStatement(
					"INSERT INTO WH_cStoreReturnGoods (dDate,cSheetno,cClientNo,cClient,cOperatorNo,cOperator,cWhNo,cWh,cStoreNo,cStoreName,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,bExamin,fMoney,cBeizhu1,cSupNo,cSupName) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cClientNo"));
			past.setString(4, obj.getString("cClient"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, obj.getString("cStoreNo"));
			past.setString(10, obj.getString("cStoreName"));
			past.setString(11, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(12, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(14, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(15, obj.getString("cOperator"));
			past.setString(16, obj.optString("bExamin"));
			past.setString(17, "" + Sum_Money);
			past.setString(18,cBeizhu1);
			past.setString(19,obj.optString("cSupplierNo"));
			past.setString(20,obj.optString("cSupplier"));
			past.execute();
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
