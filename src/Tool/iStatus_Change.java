package Tool;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.json.JSONArray;
import org.json.JSONObject;

import DB.DB;

public class iStatus_Change {

	public static int Set_palet_not_Use(Connection conn, String pallet_no,
			String iStatus, String user_no, String user_name, String cStore_No,
			String cStore_Name, String userType) { // 让托盘不能用
		PreparedStatement past = null;
		int a = 0;
		try {
			if (iStatus.equals("0")) {
				past = conn.prepareStatement("Update t_Pallet  set iStatus=?,user_no=?,user_name =?, recovery_time =?,cStore_No=? ,cStore_Name=?,userType=?,cStoreNo_Last=?,cWhNo_Last=? where cPalletNo=? ");
				past.setString(1, iStatus);
				past.setString(2, user_no);
				past.setString(3, user_name);
				past.setString(4, String_Tool.DataBaseTime());
				past.setString(5, cStore_No);
				past.setString(6, cStore_Name);
				past.setString(7, userType);
				past.setString(8, "");
				past.setString(9, "");
				past.setString(10, pallet_no);
				a = past.executeUpdate();
			}
			else {
				past = conn
						.prepareStatement("Update t_Pallet  set iStatus=?,user_no=?,user_name =?, recovery_time =?,cStore_No=? ,cStore_Name=?,userType=? where cPalletNo=? ");
				past.setString(1, iStatus);
				past.setString(2, user_no);
				past.setString(3, user_name);
				past.setString(4, String_Tool.DataBaseTime());
				past.setString(5, cStore_No);
				past.setString(6, cStore_Name);
				past.setString(7, userType);
				past.setString(8, pallet_no);
				a = past.executeUpdate();
			}
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return 0;
	}

	public static int Set_palet_Array_not_Use(Connection conn, JSONArray array,
			String iStatus, String user_no, String user_name, String cStore_No,
			String cStore_Name, String userType) { // 让托盘不能用
		PreparedStatement past = null;
		int a = 0;
		try {
			if (iStatus.equals("0")) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					past = conn
							.prepareStatement("Update t_Pallet  set iStatus=?,user_no=?,user_name =?, recovery_time =?,cStore_No=? ,cStore_Name=?,userType=?,cStoreNo_Last=?,cWhNo_Last=?,cSheetno_ShipSheet=? where cPalletNo=? ");
					past.setString(1, iStatus);
					past.setString(2, user_no);
					past.setString(3, user_name);
					past.setString(4, String_Tool.DataBaseTime());
					past.setString(5, cStore_No);
					past.setString(6, cStore_Name);
					past.setString(7, userType);
					past.setString(8, "");
					past.setString(9, "");
					past.setString(10, "");
					past.setString(11, obj.getString("pallet_no"));
					a = past.executeUpdate();
				}
			} else {
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					past = conn
							.prepareStatement("Update t_Pallet  set iStatus=?,user_no=?,user_name =?, recovery_time =?,cStore_No=? ,cStore_Name=?,userType=? where cPalletNo=? ");
					past.setString(1, iStatus);
					past.setString(2, user_no);
					past.setString(3, user_name);
					past.setString(4, String_Tool.DataBaseTime());
					past.setString(5, cStore_No);
					past.setString(6, cStore_Name);
					past.setString(7, userType);
					past.setString(8, obj.getString("pallet_no"));
					a = past.executeUpdate();
				}
			}
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return 0;
	}

	public static int Set_Box_not_Use(Connection conn, JSONArray array,
			String iStatus, String user_no, String user_name, String cStore_No,
			String cStore_Name, String userType) { // 回收箱子集合
		PreparedStatement past = null;
		int a = 0;
		try {
			if (iStatus.equals("0")) {
				String sql = "Update  t_DistributionBox  set iStatus=?,user_no=?,user_name =?, recovery_time =?,cStore_No=? ,cStore_Name=?,userType=?,cSheetno_Last= ?  where cDistBoxNo=?";
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					past = conn.prepareStatement(sql);
					past.setString(1, iStatus);
					past.setString(2, user_no);
					past.setString(3, user_name);
					past.setString(4, String_Tool.DataBaseTime());
					past.setString(5, cStore_No);
					past.setString(6, cStore_Name);
					past.setString(7, userType);
					past.setString(8, "");
					past.setString(9, obj.getString("box_no"));
					a = past.executeUpdate();
				}
			} else {
				String sql = "Update  t_DistributionBox  set iStatus=?,user_no=?,user_name =?, recovery_time =?,cStore_No=? ,cStore_Name=?,userType=? where cDistBoxNo=?";
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					past = conn.prepareStatement(sql);
					past.setString(1, iStatus);
					past.setString(2, user_no);
					past.setString(3, user_name);
					past.setString(4, String_Tool.DataBaseTime());
					past.setString(5, cStore_No);
					past.setString(6, cStore_Name);
					past.setString(7, userType);
					past.setString(8, obj.getString("box_no"));
					a = past.executeUpdate();
				}
			}
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return 0;
	}

	public static int Set_Box_not_Use(Connection conn, String no,
			String iStatus, String user_no, String user_name, String cStore_No,
			String cStore_Name, String userType) { // 回收单个箱子
		PreparedStatement past = null;
		int a = 0;
		try {
			if (iStatus.equals("0")) {
				past = conn.prepareStatement("Update t_DistributionBox set iStatus=?,user_no=?,user_name =?, recovery_time =?,cStore_No=? ,cStore_Name=?,userType=?,cSheetno_Last= ? where cDistBoxNo=? ");
				past.setString(1, iStatus);
				past.setString(2, user_no);
				past.setString(3, user_name);
				past.setString(4, String_Tool.DataBaseTime());
				past.setString(5, cStore_No);
				past.setString(6, cStore_Name);
				past.setString(7, userType);
				past.setString(8, "");
				past.setString(9, no);
				a = past.executeUpdate();

			} else {
				past = conn
						.prepareStatement("Update t_DistributionBox set iStatus=?,user_no=?,user_name =?, recovery_time =?,cStore_No=? ,cStore_Name=?,userType=?  where cDistBoxNo=? ");
				past.setString(1, iStatus);
				past.setString(2, user_no);
				past.setString(3, user_name);
				past.setString(4, String_Tool.DataBaseTime());
				past.setString(5, cStore_No);
				past.setString(6, cStore_Name);
				past.setString(7, userType);
				past.setString(8, no);
				a = past.executeUpdate();
			}
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return 0;
	}

}
