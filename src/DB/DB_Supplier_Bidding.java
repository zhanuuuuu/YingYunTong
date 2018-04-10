package DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Tool.ResultSet_To_JSON;
import Tool.String_Tool;
import bean.Bidding_Order;
import bean.Head_Request_Order;
import bean.Head_affirm_Order;
import bean.Head_affirm_Store_Goods_Detail;
import bean.Store_Request;
import bean.cSheetNo;

public class DB_Supplier_Bidding {

	public static String Supplier_login(Connection conn, String cSupNo, String cSupPassword) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select * from dbo.t_Supplier where cSupNo=? and cSupPassword=? ");
			past.setString(1, cSupNo);
			past.setString(2, cSupPassword);
			rs = past.executeQuery();
			if (rs.next()) {
				return "" + 1;
			} else {
				return "" + 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		return "" + 0;
	}

	public static List<Head_Request_Order> select_head_request_order(Connection conn, String cSupNo, String dDate) {
		CallableStatement c = null;
		ResultSet rs = null;
		try { // p_GetcSupBhApplyFresh_PPC '1117','2017-03-12'
			c = conn.prepareCall("{call p_GetcSupBhApplyFresh_PPC (?,?)}");
			c.setString(1, cSupNo);
			c.setString(2, dDate);
			rs = c.executeQuery();
			List<Head_Request_Order> list = new ArrayList<Head_Request_Order>();
			Head_Request_Order head_Request_Order = new Head_Request_Order();
			while (rs.next()) {
				head_Request_Order = new Head_Request_Order();
				String cStoreName = rs.getString("cStoreName");
				String fQty = rs.getString("fQty");
				String cUnit = rs.getString("cUnit");
				String cGoodsNo = rs.getString("cGoodsNo");
				String cGoodsName = rs.getString("cGoodsName");
				String cSpec = rs.getString("cSpec");

				head_Request_Order.setcGoodsName(cGoodsName);
				head_Request_Order.setcUnit(cUnit);
				head_Request_Order.setfQty(fQty);
				head_Request_Order.setcGoodsNo(cGoodsNo);
				head_Request_Order.setcStoreName(cStoreName);
				head_Request_Order.setcSpec(cSpec);
				CallableStatement c1 = conn.prepareCall("{call p_GetcSupBhApplyFresh_PPC1 (?,?,?)}");
				c1.setString(1, cSupNo);
				c1.setString(2, dDate);
				c1.setString(3, cGoodsNo);
				ResultSet rs1 = c1.executeQuery();

				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs1);
				Gson gson = new Gson();
				List<Store_Request> Store_Request_list = gson.fromJson(array.toString(),new TypeToken<List<Store_Request>>() {}.getType());
				head_Request_Order.setStore_Request_list(Store_Request_list);
				list.add(head_Request_Order);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(c, conn);
		}
		return null;
	}

	public static boolean upload_New_Products(Connection conn, JSONArray array) {
		PreparedStatement past = null;
		try {
			String sql = "insert into New_Products  (cBarcode,cGoodsName,cUnit,cSpec,num,Price,Tel,Applicant,cSupNo,Image_Path,dDate)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?)";
			past = conn.prepareStatement(sql);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				past.setString(1, obj.getString("cBarcode"));
				past.setString(2, obj.getString("cGoodsName"));
				past.setString(3, obj.getString("cUnit"));
				past.setString(4, obj.getString("cSpec"));
				past.setString(5, obj.getString("num"));
				past.setString(6, obj.getString("Price"));
				past.setString(7, obj.getString("Tel"));
				past.setString(8, obj.getString("Applicant"));
				past.setString(9, obj.getString("cSupNo"));
				past.setString(10, obj.getString("Image_Path"));
				past.setString(11, String_Tool.DataBaseYear_Month_Day());
				past.addBatch();
			}
			past.executeBatch();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return false;
	}

	public static JSONArray select_New_Products(Connection conn, String cSupNo) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select * from New_Products where cSupNo= ? ");
			past.setString(1, cSupNo);
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

	public static boolean Upload_Bidding(Connection conn, JSONArray array, String Head_cStoreNo,String Head_cStoreName) {
		PreparedStatement past = null;
		try {
			conn.setAutoCommit(false);
			String sql = "insert into Bidding_Detail (Bidding_cSheetno,cGoodsNo,cGoodsName,fQuantity,cUnit,cSpec,cGoodsNo_State,price,Image_Path,iLine,fImoney)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?)";
			past = conn.prepareStatement(sql);
			JSONObject obj1 = array.getJSONObject(0);
			String sheetno = "J" + obj1.getString("cSpuer_No") + String_Tool.reformat();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				past.setString(1, sheetno);
				past.setString(2, obj.getString("cGoodsNo"));
				past.setString(3, obj.getString("cGoodsName"));
				past.setString(4, obj.getString("fQuantity"));
				past.setString(5, obj.getString("cUnit"));
				past.setString(6, obj.getString("cSpec"));
				past.setString(7, obj.getString("cGoodsNo_State"));
				past.setString(8, obj.getString("price"));
				past.setString(9, obj.getString("Image_Path"));
				past.setString(10, "" + (i + 1));
				double fImoney = Double.parseDouble(obj.getString("fQuantity"))
						* Double.parseDouble(obj.getString("price"));
				past.setString(11, "" + String_Tool.String_IS_Four(fImoney));
				past.addBatch();

				JSONArray array1 = obj.getJSONArray("branchStoreList");
				PreparedStatement past2 = conn.prepareStatement(
						"insert into Bidding_Store_Goods_Detail (cStoreNo,cStoreName,cGoodsNo,fQuantity,Bidding_cSheetno) values(?,?,?,?,?)");
				for (int j = 0; j < array1.length(); j++) {
					JSONObject obj2 = array1.getJSONObject(j);
					past2.setString(1, obj2.getString("cStoreNo"));
					past2.setString(2, obj2.getString("cStoreName"));
					past2.setString(3, obj.getString("cGoodsNo"));
					past2.setString(4, obj2.getString("fQuantity"));
					past2.setString(5, sheetno);
					past2.execute();
				}
			}
			past.executeBatch();
			JSONObject obj = array.getJSONObject(0);
			PreparedStatement past1 = conn.prepareStatement(
					"insert into Bidding  (Bidding_cSheetno,dDate,cSpuer_No,cSpuer,StartTime,EndTime,Bidding_State,num,Head_cStoreNo,Head_cStoreName) values(?,?,?,?,?,?,?,?,?,?)");
			past1.setString(1, sheetno);
			past1.setString(2, String_Tool.DataBaseYear_Month_Day());
			past1.setString(3, obj.getString("cSpuer_No"));
			past1.setString(4, obj.getString("cSpuer"));
			past1.setString(5, obj.getString("StartTime"));
			past1.setString(6, obj.getString("EndTime"));
			past1.setString(7, "0");
			past1.setString(8, "" + array.length());
			past1.setString(9, "" + Head_cStoreNo);
			past1.setString(10, "" + Head_cStoreName);
			past1.execute();
			DB.closePreparedStatement(past1);
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

	public static boolean Head_Store_Ok_bidding_Order(Connection conn, JSONArray array) {
		PreparedStatement past = null;
		try {
			conn.setAutoCommit(false);
			String sql = "insert into Head_affirm_Detail (Head_affirm_cSheetno,cGoodsNo,cGoodsName,fQuantity,cUnit,cSpec,cGoodsNo_State,price,Image_Path,iLine,fImoney)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?)";
			past = conn.prepareStatement(sql);
			JSONObject obj1 = array.getJSONObject(0);
			String sheetno = "K" + String_Tool.reformat() + obj1.getString("cSpuer_No");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				past.setString(1, sheetno);
				past.setString(2, obj.getString("cGoodsNo"));
				past.setString(3, obj.getString("cGoodsName"));
				past.setString(4, obj.getString("fQuantity"));
				past.setString(5, obj.getString("cUnit"));
				past.setString(6, obj.getString("cSpec"));
				past.setString(7, obj.getString("cGoodsNo_State"));
				past.setString(8, obj.getString("price"));
				past.setString(9, obj.getString("Image_Path"));
				past.setString(10, "" + (i + 1));
				double fImoney = Double.parseDouble(obj.getString("fQuantity"))
						* Double.parseDouble(obj.getString("price"));
				past.setString(11, "" + String_Tool.String_IS_Four(fImoney));
				past.addBatch();

				JSONArray array1 = obj.getJSONArray("StoreList");

				PreparedStatement past2 = conn.prepareStatement(
						"insert into Head_affirm_Store_Goods_Detail (cStoreNo,cStoreName,cGoodsNo,fQuantity,Head_affirm_cSheetno) values(?,?,?,?,?)");
				for (int j = 0; j < array1.length(); j++) {
					JSONObject obj2 = array1.getJSONObject(j);
					past2.setString(1, obj2.getString("cStoreNo"));
					past2.setString(2, obj2.getString("cStoreName"));
					past2.setString(3, obj.getString("cGoodsNo"));
					past2.setString(4, obj2.getString("fQuantity"));
					past2.setString(5, sheetno);
					past2.execute();
				}
			}
			past.executeBatch();
			JSONObject obj = array.getJSONObject(0);
			PreparedStatement past1 = conn.prepareStatement("insert into Head_affirm "
					+ "(Head_affirm_cSheetno,dDate,Head_cStoreNo,Head_cStoreName,cOperatorNo,cOperator,"
					+ "StartTime,EndTime,Head_affirm_State,Receiving_WayId,Receiving_Way,cSpuer_No,cSpuer,iSsort,num) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past1.setString(1, sheetno);
			past1.setString(2, String_Tool.DataBaseYear_Month_Day());
			past1.setString(3, obj.getString("Head_cStoreNo"));
			past1.setString(4, obj.getString("Head_cStoreName"));
			past1.setString(5, obj.getString("cOperatorNo"));
			past1.setString(6, obj.getString("cOperator"));
			past1.setString(7, obj.getString("StartTime"));
			past1.setString(8, obj.getString("EndTime"));
			past1.setString(9, "0");
			past1.setString(10, obj.getString("Receiving_WayId"));
			past1.setString(11, obj.getString("Receiving_Way"));
			past1.setString(12, obj.getString("cSpuer_No"));
			past1.setString(13, obj.getString("cSpuer"));
			past1.setString(14, obj.getString("iSsort"));
			past1.setString(15, "" + array.length());
			past1.execute();
			DB.closePreparedStatement(past1);

			PreparedStatement past2 = conn
					.prepareStatement("update Bidding set Bidding_State ='2' where Bidding_cSheetno=? ");
			past2.setString(1, obj.getString("Bidding_cSheetno"));
			past2.executeUpdate();
			DB.closePreparedStatement(past2);

			PreparedStatement past3 = conn.prepareStatement(
					"insert into Head_affirm_Bidding_cSheetno (Head_affirm_cSheetno,Bidding_cSheetno,dDate) values (?,?,?)");
			past3.setString(1, sheetno);
			past3.setString(2, obj.getString("Bidding_cSheetno"));
			past3.setString(3, String_Tool.DataBaseYear_Month_Day());
			past3.executeUpdate();

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

	public static JSONArray select_Customer_Order(Connection conn, String cSupNo, String dDate) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "select Distinct Head_cStoreName,Head_cStoreNo from Head_affirm where cSpuer_No=? and dDate=? and Head_affirm_State ='0' ";
			past = conn.prepareStatement(sql);
			past.setString(1, cSupNo);
			past.setString(2, dDate);
			LoggerUtil.info(cSupNo);
			LoggerUtil.info(dDate);
			rs = past.executeQuery();
			JSONArray array = new JSONArray();
			JSONObject obj = new JSONObject();
			while (rs.next()) {
				obj = new JSONObject();
				String Head_cStoreName = rs.getString("Head_cStoreName");
				String Head_cStoreNo = rs.getString("Head_cStoreNo");
				obj.put("Head_cStoreNo", Head_cStoreNo);
				obj.put("Head_cStoreName", Head_cStoreName);

				String sql1 = "select Head_affirm_cSheetno,dDate,Head_cStoreName,Receiving_Way,Head_affirm_State from Head_affirm where cSpuer_No=? and Head_affirm_State ='0'  ";
				PreparedStatement past1 = conn.prepareStatement(sql1);
				past1.setString(1, cSupNo);
				ResultSet rs1 = past1.executeQuery();

				JSONArray array1 = ResultSet_To_JSON.resultSetToJsonArray(rs1);
				obj.put("list", array1);
				array.put(obj);
			}
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, past, null, conn);
		}
		return null;
	}

	public static JSONArray select_unshipped_cSheetno(Connection conn, String cSupNo, String dDate) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "select Distinct Head_cStoreName,Head_cStoreNo from Head_affirm where cSpuer_No=? and dDate=? and (Head_affirm_State ='0' or Head_affirm_State ='1' or Head_affirm_State ='2') ";
			past = conn.prepareStatement(sql);
			past.setString(1, cSupNo);
			past.setString(2, dDate);
			LoggerUtil.info(cSupNo);
			LoggerUtil.info(dDate);
			rs = past.executeQuery();
			JSONArray list = new JSONArray();
			JSONObject obj = new JSONObject();
			while (rs.next()) {
				obj = new JSONObject();
				String Head_cStoreName = rs.getString("Head_cStoreName");
				String Head_cStoreNo = rs.getString("Head_cStoreNo");
				obj.put("Head_cStoreName", Head_cStoreName);
				obj.put("Head_cStoreNo", Head_cStoreNo);
				String sql1 = "select Head_affirm_cSheetno,dDate,Receiving_Way,Head_affirm_State from Head_affirm where cSpuer_No=?  and dDate=?  and (Head_affirm_State ='0' or Head_affirm_State ='1' or Head_affirm_State ='2')";
				PreparedStatement past1 = conn.prepareStatement(sql1);
				past1.setString(1, cSupNo);
				past1.setString(2, dDate);
				ResultSet rs1 = past1.executeQuery();
				JSONArray array1 = ResultSet_To_JSON.resultSetToJsonArray(rs1);
				obj.put("list", array1);
				list.put(obj);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, past, null, conn);
		}
		return null;
	}

	public static JSONArray select_Yishipped_cSheetno(Connection conn, String cSupNo, String dDate) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "select Distinct Head_cStoreName,Head_cStoreNo from Head_affirm where cSpuer_No=? and dDate=? and (Head_affirm_State ='3' or Head_affirm_State ='4') ";
			past = conn.prepareStatement(sql);
			past.setString(1, cSupNo);
			past.setString(2, dDate);
			LoggerUtil.info(cSupNo);
			LoggerUtil.info(dDate);
			rs = past.executeQuery();
			JSONArray list = new JSONArray();
			JSONObject obj = new JSONObject();
			while (rs.next()) {
				obj = new JSONObject();
				String Head_cStoreName = rs.getString("Head_cStoreName");
				String Head_cStoreNo = rs.getString("Head_cStoreNo");
				obj.put("Head_cStoreName", Head_cStoreName);
				obj.put("Head_cStoreNo", Head_cStoreNo);
				String sql1 = "select Head_affirm_cSheetno,dDate,Receiving_Way,Head_affirm_State from Head_affirm where cSpuer_No=? and dDate=? and (Head_affirm_State ='3' or Head_affirm_State ='4')";
				PreparedStatement past1 = conn.prepareStatement(sql1);
				past1.setString(1, cSupNo);
				past1.setString(2, dDate);
				ResultSet rs1 = past1.executeQuery();
				JSONArray array1 = ResultSet_To_JSON.resultSetToJsonArray(rs1);
				obj.put("list", array1);
				list.put(obj);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, past, null, conn);
		}
		return null;
	}

	public static JSONArray Driver_Select_Order(Connection conn, String dDate, String Head_cStoreNo) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "select * from Head_affirm where  dDate=? and Head_cStoreNo=? and (Head_affirm_State='0' or Head_affirm_State='1' or Head_affirm_State='2' or Head_affirm_State='3')"; // 已经备货
			past = conn.prepareStatement(sql);
			past.setString(1, dDate);
			past.setString(2, Head_cStoreNo);
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

	public static ArrayList<Bidding_Order> select_Select_Bidding_Order(Connection conn, String cSupNo, String dDate) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "select Distinct Bidding_State from Bidding where cSpuer_No=? and dDate=? ";
			past = conn.prepareStatement(sql);
			past.setString(1, cSupNo);
			past.setString(2, dDate);
			rs = past.executeQuery();
			ArrayList<Bidding_Order> list = new ArrayList<Bidding_Order>();
			Bidding_Order a = new Bidding_Order();
			while (rs.next()) {
				a = new Bidding_Order();
				String bidding_State = rs.getString("Bidding_State");
				a.setBidding_State(bidding_State);
				String sql1 = "select  Bidding_cSheetno,dDate from Bidding where Bidding_State=? and dDate=? and cSpuer_No=? ";
				PreparedStatement past1 = conn.prepareStatement(sql1);
				past1.setString(1, bidding_State);
				past1.setString(2, dDate);
				past1.setString(3, cSupNo);
				ResultSet rs1 = past1.executeQuery();
				Gson gson = new Gson();
				JSONArray array1 = ResultSet_To_JSON.resultSetToJsonArray(rs1);
				List<cSheetNo> as = gson.fromJson(array1.toString(), new TypeToken<List<cSheetNo>>() {
				}.getType());
				LoggerUtil.info(array1);
				a.setList(as);
				list.add(a);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		return null;
	}

	public static JSONArray head_Select_bidding_Order(Connection conn, String Head_cStoreNo, String dDate) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "select * from Bidding where Head_cStoreNo=? and dDate=? and Bidding_State='0' ";
			past = conn.prepareStatement(sql);
			past.setString(1, Head_cStoreNo);
			past.setString(2, dDate);
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

	public static JSONArray Select_Bidding_OrderContent(Connection conn, String cSheetNo) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select * from Bidding_Detail where Bidding_cSheetno=? ");
			past.setString(1, cSheetNo);
			rs = past.executeQuery();
			JSONArray array = new JSONArray();
			JSONObject obj = new JSONObject();
			while (rs.next()) {
				String fQuantity = rs.getString("fQuantity");
				String cUnit = rs.getString("cUnit");
				String cGoodsNo = rs.getString("cGoodsNo");
				String cGoodsName = rs.getString("cGoodsName");
				String cSpec = rs.getString("cSpec");
				String Image_Path = rs.getString("Image_Path");
				String price = rs.getString("price");

				obj = new JSONObject();
				obj.put("cGoodsName", cGoodsName);
				obj.put("cUnit", cUnit);
				obj.put("fQuantity", fQuantity);
				obj.put("cGoodsNo", cGoodsNo);
				obj.put("cSpec", cSpec);
				obj.put("Image_Path", Image_Path);
				obj.put("price", price);

				PreparedStatement past1 = conn.prepareStatement(
						"select * from Bidding_Store_Goods_Detail where Bidding_cSheetno=? and cGoodsNo=? ");
				past1.setString(1, cSheetNo);
				past1.setString(2, cGoodsNo);
				ResultSet rs1 = past1.executeQuery();
				JSONArray array1 = ResultSet_To_JSON.resultSetToJsonArray(rs1);
				obj.put("StoreList", array1);
				array.put(obj);
			}
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, past, null, conn);
		}
		return null;
	}

	public static JSONArray head_Select_affirm_Order(Connection conn, String Head_cStoreNo, String dDate) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "select * from Head_affirm where Head_cStoreNo=? and dDate=? ";
			past = conn.prepareStatement(sql);
			past.setString(1, Head_cStoreNo);
			past.setString(2, dDate);
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

	public static List<Head_affirm_Order> Select_Customer_OrderContent(Connection conn, String cSheetNo) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select * from Head_affirm_Detail where Head_affirm_cSheetno=? ");
			past.setString(1, cSheetNo);
			rs = past.executeQuery();
			ArrayList<Head_affirm_Order> list = new ArrayList<Head_affirm_Order>();
			Head_affirm_Order head_affirm_Order = new Head_affirm_Order();
			while (rs.next()) {
				String fQuantity = rs.getString("fQuantity");
				String cUnit = rs.getString("cUnit");
				String cGoodsNo = rs.getString("cGoodsNo");
				String cGoodsName = rs.getString("cGoodsName");
				String cSpec = rs.getString("cSpec");
				String Image_Path = rs.getString("Image_Path");
				String price = rs.getString("price");

				head_affirm_Order = new Head_affirm_Order();
				head_affirm_Order.setcGoodsName(cGoodsName);
				head_affirm_Order.setcUnit(cUnit);
				head_affirm_Order.setfQuantity(fQuantity);
				head_affirm_Order.setcGoodsNo(cGoodsNo);
				head_affirm_Order.setcSpec(cSpec);
				head_affirm_Order.setImage_Path(Image_Path);
				head_affirm_Order.setPrice(price);

				PreparedStatement past1 = conn.prepareStatement(
						"select * from Head_affirm_Store_Goods_Detail where Head_affirm_cSheetno=? and cGoodsNo=? ");
				past1.setString(1, cSheetNo);
				past1.setString(2, cGoodsNo);
				ResultSet rs1 = past1.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs1);
				Gson gson = new Gson();
				List<Head_affirm_Store_Goods_Detail> storeList = gson.fromJson(array.toString(),
						new TypeToken<List<Head_affirm_Store_Goods_Detail>>() {
						}.getType());

				head_affirm_Order.setStoreList(storeList);
				list.add(head_affirm_Order);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, past, null, conn);
		}
		return null;
	}

	public static JSONArray Select_Select_Prepare_cSheetno(Connection conn, String cSpuer_No, String dDate) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement(
					"select Distinct Head_cStoreNo,Head_cStoreName from dbo.Head_affirm where cSpuer_No=? and dDate=? and (Head_affirm_State='1' or Head_affirm_State='2' )");
			past.setString(1, cSpuer_No);
			past.setString(2, dDate);
			rs = past.executeQuery();
			JSONObject obj = new JSONObject();
			JSONArray array = new JSONArray();
			while (rs.next()) {
				String Head_cStoreNo = rs.getString("Head_cStoreNo");
				String Head_cStoreName = rs.getString("Head_cStoreName");
				obj = new JSONObject();
				obj.put("Head_cStoreNo", Head_cStoreNo);
				obj.put("Head_cStoreName", Head_cStoreName);

				PreparedStatement past1 = conn.prepareStatement(
						"select * from dbo.Head_affirm where Head_cStoreNo=? and (Head_affirm_State='1' or Head_affirm_State='2' )  ");
				past1.setString(1, Head_cStoreNo);
				ResultSet rs1 = past1.executeQuery();
				JSONArray array1 = ResultSet_To_JSON.resultSetToJsonArray(rs1);
				obj.put("list", array1);
				array.put(obj);
			}
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, past, null, conn);
		}
		return null;
	}

	public static JSONArray Select_Select_Prepare_Goods(Connection conn, String cSpuer_No, String dDate) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement(
					"select b.fQuantity,b.cUnit,b.cGoodsNo,b.cGoodsName,b.cSpec,b.Image_Path,b.price,b.Head_affirm_cSheetno from Head_affirm a ,Head_affirm_Detail b where a.Head_affirm_cSheetno=b.Head_affirm_cSheetno and a.cSpuer_No=? and  a.dDate=? and a.Head_affirm_State= '1' ");
			past.setString(1, cSpuer_No);
			past.setString(2, dDate);
			rs = past.executeQuery();
			JSONArray list = new JSONArray();
			JSONObject obj = new JSONObject();
			while (rs.next()) {
				String fQuantity = rs.getString("fQuantity");
				String cUnit = rs.getString("cUnit");
				String cGoodsNo = rs.getString("cGoodsNo");
				String cGoodsName = rs.getString("cGoodsName");
				String cSpec = rs.getString("cSpec");
				String Image_Path = rs.getString("Image_Path");
				String price = rs.getString("price");
				String Head_affirm_cSheetno = rs.getString("Head_affirm_cSheetno");

				obj = new JSONObject();
				obj.put("cGoodsName", cGoodsName);
				obj.put("cUnit", cUnit);
				obj.put("fQuantity", fQuantity);
				obj.put("cGoodsNo", cGoodsNo);
				obj.put("cSpec", cSpec);
				obj.put("Image_Path", Image_Path);
				obj.put("price", price);

				PreparedStatement past1 = conn.prepareStatement(
						"select * from Head_affirm_Store_Goods_Detail where Head_affirm_cSheetno=? and cGoodsNo=? ");
				past1.setString(1, Head_affirm_cSheetno);
				past1.setString(2, cGoodsNo);
				ResultSet rs1 = past1.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs1);
				obj.put("storeList", array);
				list.put(obj);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, past, null, conn);
		}
		return null;
	}

	// 確認分揀
	// 查询司机位置
	public static JSONArray Select_driver_the_adress(Connection conn, String cSheetNo) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement(
					"select a.longitude, a.latitude,a.Driver, a.DriverNo ,a.TruckLicenseTag,a.tel from Posmanagement_main.dbo.t_Driver  a,Driver_Receiver_Order b where a.DriverNo=b.DriverNo and b.Head_affirm_cSheetno=?");
			past.setString(1, cSheetNo);
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

	public static JSONArray Select_driver_float(Connection conn, String cSheetNo) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement(
					"select a.longitude, a.latitude from Posmanagement_main.dbo.t_Driver  a,Driver_Receiver_Order b where a.DriverNo=b.DriverNo and b.Head_affirm_cSheetno=?");
			past.setString(1, cSheetNo);
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

	public static int update_Driver_address(Connection conn, String longitude, String latitude, String DriverNo) {
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement(
					"update Posmanagement_main.dbo.t_Driver set longitude =?,latitude=?  where DriverNo=? ");
			past.setString(1, longitude);
			past.setString(2, latitude);
			past.setString(3, DriverNo);
			int a = past.executeUpdate();
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
		Gson gson = new Gson();
		// select_Customer_Order(Connection conn, String cSupNo, String dDate)
		// String sre = gson.toJson(select_Customer_Order();
		// LoggerUtil.info(
		// new
		// Gson().toJson(select_head_request_order(GetConnection.getStoreConn(),
		// "1117", "2017-03-12")));
		// LoggerUtil.info(new
		// Gson().toJson(select_Customer_Order(GetConnection.getBiddingConn(),
		// "1001", "2017-03-12")));
		// LoggerUtil.info(new
		// Gson().toJson(Select_Customer_OrderContent(GetConnection.getBiddingConn(),
		// "1")));
		// LoggerUtil.info(sre);
		// LoggerUtil.info(Select_driver_the_adress(GetConnection.getBiddingConn(),
		// "1"));
		// LoggerUtil.info(new
		// Gson().toJson(select_head_request_order(GetConnection.getStoreConn(),
		// "1117","2017-03-12")));
		// LoggerUtil.info(
		// new
		// Gson().toJson(select_Select_Bidding_Order(GetConnection.getBiddingConn(),
		// "1117", "2017-04-18")));
		LoggerUtil.info(Select_Select_Prepare_cSheetno(GetConnection.getBiddingConn(), "1117", "2017-04-19"));
	}

}
