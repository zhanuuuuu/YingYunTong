package DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import Tool.GetLog;
import Tool.GetcSheetno;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

public class DBupdate {

	public static String updatemessage(Connection conn, String userNo, String Longitude, String Latitude) {
		CallableStatement c = null;
		try {
			c = conn.prepareCall("{call P_O_CourierGPS (?,?,?,?)}");
			c.setString(1, userNo);
			c.setString(2, Longitude);
			c.setString(3, Latitude);
			c.registerOutParameter("return", java.sql.Types.INTEGER);
			c.execute();
			int fage = c.getInt("return");
			return "" + fage;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(c, conn);
		}
		return null;
	}

	public static JSONArray Select_ALL_Store(Connection conn) { // 查询店铺
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select cStoreNo,cStoreName,cServerName,cStoreIP,cDataBasePassword,cDataBase,cID_2D,fArea,cManager,cTel,image_path from t_Store where bOpen='1' ");
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, past, null, conn);
		
		}
		return null;
	}

	public static JSONArray Select_ALL_Pallet(Connection conn) { // 查询所有的托盘
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select cPalletNo,cStoreNo_belong,cID_2D from t_Pallet where iStatus ='0' "); // 查询可以使用的托盘
			// 不能使用
			// -1，
			// 可以使用0，正在使用1，随车返程中2

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

	public static JSONArray Select_Yi_Sheng_Fen_jian_Message(Connection conn) { // 查询已经审核分拣的信息
		ResultSet rs = null;
		CallableStatement c = null;
		try {
			c = conn.prepareCall("{call yi_shen_fen_jian_dan_goods}");
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

	public static JSONArray Select_Wei_Sheng_Fen_jian_Message(Connection conn) { // 查询没有审核分拣的信息
		ResultSet rs = null;
		CallableStatement c = null;
		try {
			c = conn.prepareCall("{call wei_shen_fen_jian_dan_goods}");
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

	public static JSONArray Select_ALL_Pallet(Connection conn, String str) { // 查询托盘
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select * from t_Pallet where cPalletNo=?");
			past.setString(1, str);
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

	public static JSONArray Select_ALL_Box(Connection conn) { // 查询所有的已经演的箱子
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select * from t_DistributionBox where iStatus ='0' "); // 查询可用的箱子
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

	public static JSONArray Select_ALL_Box(Connection conn, String name) { // 查询所有的已经演的箱子
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select * from t_DistributionBox where iStatus ='0'"); // 查询可用的箱子
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

	public static JSONArray Select_ALL_Yi_Yan_Tuo_Pan(Connection conn) { // 查询所有的已经演的托盘
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement(
					"select a.cSheetno,a.cCustomerNo,a.cCustomer,a.cStoreNo,a.cStoreName,a.cWhNo,a.cwh,b.cPalletNo,b.cDistBoxNo,b.cDistBoxNo_SortSheetNo  from wh_PalletSheet a,wh_PalletSheetDetail b where a.cSheetno=b.cSheetno and ISNULL(bExamin,0)=1");
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

	public static JSONArray Select_ALL_Store_Cangku(Connection conn) { // 查询所有的店铺
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select cStoreNo,cWhNo ,cWh from t_WareHouse");
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

	public static JSONArray Select_ALL_Car(Connection conn, String data) { // 查询所有的汽车信息
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement(
					"select a.TruckNo ,a.TruckType,a.TruckLicenseTag,b.DriverNo,b.Driver  from t_Truck a,t_Driver b where a.TruckLicenseTag=b.TruckLicenseTag and b.DriverNo=?");
			past.setString(1, data);
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

	public static JSONArray Select_ALL_Car(Connection conn) { // 查询所有的汽车信息
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select a.TruckNo ,a.TruckType,a.TruckLicenseTag,b.DriverNo,b.Driver from t_Truck a,t_Driver b where a.TruckLicenseTag=b.TruckLicenseTag ");
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

	public static JSONArray T_GroupType(Connection conn) { // 大类类别
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select cPath,cParentNo,cMarket,bFresh,bOnLine,cGroupTypeName,ImagePath,cGroupTypeNo,cIMG from T_GroupType where cParentno='--'");
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

	public static JSONArray Select_t_Warehouse(Connection conn, String cStoreNo) { // 根据店铺编号查名称
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select cWhNo ,cWh from t_Warehouse where cStoreNo=?");
			past.setString(1, cStoreNo);
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

	public static JSONArray T_GroupTypemall(Connection conn, String str) { // 查询类别下的类别
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			
			past = conn.prepareStatement("select cPath,cParentNo,cMarket,bFresh,bOnLine,cGroupTypeName,ImagePath,cGroupTypeNo,cIMG from T_GroupType where cParentno=?");
			past.setString(1, str);
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

	public static JSONArray Select_Out_Warehouse_No(Connection conn) { // 查询配送出库单可用的号
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = " select * from wh_cStoreOutWarehouse  where dDate>GETDATE()-30 and ISNULL(bExamin,0)=1 and  ISNULL(bReceive,0)=0 and  ISNULL(bOutYH,0)=0";
			past = conn.prepareStatement(sql);
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

	public static JSONArray Select_Out_Warehouse_goods(Connection conn, String no) { // 查询出已审核出库单的所有商品
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = " select * from wh_cStoreOutWarehouseDetail where wh_cStoreOutWarehouseDetail.cSheetno=?";
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

	public static JSONArray Select_Out_Warehouse_goods(Connection conn) { // 查询配送出库单的商品
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call Select_Out_Warehouse_goods }");
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

	public static JSONArray Select_Larage_group_Good(Connection conn, String cStoreNo, String dDate1, String dDate2,
			String cWhNo, String GroupTypeNo) { // 大类的商品
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			System.out.println(GroupTypeNo);
			c = conn.prepareCall("{call p_AppGetFreshSale (?,?,?,?,?)}");
			c.setString(1, cStoreNo);
			c.setString(2, dDate1);
			c.setString(3, dDate2);
			c.setString(4, cWhNo);
			c.setString(5, GroupTypeNo);
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, null, c, conn);
		}
		return null;
	}

	public static JSONArray Select_Larage_group_Good_Zi_dong(Connection conn, String cStoreNo, String dDate1,
			String dDate2, String cWhNo, String GroupTypeNo) { // 大类的商品
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			System.out.println(GroupTypeNo);
			c = conn.prepareCall("{call p_AppGetFreshSale_OUTO (?,?,?,?,?)}");
			c.setString(1, cStoreNo);
			c.setString(2, dDate1);
			c.setString(3, dDate2);
			c.setString(4, cWhNo);
			c.setString(5, GroupTypeNo);
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, null, c, conn);
		}
		return null;
	}

	public static String Select_Fresh_Zi_dong_Xi_Shu(Connection conn, String cStoreNo, String cSupNo, String dDate0,
			String iDays) { // 大类的商品
		CallableStatement c = null;
		ResultSet rs = null;
		String fWeekAVG = "";
		try {

			c = conn.prepareCall("{call p_GetSupDayOrderRate_Wei_iDays (?,?,?,?)}");
			c.setString(1, cStoreNo);
			c.setString(2, cSupNo);
			c.setString(3, dDate0);
			c.setString(4, iDays);
			rs = c.executeQuery();
			if (rs.next()) {
				fWeekAVG = rs.getString("fWeekAVG");
			}
			return fWeekAVG;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, null, c, conn);
		}
		return null;
	}

	public static JSONArray Select_Fresh_Zi_dong(Connection conn, String dDate1, JSONArray Store_array,
			JSONArray Sup_array) { // 大类的商品
		CallableStatement c = null;
		ResultSet rs = null;
		Statement stat = null;
		try {
			String sql = "if (select object_id('tempdb..#tmp_Store')) is not null drop table #tmp_Store create table #tmp_Store ( cStoreNo varchar(32),cStoreName varchar(64) )";
			stat = conn.createStatement();
			stat.addBatch(sql);
			stat.addBatch("if (select object_id('tempdb..#tmp_StoreSup')) is not null drop table #tmp_StoreSup create table #tmp_StoreSup ( cSupNo varchar(32) )");

			for (int i = 0; i < Store_array.length(); i++) {
				JSONObject obj = Store_array.getJSONObject(i);
				sql = "INSERT INTO #tmp_Store (cStoreNo,cStoreName) values('" + obj.getString("cStoreNo") + "'" + ","
						+ "'" + obj.getString("cStoreName") + "')";
				System.out.println(sql);
				stat.addBatch(sql);
			}
			for (int i = 0; i < Sup_array.length(); i++) {
				JSONObject obj = Sup_array.getJSONObject(i);
				sql = "INSERT INTO #tmp_StoreSup (cSupNo) values('" + obj.getString("cSupNo") + "')";
				stat.addBatch(sql);
			}
			stat.executeBatch();
			c = conn.prepareCall("{call p_GetStoreStock_Wei_APP (?,?)}");
			c.setString(1, dDate1);
			c.setString(2, dDate1);
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, null, c, conn);
		}
		return null;
	}

	public static JSONArray Select_Larage_group_Good(Connection conn, String cStoreNo, String cWhNo,
			String GroupTypeNo) { // 大类的商品
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			System.out.println(GroupTypeNo);
			c = conn.prepareCall("{call p_AppGetFreshSale_Goods (?,?,?)}");
			c.setString(1, cStoreNo);
			c.setString(2, cWhNo);
			System.out.println(GroupTypeNo);
			c.setString(3, GroupTypeNo);
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, null, c, conn);
		}
		return null;
	}

	public static boolean insert_into_group_Good(Connection conn, JSONArray array) { // 提交补货
		PreparedStatement past = null;
		String strrandom = "";
		double zmoney = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.getcSheetno(conn, String_Tool.DataBaseYear(), obj.getString("cStoreNo")); // 补货单
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past = conn.prepareStatement(
						"INSERT INTO WH_BhApplyDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo) values (?,?,?,?,?,?,?,?,?)");
				past.setString(1, "" + strrandom);
				past.setString(2, "" + (i + 1));
				past.setString(3, obj1.getString("cGoodsName"));
				past.setString(4, obj1.getString("cBarcode"));
				String fQuantity = obj1.getString("fQuantity");
				String fInPrice = obj1.getString("fInPrice");
				double fInMoney = (Double.parseDouble(fQuantity) * Double.parseDouble(fInPrice));
				zmoney = zmoney + fInMoney;
				past.setString(5, obj1.getString("fQuantity"));
				past.setString(6, obj1.getString("fInPrice"));
				past.setString(7, String_Tool.String_IS_Four(fInMoney));
				past.setString(8, String_Tool.DataBaseYear_Month_Day());
				past.setString(9, obj1.getString("cGoodsNo"));
				past.execute();
			}
			past = conn.prepareStatement(
					"INSERT INTO WH_BhApply (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,fMoney,cWhNo,cWh,cTime,cFillEmpNo,cFillEmp,bfresh,bExamin,MbExamin,bPeisong) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, "" + String_Tool.String_IS_Four(zmoney));
			past.setString(8, obj.getString("cWhNo"));
			past.setString(9, obj.getString("cWh"));
			past.setString(10, String_Tool.DataBaseH_M_S());
			past.setString(11, obj.getString("cOperatorNo"));
			past.setString(12, obj.getString("cOperator"));
			past.setString(13, obj.getString("bFresh"));
			past.setString(14, "1");
			past.setString(15, "0");
			past.setString(16, "0");
			past.execute();

			
			PreparedStatement past1 = conn.prepareStatement(
					"INSERT INTO t_Messages (title,content,itimes,acceptusername,acceptrealname,cSheetNo) values(?,?,?,?,?,?)");
			past1.setString(1, "门店补货单审核");
			past1.setString(2, obj.getString("cStoreNo") + "-" + obj.getString("cStoreName"));
			past1.setString(3, String_Tool.DataBaseTime());
			past1.setString(4, obj.getString("cOperatorNo"));
			past1.setString(5, obj.getString("cOperator"));
			past1.setString(6, strrandom);
			past1.execute();
			DB.closePreparedStatement(past1);
			conn.commit();
			conn.setAutoCommit(true);
			return true;
			// }
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

	public static boolean insert_into_Smart_Good(Connection conn, JSONArray array) { // 提交补货
		PreparedStatement past = null;
		String strrandom = "";
		double zmoney = 0;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			strrandom = GetcSheetno.getcSheetno(conn, String_Tool.DataBaseYear(), obj.getString("cStoreNo")); // 补货单
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past = conn.prepareStatement(
						"INSERT INTO WH_BhApplyDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,fprice_so) values (?,?,?,?,?,?,?,?,?,?)");
				past.setString(1, "" + strrandom);
				past.setString(2, "" + (i + 1));
				past.setString(3, obj1.getString("cGoodsName"));
				past.setString(4, obj1.getString("cBarcode"));
				String bu_huo_num = obj1.getString("bu_huo_num");

				String fInPrice = obj1.getString("fInPrice");
				String fInMoney = "" + (Double.parseDouble(bu_huo_num) * Double.parseDouble(fInPrice));
				zmoney = zmoney + Double.parseDouble(fInMoney);

				past.setString(5, obj1.getString("bu_huo_num"));
				past.setString(6, obj1.getString("fInPrice"));
				past.setString(7, fInMoney);
				past.setString(8, String_Tool.DataBaseTime());
				past.setString(9, obj1.getString("cGoodsNo"));
				past.setString(10, obj1.getString("fQuantity"));
				past.execute();
			}
			past = conn.prepareStatement(
					"INSERT INTO WH_BhApply (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,fMoney,cWhNo,cWh,cTime,cFillEmpNo,cFillEmp,bfresh,bExamin,MbExamin,bPeisong) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, "" + zmoney);
			past.setString(8, obj.getString("cWhNo"));
			past.setString(9, obj.getString("cWh"));
			past.setString(10, String_Tool.DataBaseH_M_S());
			past.setString(11, obj.getString("cOperatorNo"));
			past.setString(12, obj.getString("cOperator"));
			past.setString(13, obj.getString("bFresh"));
			past.setString(14, "1");
			past.setString(15, "1");
			past.setString(16, "1");
			past.execute();

			PreparedStatement past1 = conn.prepareStatement(
					"INSERT INTO t_Messages (title,content,itimes,acceptusername,acceptrealname,cSheetNo) values(?,?,?,?,?,?)");
			past1.setString(1, "门店补货单审核");
			past1.setString(2, obj.getString("cStoreNo") + "-" + obj.getString("cStoreName"));
			past1.setString(3, String_Tool.DataBaseTime());
			past1.setString(4, obj.getString("cOperatorNo"));
			past1.setString(5, obj.getString("cOperator"));
			past1.setString(6, strrandom);
			past1.execute();
			DB.closePreparedStatement(past1);

			CallableStatement c = null;
			c = conn.prepareCall("{call p_cStoreBhApplyTocStoreOut (?,?)}");
			System.out.println(strrandom);
			c.setString(1, strrandom);
			c.registerOutParameter("return", java.sql.Types.LONGNVARCHAR);
			c.execute();
			String fage = c.getString("return");
			System.out.println(fage);
			DB.closeCallState(c);
			if (fage.equals("0")) {
				conn.rollback();
				return false;
			} else {
				conn.commit();
				conn.setAutoCommit(true);
				return true;
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block

				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return false;
	}

	public static JSONArray Select_fen_dian_not_update_Good(Connection conn, // 查询不能修改补货
			String date1, String date2, String cStoreNo) { //
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			// '2016-10-11','2016-11-25','1011'
			c = conn.prepareCall("{call bu_huo_dan_goods  (?,?,?)}");
			c.setString(1, date1);
			c.setString(2, date2);
			c.setString(3, cStoreNo);
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			System.out.println(array.toString());
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, null, c, conn);
		}
		return null;
	}

	public static JSONArray Select_fen_dian_buhuo_Good(Connection conn, // 查询能修改补货
			String date1, String date2, String cStoreNo) { //
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			// '2016-10-11','2016-11-25','1011'
			c = conn.prepareCall("{call wei_shen_bu_huo_dan  (?,?,?)}");
			c.setString(1, date1);
			c.setString(2, date2);
			c.setString(3, cStoreNo);
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, null, c, conn);
		}
		return null;
	}

	public static boolean Update_fen_dian_buhuo_Good(Connection conn, // 修改补货
			String cSheetno, String fMoney, JSONArray array) {

		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			PreparedStatement past = conn.prepareStatement("update WH_BhApply set  fMoney=? where cSheetno =?");
			past.setString(1, fMoney);
			past.setString(2, cSheetno);
			past.execute();
			DB.closePreparedStatement(past);

			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				PreparedStatement past1 = conn.prepareStatement("update WH_BhApplyDetail set fInMoney= ?, fQuantity=?  where cGoodsNo=? and cSheetno=? ");
				past1.setString(1, obj.getString("fInMoney"));
				past1.setString(2, obj.getString("fQuantity"));
				past1.setString(3, obj.getString("cGoodsNo"));
				past1.setString(4, obj.getString("cSheetno"));
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			DB.closeConn(conn);
		}
		return false;
	}

	public static int insert_into_Fen_jian(Connection conn, JSONArray array, JSONArray array1) { // 提交分拣
		String strrandom;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			PreparedStatement pastselect=conn.prepareStatement("select * from wh_cStoreOutWarehouse_Sort where dDate=? and cDistBoxNo=? and cCustomerNo=?");
			pastselect.setString(1, String_Tool.DataBaseYear_Month_Day());
			pastselect.setString(2, obj.optString("cDistBoxNo"));
			pastselect.setString(3, obj.getString("cCustomerNo"));
			ResultSet rs=pastselect.executeQuery(); //此箱子今天已经提交
			if(rs.next()){
				System.out.println("已经装了");
				return 2;
			}
			PreparedStatement past = conn.prepareStatement("INSERT INTO wh_cStoreOutWarehouse_Sort (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,cWhNo,cWh,cDistBoxNo,cCustomerNo,cCustomer,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,jiesuanno,bExamin,cOutSerNo) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			strrandom = GetcSheetno.getcSheetno_Fen_jian(conn, String_Tool.DataBaseYear(), obj.getString("cStoreNo"));
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, obj.optString("cDistBoxNo")); // 箱号可能为空
			past.setString(10, obj.getString("cCustomerNo"));
			past.setString(11, obj.getString("cCustomer"));
			past.setString(12, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(13, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(14, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(15, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(16, obj.getString("cOperator")); // dFillin,cFillinTime,cTime
			past.setString(17, obj.getString("cSheetno")); //
			past.setString(18, obj.getString("bExamin")); //
			past.setString(19, obj.getString("cOutSerNo")); //
			past.execute();
			DB.closePreparedStatement(past);

			past = conn.prepareStatement(
					"INSERT INTO wh_cStoreOutWarehouseDetail_Sort (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit,jiesuanno,cProductSerno,cUnitedNo,cOutiLineNo,fPrice_SO) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement past1 = conn.prepareStatement(
					"Update wh_cStoreOutWarehouseDetail  set bChecked ='1' where cSheetno = ? and cGoodsNo=? and iLineNo= ?");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past.setString(1, "" + strrandom);
				past.setString(2, "" + (i + 1));
				past.setString(3, obj1.getString("cGoodsName"));
				past.setString(4, obj1.getString("cBarcode"));
				String fQuantity = obj1.getString("num");
				String fInPrice = obj1.getString("fInPrice");
				String fInMoney = "" + (Double.parseDouble(fQuantity) * Double.parseDouble(fInPrice));
				past.setString(5, fQuantity);
				past.setString(6, obj1.getString("fInPrice"));
				past.setString(7, fInMoney);
				past.setString(8, String_Tool.DataBaseTime());
				past.setString(9, obj1.getString("cGoodsNo"));
				past.setString(10, obj1.optString("cUnit"));//
				past.setString(11, obj1.getString("cSheetno"));
				past.setString(12, obj1.optString("cProductSerno"));
				past.setString(13, obj1.optString("cUnitedNo"));
				past.setString(14, obj1.getString("iLineNo"));
				past.setString(15, obj1.getString("fQuantity"));
				past.addBatch();
				// 把这个出库单的商品设置为已经出库
				past1.setString(1, obj1.getString("cSheetno"));
				past1.setString(2, obj1.getString("cGoodsNo"));
				past1.setString(3, obj1.getString("iLineNo"));
				past1.addBatch();
			}
			past.executeBatch();
			past1.executeBatch();
			DB.closePreparedStatement(past);
			DB.closePreparedStatement(past1);
			// 箱子设置一下
			PreparedStatement past2 = conn.prepareStatement(
					" Update t_DistributionBox set iStatus=1,cStoreNo_Last =?, cWhNo_Last =?, cSheetno_Last=?,cStore_No =? , cStore_Name=? , user_no =? , [user_name]=?, recovery_time=?   where cDistBoxNo=? ");
			past2.setString(1, obj.getString("cCustomerNo"));
			past2.setString(2, obj.optString("cWhNo"));
			past2.setString(3, strrandom);
			past2.setString(4, obj.getString("cStoreNo"));
			past2.setString(5, obj.getString("cStoreName"));
			past2.setString(6, obj.getString("cOperatorNo"));
			past2.setString(7, obj.getString("cOperator"));
			past2.setString(8, String_Tool.DataBaseTime());
			past2.setString(9, obj.optString("cDistBoxNo"));
			past2.executeUpdate();
			DB.closePreparedStatement(past2);

			PreparedStatement past3 = conn.prepareStatement("INSERT INTO wh_cStoreOutWarehouse_Sort_Out (dDate,cSheetNo,cSheetNo_Out) values (?,?,?)");
			PreparedStatement past3_1 = conn.prepareStatement("Update wh_cStoreOutWarehouse set bOutYH ='1' where cSheetno = ?");
			for (int i = 0; i < array1.length(); i++) {
				JSONObject obj2 = array1.getJSONObject(i);
				past3.setString(1, String_Tool.DataBaseYear_Month_Day());
				past3.setString(2, strrandom);
				past3.setString(3, obj2.getString("cSheetNo_Out"));
				past3.addBatch();
				// 把这个出库单的商品设置为已经出库
				past3_1.setString(1, obj2.getString("cSheetNo_Out"));
				past3_1.addBatch();
			}
			past3.executeBatch();
			past3_1.executeBatch();
			DB.closePreparedStatement(past3);
			DB.closePreparedStatement(past3_1);
			conn.commit();
			conn.setAutoCommit(true);
			return 1;
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
		return 0;
	}

	public static boolean insert_into_Tuo_Pan(Connection conn, JSONArray array) { // 提交托盘
		PreparedStatement past = null;
		String strrandom;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			past = conn.prepareStatement("INSERT INTO wh_PalletSheet (dDate,cSheetno,cStoreNo,cStoreName,cOperatorNo,cOperator,cCustomerNo,cCustomer,dFillin,cFillinTime,cWhNo,cWh,cTime,cFillEmpNo,cFillEmp,bExamin) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			strrandom = GetcSheetno.getGenPalletSheet_no(conn, String_Tool.DataBaseYear(), String_Tool.DataBaseM());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.optString("cOperatorNo"));
			past.setString(6, obj.optString("cOperator"));
			past.setString(7, obj.getString("cCustomerNo"));
			past.setString(8, obj.getString("cCustomer"));
			past.setString(9, String_Tool.DataBaseYear_Month_Day()); // dFillin
			past.setString(10, String_Tool.DataBaseH_M_S()); // cFillinTime
			past.setString(11, "");
			past.setString(12, "");
			past.setString(13, String_Tool.DataBaseH_M_S()); // cTime
			past.setString(14, obj.getString("cOperatorNo"));
			past.setString(15, obj.getString("cOperator"));
			past.setString(16, obj.getString("bExamin"));
			past.execute();
			PreparedStatement  past1 = conn.prepareStatement("INSERT INTO wh_PalletSheetDetail (dDate,cSheetno,cPalletNo,cDistBoxNo,cDistBoxNo_SortSheetNo) values (?,?,?,?,?)");
			PreparedStatement  past2 = conn.prepareStatement("Update wh_cStoreOutWarehouse_Sort  set bReceive='1' where cSheetno=?");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past1.setString(1, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
				past1.setString(2, "" + strrandom);
				past1.setString(3, obj1.getString("cPalletNo"));
				past1.setString(4, obj1.getString("cDistBoxNo"));
				past1.setString(5, obj1.getString("cDistBoxNo_SortSheetNo"));
				past1.addBatch();
				if(i%200==0){
					past1.executeBatch();
				}
				past2.setString(1, obj1.getString("cDistBoxNo_SortSheetNo"));
				past2.addBatch();
				if(i%200==0){
					past2.executeBatch();
				}
			}
			past1.executeBatch();
			past2.executeBatch();
			DB.closePreparedStatement(past1);
			DB.closePreparedStatement(past2);
			
			PreparedStatement past3 = conn.prepareStatement("update t_Pallet  set iStatus =1  where cPalletNo= ? ");
			past3.setString(1, obj.optString("cPalletNo"));
			past3.executeUpdate();
			DB.closePreparedStatement(past3);
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

	public static String insert_into_Tuo_Zhuang_Che(Connection conn, JSONArray array, JSONArray array1,
			JSONArray array2) { // 提交装车
		String strrandom;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			strrandom = GetcSheetno.get_zhuang_che_Sheetno(conn, String_Tool.DataBaseYear(), String_Tool.DataBaseM());
			System.out.println(strrandom);
			
			PreparedStatement past = conn.prepareStatement("INSERT INTO wh_ShipSheetDetail (cSheetno,iLineNo,cSheetno_YH,cGoodsNo,cGoodsName,cBarcode,cUnitedNo,fQuantity,fInPrice,fInMoney,cUnit,fTimes) values (?,?,?,?,?,?,?,?,?,?,?,?)");

			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				
				PreparedStatement past0=conn.prepareStatement("select bReceive from wh_cStoreOutVerifySheet where cSheetno = ? and bReceive='1' ");
				past0.setString(1,obj1.getString("cSheetno"));
				ResultSet rs=past0.executeQuery();
				if(rs.next()){
					DB.closeRs_Con(rs, past0, null);
					continue;
				}
				past.setString(1, "" + strrandom);
				past.setString(2, "" + (i + 1));
				past.setString(3, obj1.getString("cSheetno"));
				past.setString(4, obj1.getString("cGoodsNo"));
				past.setString(5, obj1.getString("cGoodsName"));
				past.setString(6, obj1.getString("cBarcode"));
				past.setString(7, obj1.getString("cUnitedNo"));
				past.setString(8, obj1.getString("fQuantity"));
				String fInPrice = obj1.getString("fInPrice");
				String fInMoney = obj1.getString("fInMoney");
				if (fInPrice == null || fInPrice.equals("") || fInPrice.equals("null")) {
					fInPrice = "0.0";
				}
				if (fInMoney == null || fInMoney.equals("") || fInMoney.equals("null")) {
					fInMoney = "0.0";
				}
				past.setString(9, fInPrice);
				past.setString(10, fInMoney);
				past.setString(11, obj1.optString("cUnit"));
				past.setString(12, fInMoney);
				past.addBatch();
			}
			past.executeBatch();
			DB.closePreparedStatement(past);

			JSONObject obj = array.getJSONObject(0);
			PreparedStatement past1 = conn.prepareStatement("INSERT INTO wh_ShipSheet (dDate,cSheetno,cStoreNo,cStoreName,cCustomerNo,cCustomer,cOperatorNo,cOperator,cFillEmpNo,cFillEmp,dFillin,cFillinTime,cWhNo,cWh,cDriverNo,cDriverName,cTruckID,cTime,bExamin) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past1.setString(1, String_Tool.DataBaseYear_Month_Day());
			past1.setString(2, "" + strrandom);
			past1.setString(3, obj.getString("cStoreNo"));
			past1.setString(4, obj.getString("cStoreName"));
			past1.setString(5, obj.getString("cStoreNo"));
			past1.setString(6, obj.getString("cStoreName"));
			past1.setString(7, obj.getString("cOperatorNo"));
			past1.setString(8, obj.getString("cOperator"));
			past1.setString(9, obj.getString("cOperatorNo"));
			past1.setString(10, obj.getString("cOperator"));
			past1.setString(11, String_Tool.DataBaseYear_Month_Day()); // dFillin
			past1.setString(12, String_Tool.DataBaseH_M_S()); // cFillinTime
			past1.setString(13, obj.getString("cWhNo"));
			past1.setString(14, obj.getString("cwh"));
			past1.setString(15, obj.getString("cDriverNo"));
			past1.setString(16, obj.getString("cDriverName"));
			past1.setString(17, obj.getString("cTruckID"));
			past1.setString(18, String_Tool.DataBaseH_M_S());
			past1.setString(19, "1");
			past1.execute();

			PreparedStatement past2 = conn.prepareStatement("INSERT INTO wh_ShipOutYhSheetDetail (dDate,cSheetno,cStoreNo,cStoreName,cSheetNo_YH,dDate_YH,cSheetNo_Pallet) values (?,?,?,?,?,?,?)");
			
			PreparedStatement past3 = conn.prepareStatement("Update wh_cStoreOutVerifySheet set bReceive ='1' where cSheetno = ?");// 把配送验货出库单设置为
																												// //																				// 1
			for (int i = 0; i < array1.length(); i++) {
				JSONObject obj1 = array1.getJSONObject(i);

				past3.setString(1, obj1.getString("cSheetno"));
				past3.addBatch();
				
				PreparedStatement past0=conn.prepareStatement("select bReceive from wh_cStoreOutVerifySheet where cSheetno = ? and bReceive='1' ");
				past0.setString(1,obj1.getString("cSheetno"));
				ResultSet rs=past0.executeQuery();
				if(rs.next()){
					DB.closeRs_Con(rs, past0, null);
					continue;
				}
				past2.setString(1, String_Tool.DataBaseYear_Month_Day());
				past2.setString(2, "" + strrandom);
				past2.setString(3, obj1.getString("cCustomerNo"));
				past2.setString(4, obj1.getString("cCustomer"));
				past2.setString(5, obj1.getString("cSheetno"));
				past2.setString(6, String_Tool.DataBaseYear_Month_Day());
				past2.setString(7, obj1.getString("cSheetNo_Pallet"));
				System.out.println("" + i + obj1.getString("cSheetNo_Pallet"));
				past2.addBatch();

			}
			past2.executeBatch();
			past3.executeBatch();

			PreparedStatement past4 = conn.prepareStatement("UPDATE  t_DistributionBox  set  cStore_No =? , cStore_Name=? , user_no =? , [user_name]=?, recovery_time=?  where cDistBoxNo =? ");
			for (int i = 0; i < array2.length(); i++) {
				JSONObject obj2 = array2.getJSONObject(i);
				String cDistBoxNo_name = obj2.getString("cDistBoxNo_name");
				String cPalletNo_name = obj2.optString("cPalletNo_name");
				String user_no = obj2.optString("user_no");
				String user_name = obj2.optString("user_name");
				String user_type = obj2.optString("user_type");

				past4.setString(1, obj.getString("cStoreNo"));
				past4.setString(2, obj.getString("cStoreName"));
				past4.setString(3, user_no);
				past4.setString(4, user_name);
				past4.setString(5, String_Tool.DataBaseTime());
				past4.setString(6, cDistBoxNo_name);
				past4.addBatch();
			}
			past4.executeBatch();
			DB.closePreparedStatement(past1);
			DB.closePreparedStatement(past2);
			DB.closePreparedStatement(past3);
			DB.closePreparedStatement(past4);
			conn.commit();
			conn.setAutoCommit(true);
			return strrandom;

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return null;
		} finally {
			DB.closeConn(conn);
		}
	}

	public static boolean Update_Fen_jian_dan(Connection conn, String SortSheetNo, JSONArray array) { // 提交装车
		PreparedStatement past = null;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				String sql = "UPDATE wh_cStoreOutWarehouseDetail_Sort set fQuantity=? , fInMoney=?  where cSheetno = ?  and cGoodsNo = ?";
				past = conn.prepareStatement(sql);

				String fInPrice = obj.getString("fInPrice");
				String Now_Num = obj.getString("Now_Num");
				double fInMoney = Double.parseDouble(fInPrice) * Double.parseDouble(Now_Num);
				past.setString(1, Now_Num);
				past.setString(2, "" + fInMoney);
				past.setString(3, obj.getString("cDistBoxNo_SortSheetNo"));
				past.setString(4, obj.getString("cGoodsNo"));
				past.execute();
				DB.closePreparedStatement(past);
			}
			String sql = "UPDATE  wh_cStoreOutWarehouse_Sort  set bExamin=' 1'  where cSheetno = ? ";
			past = conn.prepareStatement(sql);
			past.setString(1, SortSheetNo);
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
			return false;
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
	}
}
