package DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import Tool.GetcSheetno;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

public class DB_Price_Policy {

	public static boolean Insert_Into_discount(Connection conn, JSONArray array) { //�ύ�ؼ�
		CallableStatement c = null;
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				c = conn.prepareCall("{call p_InsertPloyOfSale_App (?,?,?,?,?,?,?,?,?) }");
				c.setString(1, obj.getString("cStoreNo")
						+ String_Tool.DataBaseTime_MM().replace("-", "")
								.replace(" ", "").replace(":", ""));
				System.out.println(String_Tool.DataBaseTime().replace("-", "")
						.replace(" ", "").replace(":", ""));
				c.setString(2, obj.getString("cGoodsNo"));
				c.setString(3, obj.getString("fPrice_So"));
				c.setString(4, obj.getString("fVipValue"));
				c.setString(5, obj.getString("dDateBgn"));
				c.setString(6, obj.getString("dDateEnd"));
				c.setString(7, obj.getString("cPloyTypeNo"));
				c.setString(8, obj.getString("cPloyTypeName"));
				c.setString(9, obj.getString("cStoreNo"));
				c.execute();
			}
			JSONObject obj = array.getJSONObject(0);

			PreparedStatement past = conn.prepareStatement("update t_posstation set bDownLoad=1,bDownLoad_Ploy=1 where cStoreNo=?");
			past.setString(1, obj.getString("cStoreNo"));
			past.execute();
			DB.closePreparedStatement(past);

			PreparedStatement past1 = conn
					.prepareStatement("update t_Store set bDownLoad=1 where cStoreNo=?");
			past1.setString(1, obj.getString("cStoreNo"));
			past1.execute();
			DB.closePreparedStatement(past1);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(c, conn);
		}
		return false;
	}

	public static JSONArray Select_GoodsPloy(Connection conn, String date1,
			String date2, String cStoreNo, String cGoodsNo) { // ��ѯ���ͳ��ⵥ����Ʒ
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call p_SelectGoodsPloyISNOTNull (?,?,?,?) }");
			c.setString(1, cStoreNo);
			c.setString(2, cGoodsNo);
			c.setString(3, date1);
			c.setString(4, date2);
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
	public static JSONArray Select_GoodsPloy(Connection conn,
			String cStoreNo, String cPloyNo) { // ��ѯ���ͳ��ⵥ����Ʒ
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			System.out.println("heheh");
			c = conn.prepareCall("{call p_SelectGoodsPloyISNOTNull_ALL (?,?) }");
			c.setString(1, cStoreNo);
			c.setString(2, cPloyNo);
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
	public static JSONArray Select_GoodsPloy(Connection conn,
			String cStoreNo) { // ��ѯ���ͳ��ⵥ����Ʒ
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past=conn.prepareStatement("select cPloyNo from  t_PloyOfSale where cStoreNo=  ?");
			past.setString(1, cStoreNo);
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

	public static boolean Insert_Into_Vip(Connection conn, JSONArray array) { // ��ѯ���ͳ��ⵥ����Ʒ

		String cSheetno="";
		try {
			JSONObject obj = array.getJSONObject(0);
			cSheetno=GetcSheetno.get_vip_no(conn,String_Tool.DataBaseYear(), obj.getString("cStoreNo"));
			PreparedStatement past = conn.prepareStatement("INSERT INTO dbo.t_VipcGoodsPrice (dDate,dDateBgn,dDateEnd,cSheetNo,cFillEmpNo,cFillEmp,dFillin,cFillintime,bExamin,bReceive,cStoreNo,cStoreName, Week1, Week2, Week3, Week4, Week5, Week6, Week7, Hour0, Hour1,Hour2,Hour3,Hour4,Hour5,Hour6,Hour7,Hour8, Hour9,Hour10,Hour11,Hour12,Hour13,Hour14,Hour15,Hour16,Hour17,Hour18,Hour19,Hour20,Hour21,Hour22,Hour23)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, obj.getString("dDateBgn"));
			past.setString(3, obj.getString("dDateEnd"));
			past.setString(4, cSheetno);
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, String_Tool.DataBaseYear_Month_Day());
			past.setString(8, String_Tool.DataBaseH_M_S());
			past.setString(9, "1");
			past.setString(10,"1");
			past.setString(11, obj.getString("cStoreNo"));
			past.setString(12, obj.getString("cStoreName"));
			
			past.setString(13, "1");
			past.setString(14, "1");
			past.setString(15, "1");
			past.setString(16, "1");
			past.setString(17, "1");
			
			past.setString(18, "1");
			past.setString(19, "1");
			past.setString(20, "1");
			past.setString(21, "1");
			past.setString(22, "1");
			
			past.setString(23, "1");
			past.setString(24, "1");
			past.setString(25, "1");
			past.setString(26, "1");
			past.setString(27, "1");
			
			past.setString(28, "1");
			past.setString(29, "1");
			past.setString(30, "1");
			past.setString(31, "1");
			past.setString(32, "1");
			
			past.setString(33, "1");
			past.setString(34, "1");
			past.setString(35, "1");
			past.setString(36, "1");
			past.setString(37, "1");
			
			past.setString(38, "1");
			past.setString(39, "1");
			past.setString(40, "1");
			past.setString(41, "1");
			past.setString(42, "1");
			past.setString(43, "1");
			past.execute();
			DB.closePreparedStatement(past);
			
			for (int i = 0; i < array.length(); i++) {
				PreparedStatement past1=conn.prepareStatement("INSERT INTO t_VipcGoodsPriceDetail (cSheetno,iLineNo,cGoodsNo,cGoodsName,cBarcode,cUnitedNo,fInPrice,fNoTaxPrice,fVipPrice,fVipPrice_student,fVipValue,cUnit,cSpec,bLimitQty,fLimitQty,fShiDuanQty, Week1, Week2, Week3, Week4, Week5, Week6, Week7, Hour0, Hour1,Hour2,Hour3,Hour4,Hour5,Hour6,Hour7,Hour8, Hour9,Hour10,Hour11,Hour12,Hour13,Hour14,Hour15,Hour16,Hour17,Hour18,Hour19,Hour20,Hour21,Hour22,Hour23)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				JSONObject obj1 = array.getJSONObject(i);
				past1.setString(1, cSheetno);
				past1.setString(2, ""+i);
				past1.setString(3, obj1.getString("cGoodsNo"));
				past1.setString(4, obj1.getString("cGoodsName"));
				past1.setString(5, obj1.getString("cBarcode"));
				past1.setString(6, "");
				past1.setString(7, obj1.getString("fInPrice"));
				past1.setString(8, obj1.getString("fNoTaxPrice"));
				past1.setString(9, obj1.getString("fVipPrice"));
				past1.setString(10, obj1.getString("fVipPrice_student"));
				past1.setString(11, obj1.getString("fVipValue"));
				past1.setString(12, obj1.getString("cUnit"));
				past1.setString(13, obj1.getString("cSpec"));
				past1.setString(14, obj1.getString("bLimitQty"));
				past1.setString(15, obj1.getString("fLimitQty"));
				past1.setString(16, obj1.getString("fShiDuanQty"));
				past1.setString(17, "1");
				
				past1.setString(18, "1");
				past1.setString(19, "1");
				past1.setString(20, "1");
				past1.setString(21, "1");
				past1.setString(22, "1");
				
				past1.setString(23, "1");
				past1.setString(24, "1");
				past1.setString(25, "1");
				past1.setString(26, "1");
				past1.setString(27, "1");
				
				past1.setString(28, "1");
				past1.setString(29, "1");
				past1.setString(30, "1");
				past1.setString(31, "1");
				past1.setString(32, "1");
				
				past1.setString(33, "1");
				past1.setString(34, "1");
				past1.setString(35, "1");
				past1.setString(36, "1");
				past1.setString(37, "1");
				
				past1.setString(38, "1");
				past1.setString(39, "1");
				past1.setString(40, "1");
				past1.setString(41, "1");
				past1.setString(42, "1");
				
				past1.setString(43, "1");
				past1.setString(44, "1");
				past1.setString(45, "1");
				past1.setString(46, "1");
				past1.setString(47, "1");
				past1.execute();
				DB.closePreparedStatement(past1);
			}
			PreparedStatement past2=conn.prepareStatement("update t_Store set bDownLoad=1 where cStoreNo=? ");
			past2.setString(1, obj.getString("cStoreNo"));
			past2.execute();
			DB.closePreparedStatement(past2);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeConn(conn);
		}
		return false;
	}

}
