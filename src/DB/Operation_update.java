package DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

public class Operation_update {

	public static JSONArray Check_in_Select(Connection conn) { // 签到
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call check_in }");
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closeRs_Con(c, conn);
		}
		return null;
	}

	public static String Driver_Sign_in(Connection conn,
			String TruckLicenseTag, String DriverNo, String fage,
			String user_name) { // 扫描车绑定
		ResultSet rs = null;
		String a = null;
		PreparedStatement past1 = null;
		String sql = "Update  t_Driver  set  TruckLicenseTag =? where  DriverNo = ? ";
		try {
			past1 = conn.prepareStatement(sql);
			if (fage.equals("0")) { // 签到
				PreparedStatement past = conn
						.prepareStatement("select * from t_Driver where  TruckLicenseTag =?");
				past.setString(1, TruckLicenseTag);
				rs = past.executeQuery();
				if (rs.next()) {
					String Driver = rs.getString("Driver");
					String DriverN = rs.getString("DriverNo");
					if (DriverNo.equals(DriverN)) {
						a = "3"; // 已经签到
						LoggerUtil.info(DriverNo + "" + DriverN);
					} else {
						a = Driver;
					}
					return a;
				}
				past1.setString(1, TruckLicenseTag);
			}

			else {
				past1.setString(1, ""); // 取消绑定
			}
			past1.setString(2, DriverNo);
			a = "" + past1.executeUpdate();

			PreparedStatement past2 = conn
					.prepareStatement("INSERT INTO Driver_Sign_log values (?,?,?,?,?)");
			past2.setString(1, DriverNo);
			past2.setString(2, user_name);
			past2.setString(3, String_Tool.DataBaseTime());
			past2.setString(4, TruckLicenseTag);
			past2.setString(5, fage); // 0是签到,1是下班
			past2.execute();
			DB.closePreparedStatement(past2);

			return a;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past1);
			DB.closeConn(conn);
		}
		return "0";
	}

	public static String OK_Send(Connection conn, String UserNo,
			String DriverName, String Store_no, String Store_name,
			String cSheetno) { // 扫描车绑定

		String sql = "INSERT INTO Driver_Ok (DriverNo,DriverName,Store_no,Store_name,Date_time,zhuang_che_cSheetno) values(?,?,?,?,?,?) ";
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement(sql);
			past.setString(1, UserNo);
			past.setString(2, DriverName);
			past.setString(3, Store_no);
			past.setString(4, Store_name);
			past.setString(5, String_Tool.DataBaseTime());
			past.setString(6, cSheetno);
			int a = past.executeUpdate();
			return "" + a;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return "0";

	}

	public static JSONArray Select_Box_Pallet_Position(Connection conn,
			String fage) { //

		String sql = null;
		if (fage.equals("1")) {
			sql = " select * from t_Pallet";
		} else {
			sql = " select * from t_DistributionBox";
		}
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement(sql);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return null;
	}

	public static JSONArray Select_Box_Pallet_Position(Connection conn,
			String fage, String cStoreno) { //

		String sql = null;
		if (fage.equals("1")) {
			sql = " select * from t_Pallet where cStore_No= ? ";
		} else {
			sql = " select * from t_DistributionBox  where cStore_No=? ";
		}
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement(sql);
			past.setString(1, "" + cStoreno);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return null;
	}

	public static JSONArray Select_All_t_Truck(Connection conn) { // 扫描车绑定

		String sql = "Select * from t_Truck";
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement(sql);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return null;
	}

	public static JSONArray Select_All_t_Store(Connection conn) { // 扫描车绑定

		String sql = "select cStoreNo,   cStoreName,    cID_2D,   cStyle   from t_Store";
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement(sql);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return null;
	}

	public static JSONArray Select_Xiao_Piao(Connection conn, String cGoods,
			String cStoreNo, String startdate, String enddate) { // 签到
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{ call selec_xiao_piao (?,?,?,?) }");
			c.setString(1, cGoods);
			c.setString(2, cStoreNo);
			c.setString(3, startdate);
			c.setString(4, enddate);
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

	public static JSONArray Select_Xiao_Piao_Content(Connection conn,
			String cStoreno, String dSaleDate, String cSaleSheetno) { // 扫描车绑定

		String sql = "select dSaleDate,cSaleSheetno,fVipRate,fLastSettle0 ,fLastSettle,cGoodsNo,"
				+ "cGoodsName,cBarCode,cOperatorno,cOperatorName,fVipScore,"
				+ "fPrice,fNormalSettle,fVipPrice,fVipRate,fQuantity,bWeight,fNormalVipScore,cSaleTime "
				+ "from t_SaleSheetDetail where cSaleSheetno= ? and dSaleDate=? and cStoreNo=?";
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement(sql);
			past.setString(1, cSaleSheetno);
			past.setString(2, dSaleDate);
			past.setString(3, cStoreno);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return null;
	}
	
	
	public static boolean hand_in_money(Connection conn, String RMB,String ZFB,String WeiXIN,String Chu_Zhi_Ka,String Bank_Union,String cStoreNo,String cStoreName,
			String UserName,String UserNo,String SuperNo,String SuperName,String Wallet) { // 签到
		PreparedStatement past=null;
		try {
			past=conn.prepareStatement("INSERT INTO Hand_in_Money  (dDate,cStoreNo,cStoreName,RMB,ZFB,WeiXIN,Chu_Zhi_Ka,Bank_Union,Wallet,UserNo,UserName,SuperNo,SuperName) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseTime());
			past.setString(2, cStoreNo);
			past.setString(3, cStoreName);
			past.setString(4, RMB);
			past.setString(5, ZFB);
			past.setString(6, WeiXIN);
			past.setString(7, Chu_Zhi_Ka);
			past.setString(8, Bank_Union);
			past.setString(9, Wallet);
			past.setString(10, UserNo);
			past.setString(11, UserName);
			past.setString(12, SuperNo);
			past.setString(13,SuperName);
			past.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return false;
	}
	
	public static JSONArray Select_Pan_dian_Cha_Yi(Connection conn, String cCheckTaskNo,String type) { // 盘点差异
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call p_cStoreCheckTaskDiff (?,?) }");
			c.setString(1, cCheckTaskNo);
			c.setString(2, type);
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
	public static String Update_Pass(Connection conn, String UserNo,String OldPass,String NewPass) { // 修改密码
		PreparedStatement past=null;
		ResultSet rs=null;
		try {
			 past = conn.prepareStatement("select top 1  [User] from t_Pass  where [User]= ? and Pass=? ");
			 past.setString(1, UserNo);
			 past.setString(2, OldPass);
			 rs=past.executeQuery();
			 LoggerUtil.info(rs.toString());
			 if(rs.next()){
				 past = conn.prepareStatement("Update t_Pass set Pass=?   where [User]= ? and Pass=? ");
				 past.setString(1, NewPass);
				 past.setString(2, UserNo);
				 past.setString(3, OldPass);
				 past.execute();
				 return ""+1;
			 }
			 else{
				 return ""+2;
			 }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return ""+0;
	}
	public static JSONArray Select_MoneyCar_Or_VipCard_Message(Connection conn, String card,String sql) { // 签到
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past= conn.prepareStatement(sql);
			past.setString(1, card);
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
	public static int Update_Vip_message(Connection conn,JSONObject obj) { // 修改會員卡信息
		PreparedStatement past=null;
		try {
			 past = conn.prepareStatement("update dbo.t_Vip set cVipName=?,cSex=?,dBirthday=?,cTel=?,cIdCard=?,cHome=? where cVipno=?");
			 past.setString(1, obj.getString("cVipName"));
			 past.setString(2, obj.getString("cSex"));
			 past.setString(3, obj.getString("dBirthday"));
			 past.setString(4, obj.getString("cTel"));
			 past.setString(5, obj.getString("cIdCard"));
			 past.setString(6, obj.getString("cHome"));
			 past.setString(7, obj.getString("cVipno"));
			 int a=past.executeUpdate();
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return 0;
	}
	public static int Update_Money_card_message(Connection conn,JSONObject obj) { // 修改會員卡信息
		PreparedStatement past=null;
		try {
			 past = conn.prepareStatement("update Supermarket.dbo.moneycard set home=?,tel=? where cardno=? ");
			 past.setString(1, obj.getString("cHome"));
			 past.setString(2, obj.getString("cTel"));
			 past.setString(3, obj.getString("moneycardno"));
			 int a=past.executeUpdate();
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return 0;
	}

	public static void main(String args[]) {
	
	}

}
