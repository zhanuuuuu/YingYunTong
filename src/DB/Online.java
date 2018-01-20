package DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;
import Tool.GetcSheetno;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

public class Online {

	public static JSONArray Select_Online_Bu_huo_goods(Connection conn) {
		ResultSet rs = null;
		try {
			String sql = "select * from dbo.WH_BhApply_Online a,dbo.WH_BhApplyDetail_Online b where a.cSheetno=b.cSheetno and a.bReceive='0' ";
			PreparedStatement past = conn.prepareStatement(sql);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean insert_into_online_Fen_jian(Connection conn,
			JSONArray array, JSONArray array1) { // 提交分拣
		String strrandom;
		Connection conn1 = GetConnection.getPS_OSSConn(); // 线上数据库链接
		try {
			conn.setAutoCommit(false); // 更改JDBC事务的默认提交方式
			conn1.setAutoCommit(false);
			JSONObject obj = array.getJSONObject(0);
			PreparedStatement past = conn.prepareStatement("INSERT INTO wh_cStoreOutWarehouse_Sort__Online (dDate,cSheetno,UserNo,UserName,cOperatorNo,cOperator,time,cManagerNo,cManager,bExamin,cStoreNo,cStoreName,boxNo,Head_Store_No,Head_Store_Name,receive) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			strrandom = "F_" + UUID.randomUUID().toString();
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("UserNo"));
			past.setString(4, obj.getString("UserName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, String_Tool.DataBaseTime());
			// cManagerNo,cManager,
			past.setString(8, obj.getString("cOperatorNo"));
			past.setString(9, obj.getString("cOperator"));
			past.setString(10, "1");
			past.setString(11, obj.getString("cStoreNo"));
			past.setString(12, obj.getString("cStoreName"));
			past.setString(13, obj.optString("box")); // 箱号可能为空
			past.setString(14, obj.getString("Head_Store_No"));
			past.setString(15, obj.getString("Head_Store_Name"));
			past.setString(16, "0"); //
			past.execute();
			DB.closePreparedStatement(past);

			PreparedStatement past1 = conn
					.prepareStatement("INSERT INTO wh_cStoreOutWarehouseDetail_Sort_Online (cSheetno,iLineNo,cGoodsNo,cGoodsName,cBarcode,fQuantity,num,Cha_yi_num,bChecked,time,cUnit,cSpec) values (?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("cGoodsName"));
				past1.setString(5, obj1.getString("cBarcode"));

				past1.setString(6, obj1.getString("fQuantity"));
				past1.setString(7, obj1.getString("num"));
				past1.setString(8, obj1.getString("Cha_yi_num"));

				past1.setString(9, "1");
				past1.setString(10, String_Tool.DataBaseTime());
				past1.setString(11, obj1.getString("cUnit"));
				past1.setString(12, obj1.getString("cSpec"));
				past1.addBatch();
			}
			past1.executeBatch();
			DB.closePreparedStatement(past1);
			// 箱子设置一下
			PreparedStatement past2 = conn
					.prepareStatement(" Update t_DistributionBox set iStatus=1,cSheetno_Last=?,cStore_No =? , cStore_Name=? , user_no =? , [user_name]=?, recovery_time=?   where cDistBoxNo=? ");
			past2.setString(1, strrandom);
			past2.setString(2, obj.getString("Head_Store_No"));
			past2.setString(3, obj.getString("Head_Store_Name"));
			past2.setString(4, obj.getString("cOperatorNo"));
			past2.setString(5, obj.getString("cOperator"));
			past2.setString(6, String_Tool.DataBaseTime());
			past2.setString(7, obj.optString("box"));
			past2.executeUpdate();
			DB.closePreparedStatement(past2);

			PreparedStatement past3 = conn1
					.prepareStatement("Update WH_BhApply_Online set bReceive ='1' where cSheetno = ?");
			System.out.println(strrandom);
			PreparedStatement past4 = conn.prepareStatement("INSERT INTO  Customer_Order_and_Sorting_No (Sorting_cSheetno,Customer_OrderNo)values(?,?)");
			for (int i = 0; i < array1.length(); i++) { // 把这个出库单的商品设置为已经出库
				JSONObject obj2 = array1.getJSONObject(i);
				past3.setString(1, obj.getString("cSheetno"));

				past4.setString(1, strrandom);
				past4.setString(2, obj2.getString("cSheetNo_Out"));

				past3.addBatch();
				past4.addBatch();
			}
			past3.executeBatch();
			past4.executeBatch();

			DB.closePreparedStatement(past3);
			DB.closePreparedStatement(past4);
			conn.commit();
			conn.setAutoCommit(true);
			conn1.commit();
			conn1.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			try {
				conn.rollback();
				conn1.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DB.closeConn(conn);
			DB.closeConn(conn1);
		}
		return false;
	}

	// CallableStatement c = null;
	// try {
	// c = conn.prepareCall("{call yi_shen_fen_jian_dan_goods}");
	// rs = c.executeQuery();
	public static JSONArray Select_Softing_goods(Connection conn) {
		ResultSet rs = null;
		CallableStatement c = null;
		try {
			c = conn.prepareCall("{call select_online_soft_goods}");
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean Insert_Into_Tuo_Pan(Connection conn, JSONArray array) {

		try {
			conn.setAutoCommit(false);
			String cSheetno = "T_" + UUID.randomUUID().toString().hashCode();
			JSONObject obj1 = array.getJSONObject(0);
			PreparedStatement past = conn.prepareStatement("INSERT INTO online_pallet (dDate,cSheetNo,cStoreNo,cStoreName,cOperatorNo,cOperator,cPalletNo,time) values(?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, cSheetno);
			past.setString(3, obj1.getString("cStoreNo"));
			past.setString(4, obj1.getString("cStoreName"));
			past.setString(5, obj1.getString("cOperatorNo"));
			past.setString(6, obj1.getString("cOperator"));
			past.setString(7, obj1.getString("cPalletNo"));
			past.setString(8, String_Tool.DataBaseH_M_S());
			past.execute();
			DB.closePreparedStatement(past);
            //插入托盘单数据
			PreparedStatement past1 = conn.prepareStatement("INSERT INTO online_Pallet_Detail (cSheetNo,boxNo,F_cSheetno,UserNo,UserName) values(?,?,?,?,?)");
			//修改分简单的状态
			PreparedStatement past2 = conn.prepareStatement("UPDATE wh_cStoreOutWarehouse_Sort__Online set receive='1' where cSheetno =? ");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				past1.setString(1, cSheetno);
				past1.setString(2, obj.getString("boxNo"));
				past1.setString(3, obj.getString("F_cSheetno"));
				past1.setString(4, obj.getString("UserNo"));
				past1.setString(5, obj.getString("UserName"));
				past1.addBatch();
				
				past2.setString(1, obj.getString("F_cSheetno"));
				past2.addBatch();
				
			}
			past1.executeBatch();
			past2.executeBatch();
			DB.closePreparedStatement(past1);
			DB.closePreparedStatement(past2);
			//修改托盘表的状态
			
			PreparedStatement past3 = conn.prepareStatement("update t_Pallet  set iStatus =1,cStore_No =? , cStore_Name=? , user_no =? , [user_name]=?, recovery_time=?    where cPalletNo= ? ");
			past3.setString(1, obj1.getString("Head_Store_No"));
			past3.setString(2, obj1.getString("Head_Store_Name"));
			past3.setString(3, obj1.getString("cOperatorNo"));
			past3.setString(4, obj1.getString("cOperator"));
			past3.setString(5, String_Tool.DataBaseTime());
			past3.setString(6, obj1.optString("cPalletNo"));
			past3.executeUpdate();
			DB.closePreparedStatement(past3);
			
			conn.commit();
			conn.setAutoCommit(true);		
			return true;
		} catch (Exception e) {
			try {
				e.printStackTrace();
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			DB.closeConn(conn);
		}
		return false;
	}
	
	public static boolean Insert_Into_Pei_Song_Yan(Connection conn, JSONArray array) {
		try {
			conn.setAutoCommit(false);
			String cSheetno = "P_" + UUID.randomUUID().toString().hashCode();
			JSONObject obj1 = array.getJSONObject(0);
			PreparedStatement past = conn.prepareStatement("INSERT INTO wh_cStoreOutVerifySheet_online (dDate,cSheetNo,Head_Store_No,Head_Store_Name,cOperatorNo,cOperator,cStoreNo,cStoreName,bExamin,receive,cTime) values(?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, cSheetno);
			past.setString(3, obj1.getString("Head_Store_No"));
			past.setString(4, obj1.getString("Head_Store_Name"));
			past.setString(5, obj1.getString("cOperatorNo"));
			past.setString(6, obj1.getString("cOperator"));
			past.setString(7, obj1.getString("cStoreNo"));
			past.setString(8, obj1.getString("cStoreName"));
			past.setString(9,  "1");
			past.setString(10, "1");
			past.setString(11, String_Tool.DataBaseH_M_S());
			past.execute();
			DB.closePreparedStatement(past);
            //插入配送出库验货单
			PreparedStatement past1 = conn.prepareStatement("INSERT INTO wh_cStoreOutVerifySheetDetail_online (cSheetno,  iLineNo,  time, T_cSheetno, cPalletNo) values(?,?,?,?,?)");
			
			//修改分托盘单状态
			PreparedStatement past2 = conn.prepareStatement("UPDATE online_pallet  set receive='1' where cSheetno =?");

			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				past1.setString(1, cSheetno);
				past1.setString(2, ""+(i+1));
				past1.setString(3, String_Tool.DataBaseH_M_S());
				past1.setString(4, obj.getString("T_cSheetNo"));
				past1.setString(5, obj.getString("cPalletNo"));
				past1.addBatch();
				
				past2.setString(1, obj.getString("T_cSheetNo"));
				past2.addBatch();
			}
			past1.executeBatch();
			past2.executeBatch();
			DB.closePreparedStatement(past1);
			DB.closePreparedStatement(past2);
			conn.commit();
			conn.setAutoCommit(true);		
			return true;
		} catch (Exception e) {
			try {
				e.printStackTrace();
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			DB.closeConn(conn);
		}
		return false;
	}
	
	public static JSONArray Select_Tuo_Pan_goods(Connection conn) {
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past=conn.prepareStatement("select cSheetNo as T_cSheetNo,cStoreNo,cStoreName,cPalletNo from online_pallet where isnull(receive,0) ='0' ");
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DB.closeRs_Con(rs, past, conn);
		}
		return null;
	}
	
	public static JSONArray Select_Online_Pei_Song_Yan(Connection conn) {
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past=conn.prepareStatement("select top 1000 * from wh_cStoreOutVerifySheet_online  a ,wh_cStoreOutVerifySheetDetail_online b where a.cSheetno=b.cSheetno and bExamin='1' and ISNULL(b.b_Car_check,0)=0 ");
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DB.closeRs_Con(rs, past, conn);
		}
		return null;
	}
	
	
	public static String Insert_Into_onlineZhuang_Che_Dan(Connection conn, JSONArray array) {
		try {
			conn.setAutoCommit(false);
			String cSheetno = "Z_" + UUID.randomUUID().toString().hashCode();
			JSONObject obj1 = array.getJSONObject(0);
			PreparedStatement past = conn.prepareStatement("INSERT INTO online_ZhuangCheDan (dDate,cSheetNo,Head_Store_No,Head_Store_Name,cOperatorNo,cOperator,bExamin,receive,cTime) values(?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, cSheetno);
			past.setString(3, obj1.getString("Head_Store_No"));
			past.setString(4, obj1.getString("Head_Store_Name"));
			past.setString(5, obj1.getString("cOperatorNo"));
			past.setString(6, obj1.getString("cOperator"));
			past.setString(7,  "1");
			past.setString(8, "1");
			past.setString(9, String_Tool.DataBaseH_M_S());
			past.execute();
			DB.closePreparedStatement(past);
			PreparedStatement past1 = conn.prepareStatement("INSERT INTO online_ZhuangCheDanDetail (cSheetno, iLineNo,time, P_cSheetno, T_cSheetno, cPalletNo,cStoreNo,cStoreName) values(?,?,?,?,?,?,?,?)");
			PreparedStatement past2 = conn.prepareStatement("UPDATE  wh_cStoreOutVerifySheetDetail_online  set  b_Car_check ='1'  where  T_cSheetno =? ");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				past1.setString(1, cSheetno);
				past1.setString(2, ""+(i+1));
				past1.setString(3, String_Tool.DataBaseH_M_S());
				past1.setString(4, obj.getString("P_cSheetno"));
				past1.setString(5, obj.getString("T_cSheetNo"));
				past1.setString(6, obj.getString("cPalletNo"));
				past1.setString(7, obj.getString("cStoreNo"));
				past1.setString(8, obj.getString("cStoreName"));
				past1.addBatch();
	
				past2.setString(1, obj.getString("T_cSheetNo"));
				past2.addBatch();
			}
			past1.executeBatch();
			past2.executeBatch();
			DB.closePreparedStatement(past1);
			DB.closePreparedStatement(past2);
			conn.commit();
			conn.setAutoCommit(true);		
			return cSheetno ;
		} catch (Exception e) {
			try {
				e.printStackTrace();
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			DB.closeConn(conn);
		}
		return null;
	}
	
	
	public static JSONArray Select_Online_cSheetNo_below_goods(Connection conn,String cSheetno) { //传的是分拣单号，根据分拣单号差商品
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past=conn.prepareStatement("select a.cStoreNo,a.cStoreName,b.cGoodsName,b.cGoodsNo,b.cBarcode,b.fQuantity,b.num,b.Cha_yi_num,b.cUnit,b.cSpec from wh_cStoreOutWarehouse_Sort__Online a, wh_cStoreOutWarehouseDetail_Sort_Online b where a.cSheetno=? ");
			past.setString(1, cSheetno);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DB.closeRs_Con(rs, past, conn);
		}
		return null;
	}
	
	public static JSONArray Select_Online_BoxNo(Connection conn,String cSheetno) { //传的是托盘单号，根据托盘单号查箱子
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past=conn.prepareStatement("select a.cStoreNo,a.cStoreName,b.boxNo,b.F_cSheetno,b.UserNo,b.UserName  from online_pallet a,online_Pallet_Detail bwhere a.cSheetno=b.cSheetno and a.cSheetNo=? ");
			past.setString(1, cSheetno);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DB.closeRs_Con(rs, past, conn);
		}
		return null;
	}

	public static void main(String[] args) {

	}

}
