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
import Tool.String_Tool;

public class Select_Online_Manager {

	public static JSONArray getBg(Connection conn, String cParentNo) {
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement("select * from dbo.T_GroupType where cParentNo=? ");
			past.setString(1, cParentNo);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		return null;
	}

	public static JSONArray getGoods(Connection conn, String cParentNo, String cStoreNo) {
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement(
					"select c.cGoodsName,c.cBarcode,c.cGoodsNo,c.bOnLine,c.bOnLine_Price,c.fVipPrice,c.fNormalPrice,d.cGoodsImagePath from T_GroupType_GoodsType  a,  dbo.T_GroupType  b , t_cStoreGoods c ,t_goods d where a.cGroupTypeNo=b.cGroupTypeNo and a.cGoodsTypeNo=c.cGoodsTypeNo and a.cGroupTypeNo=? and cStoreNo=? and c.cGoodsNo=d.cGoodsNo");
			past.setString(1, cParentNo);
			past.setString(2, cStoreNo);
			;
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		return null;
	}

	public static JSONArray Fresh_items_summary(Connection conn, String start_time, String end_time) {
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			String sql = "select b.cGoodsNo,b.cGoodsName,fQuantity=sum(b.Num) from dbo.Order_Table a, dbo.Order_Details b where  a.cSheetno=b.cSheetno  and a.Pay_state='1'  and a.Date_time between '"
					+ start_time + "' and '" + end_time + "'  group by cGoodsNo,cGoodsName ";
			past = conn.prepareStatement(sql);
			System.out.println(sql);
			rs = past.executeQuery();
			JSONArray array = new JSONArray();
			JSONObject obj = new JSONObject();
			while (rs.next()) {
				obj = new JSONObject();
				String cGoodsNo = rs.getString("cGoodsNo");
				String cGoodsName = rs.getString("cGoodsName");
				String fQuantity = rs.getString("fQuantity");
				obj.put("cGoodsNo", cGoodsNo);
				obj.put("cGoodsName", cGoodsName);
				obj.put("fQuantity", fQuantity);
				CallableStatement c = conn.prepareCall("{call Select_Goods_summary (?,?,?) }");
				c.setString(1, start_time);
				c.setString(2, end_time);
				c.setString(3, cGoodsNo);
				ResultSet rs1 = c.executeQuery();
				JSONArray array1 = ResultSet_To_JSON.resultSetToJsonArray(rs1);
				obj.put("storeList", array1);
				array.put(obj);
			}
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		return null;
	}

	public static boolean Update_Goods(Connection conn, JSONArray array) {
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement("update t_cStoreGoods set bOnLine =?,bOnLine_Price=?  where cStoreNo=? and cGoodsNo=? ");
			PreparedStatement past1 = conn.prepareStatement("insert into  Update_online_Price_log (bOnLine,bOnLine_Price,cStoreNo,cGoodsNo,UserNo,UserName,dDate)values(?,?,?,?,?,?,?)");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				past.setString(1, obj.getString("bOnLine"));
				past.setString(2, obj.getString("bOnLine_Price"));
				past.setString(3, obj.getString("cStoreNo"));
				past.setString(4, obj.getString("cGoodsNo"));
				past.addBatch();

				past1.setString(1, obj.getString("bOnLine"));
				past1.setString(2, obj.getString("bOnLine_Price"));
				past1.setString(3, obj.getString("cStoreNo"));
				past1.setString(4, obj.getString("cGoodsNo"));
				past1.setString(5, obj.getString("cOperatorNo"));
				past1.setString(6, obj.getString("cOperator"));
				past1.setString(7, String_Tool.DataBaseTime());
				past1.addBatch();
				if (i % 100 == 0) {
					past.executeBatch();
					past1.executeBatch();
				}
			}
			past1.executeBatch();
			past.executeBatch();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, past, null, conn);
		}
		return false;
	}

	public static String select_Order(Connection conn, String Pay_state, String cStoreNo, String start, String end) {
		// 付款状态，0是待付款，1是待发货，2是待收货，3是完成,4是所有
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "";
			if (String_Tool.isEmpty(cStoreNo)) { // 如果店铺是null的话就查询所有
				sql = "select a.*,b.cTel,b.cStorename ,c.Describe  from Simple_online.dbo.Order_Table a,posmanagement_main.dbo.t_Store b,Simple_online.dbo.Pay_way_Table c where Pay_state=? and a.cStoreNo=b.cStoreNo and a.Order_State<> 0 and Convert (varchar(10),Date_time,20) between ? and ?  and a.Pay_wayId=c.Pay_wayId"; // Order_State订单状态，0是不可用，1是可用，2是已经配送
				System.out.println(sql);
				past = conn.prepareStatement(sql);
				past.setString(1, Pay_state);
				past.setString(2, start);
				past.setString(3, end);
			} else {
				sql = "select a.*,b.cTel,b.cStorename ,c.Describe  from Simple_online.dbo.Order_Table a,posmanagement_main.dbo.t_Store b,Simple_online.dbo.Pay_way_Table c where Pay_state=? and a.cStoreNo=b.cStoreNo and a.Order_State<> 0 and Convert (varchar(10),Date_time,20) between ? and ?  and a.Send_cStoreNo=? and a.Pay_wayId=c.Pay_wayId"; // Order_State订单状态，0是不可用，1是可用，2是已经配送
				System.out.println(sql);
				past = conn.prepareStatement(sql);
				past.setString(1, Pay_state);
				past.setString(2, start);
				past.setString(3, end);
				past.setString(4, cStoreNo);
			}

			rs = past.executeQuery();
			JSONArray list = new JSONArray();
			while (rs.next()) {
				JSONObject request_Order_Json = new JSONObject();
				String cSheetno = rs.getString("cSheetno").replace(" ", "");
				String AddressID = rs.getString("AddressID").replace(" ", "");
				String date_time = rs.getString("Date_time").replace(" ", "");
				String send_Money = rs.getString("Send_Money").replace(" ", "");
				String total_money = rs.getString("Total_money").replace(" ", "");
				String all_Money = rs.getString("All_Money").replace(" ", "");
				String pay_wayId = rs.getString("Pay_wayId").replace(" ", "");
				String cTel = rs.getString("cTel").replace(" ", "");
				String cStoreName = rs.getString("cStoreName").replace(" ", "");
				String Order_State = rs.getString("Order_State").replace(" ", "");
				String Describe = rs.getString("Describe").replace(" ", "");
				String Send_Way = rs.getString("Send_Way").replace(" ", "");
				String UserNo = rs.getString("UserNo");

				PreparedStatement past1 = conn.prepareStatement(
						"select a.cGoodsImagePath,a.Description,b.cGoodsNo,b.cGoodsName,b.Num,b.Last_Price,b.Last_Money,b.RealityNum,b.Reality_Money from  posmanagement_main.dbo.T_goods a, Simple_online.dbo.Order_Details  b where cSheetno= ? and a.cGoodsNo=b.cGoodsNo ");
				past1.setString(1, cSheetno);
				ResultSet rs1 = past1.executeQuery();
				JSONArray details_list = ResultSet_To_JSON.resultSetToJsonArray(rs1);

				request_Order_Json.put("details_list", details_list);

				request_Order_Json.put("cSheetno", cSheetno);
				request_Order_Json.put("Date_time", date_time);
				request_Order_Json.put("Order_State", Order_State);

				request_Order_Json.put("Pay_wayId", pay_wayId);
				request_Order_Json.put("Send_Money", send_Money);
				request_Order_Json.put("Total_money", total_money);
				request_Order_Json.put("All_Money", all_Money);
				request_Order_Json.put("cTel", cTel);
				request_Order_Json.put("cStoreName", cStoreName);
				request_Order_Json.put("Order_State", Order_State);
				request_Order_Json.put("dDate", date_time);
				request_Order_Json.put("Describe", Describe);
				request_Order_Json.put("UserNo", UserNo);
				request_Order_Json.put("Send_Way", Send_Way);
				String sql1 = "";
				System.out.println(Send_Way);
				if (Send_Way.equals("2")) {
					sql1 = "select * from Simple_online.dbo.User_Address where AddressID= ?";
				} else if (Send_Way.equals("1")) {
					sql1 = "select * from Simple_online.dbo.Store_address_site where id= ? ";
				}
				System.out.println(sql1);
				PreparedStatement past2 = conn.prepareStatement(sql1);
				past2.setString(1, AddressID);
				ResultSet rs2 = past2.executeQuery();

				JSONObject address_obj = ResultSet_To_JSON.resultSetToJsonObject(rs2);
				DB.closeRs_Con(rs2, past2, null);

				request_Order_Json.put("user_address", address_obj);
				list.put(request_Order_Json);
			}
			String str = "";
			if (list != null && list.length() > 0) {
				str = "{\"resultStatus\":\"" + 1 + "\"," + "\"array\":" + list.toString().replace(" ", "") + "}";
			} else {
				str = "{\"resultStatus\":\"" + 0 + "\"," + "\"array\":" + list.toString() + "}";
			}
			System.out.println(str);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		return null;
	}

	public static String Yi_Pei_Song_select_Order(Connection conn, String Pay_state, String cStoreNo, String start,
			String end) {
		// 付款状态，0是待付款，1是待发货，2是待收货，3是完成,4是所有
		PreparedStatement past = null;
		ResultSet rs = null;
		JSONArray list = new JSONArray();
		try {
			String sql = "select a.*,b.cTel,b.cStorename ,c.Describe,d.cOperatorNo,d.cOperator,d.dDate  from Simple_online.dbo.Order_Table a,posmanagement_main.dbo.t_Store b,Simple_online.dbo.Pay_way_Table c, Simple_online.dbo.Store_Receive_Order_Log d where (Pay_state=? or Pay_state='3') and a.cStoreNo=b.cStoreNo and a.Order_State<> 0 and cast(dbo.[getDayStr](a.Date_time) as datetime) between ? and ?  and a.cStoreNo=? and a.Pay_wayId=c.Pay_wayId and a.cSheetno=d.cSheetno "; // Order_State订单状态
			System.out.println(sql);
			past = conn.prepareStatement(sql);
			past.setString(1, Pay_state);
			past.setString(2, start);
			past.setString(3, end);
			past.setString(4, cStoreNo);
			rs = past.executeQuery();
			while (rs.next()) {
				JSONObject request_Order_Json = new JSONObject();
				String cSheetno = rs.getString("cSheetno").replace(" ", "");
				String UserNo = rs.getString("UserNo");
				String AddressID = rs.getString("AddressID");
				String date_time = rs.getString("Date_time");
				String send_Money = rs.getString("Send_Money");
				String total_money = rs.getString("Total_money");
				String all_Money = rs.getString("All_Money");
				String pay_wayId = rs.getString("Pay_wayId");
				String cTel = rs.getString("cTel");
				String cStoreName = rs.getString("cStoreName");
				String Order_State = rs.getString("Order_State");
				String Describe = rs.getString("Describe");
				String cOperatorNo = rs.getString("cOperatorNo");
				String cOperator = rs.getString("cOperator");
				String dDate = rs.getString("dDate");
				String Send_cStoreNo = rs.getString("Send_cStoreNo");
				String Reality_All_Money = rs.getString("Reality_All_Money");
				String Send_Way = rs.getString("Send_Way").replace(" ", "");

				PreparedStatement past1 = conn.prepareStatement(
						"select a.cGoodsImagePath,a.Description,b.cGoodsNo,b.cGoodsName,b.Num, b.Last_Price,b.Last_Money,b.RealityNum,b.Reality_Money,b.Reality_Money  from  posmanagement_main.dbo.T_goods a, Simple_online.dbo.Order_Details  b where cSheetno= ? and a.cGoodsNo=b.cGoodsNo ");
				past1.setString(1, cSheetno);
				System.out.println(cSheetno);
				ResultSet rs1 = past1.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs1);
				DB.closeRs_Con(rs1, past1, null);

				request_Order_Json.put("details_list", array);
				request_Order_Json.put("cSheetno", cSheetno);
				request_Order_Json.put("Date_time", date_time);
				request_Order_Json.put("All_Money", all_Money);
				request_Order_Json.put("Send_Money", send_Money);
				request_Order_Json.put("Total_money", total_money);
				request_Order_Json.put("cTel", cTel);
				request_Order_Json.put("cStoreName", cStoreName);
				request_Order_Json.put("Describe", Describe);
				request_Order_Json.put("cOperatorNo", cOperatorNo);
				request_Order_Json.put("cOperator", cOperator);
				request_Order_Json.put("dDate", dDate);
				request_Order_Json.put("Send_Way", Send_Way);
				request_Order_Json.put("Send_cStoreNo", Send_cStoreNo);
				request_Order_Json.put("Reality_All_Money", Reality_All_Money);
				request_Order_Json.put("UserNo", UserNo);
				String sql1 = "";
				if (Send_Way.equals("1")) {
					sql1 = " select * from  Simple_online.dbo.Store_address_site where id= ? ";
				} else if (Send_Way.equals("2")) {
					sql1 = "select * from Simple_online.dbo.User_Address where AddressID= ? ";
				}
				PreparedStatement past2 = conn.prepareStatement(sql1);
				past2.setString(1, AddressID);
				ResultSet rs2 = past2.executeQuery();

				JSONObject address_array = ResultSet_To_JSON.resultSetToJsonObject(rs2);
				DB.closeRs_Con(rs2, past2, null);

				request_Order_Json.put("user_address", address_array);
				list.put(request_Order_Json);
			}
			String str = "";
			if (list != null && list.length() > 0) {
				str = "{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + list.toString().replace(" ", "") + "}";
			} else {
				str = "{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + list.toString() + "}";
			}
			System.out.println(str);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		return null;
	}

	public static int cStore_Address(Connection conn, String cStoreNo, String cStoreName, String Address, String Tel,
			String fLont, String fLant, String province, String city, String district, String street, String beizhu1,
			String beizhu2, String cOperatorNo, String cOperator) {
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			String sql = "insert into Simple_online.dbo.Store_address_site (cStoreNo,cStoreName,Address,Tel,fLont,fLant,province,city,district,street,beizhu1,beizhu2,cOperatorNo,cOperator,Available)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			past = conn.prepareStatement(sql);
			past.setString(1, cStoreNo);
			past.setString(2, cStoreName);
			past.setString(3, Address);
			past.setString(4, Tel);
			past.setString(5, fLont);
			past.setString(6, fLant);
			past.setString(7, province);
			past.setString(8, city);
			past.setString(9, district);
			past.setString(10, street);
			past.setString(11, beizhu1);
			past.setString(12, beizhu2);
			past.setString(13, cOperatorNo);
			past.setString(14, cOperator);
			past.setString(15, "1");
			int a = past.executeUpdate();
			PreparedStatement past1 = conn
					.prepareStatement("update Posmanagement_main.dbo.t_Store set longitude =? ,latitude=? ");
			past1.setString(1, fLont);
			past1.setString(2, fLant);
			past1.execute();
			DB.closePreparedStatement(past1);
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, past, null, conn);
		}
		return 0;
	}

	public static int Update_cStore_Address(Connection conn, String id, String cStoreNo, String cStoreName,
			String Address, String Tel, String fLont, String fLant, String province, String city, String district,
			String street, String beizhu1, String beizhu2, String cOperatorNo, String cOperator) {
		ResultSet rs = null;
		PreparedStatement past = null;
		try {

			PreparedStatement past2 = conn
					.prepareStatement("delete from Simple_online.dbo.Store_address_site where id= ?");
			past2.setString(1, id);
			past2.executeUpdate();
			DB.closePreparedStatement(past2);

			String sql = "insert into Simple_online.dbo.Store_address_site (cStoreNo,cStoreName,Address,Tel,fLont,fLant,province,city,district,street,beizhu1,beizhu2,cOperatorNo,cOperator,Available)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			past = conn.prepareStatement(sql);
			past.setString(1, cStoreNo);
			past.setString(2, cStoreName);
			past.setString(3, Address);
			past.setString(4, Tel);
			past.setString(5, fLont);
			past.setString(6, fLant);
			past.setString(7, province);
			past.setString(8, city);
			past.setString(9, district);
			past.setString(10, street);
			past.setString(11, beizhu1);
			past.setString(12, beizhu2);
			past.setString(13, cOperatorNo);
			past.setString(14, cOperator);
			past.setString(15, "1");
			int a = past.executeUpdate();
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, past, null, conn);
		}
		return 0;
	}

	public static JSONArray select_cStore_address(Connection conn, String cStoreNo) {
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement(
					"select * from Simple_online.dbo.Store_address_site where cStoreNo = ? and Available='1' ");
			past.setString(1, cStoreNo);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		return null; // 异常
	}

	public static boolean updateA_key_online(Connection conn, JSONArray array, String percentage, String cStoreNo) {
		String a = String_Tool.String_IS_Four(Double.parseDouble(percentage) / 100);
		System.out.println(a);
		Statement past = null;
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				String cGroupTypeNo = obj.getString("cGroupTypeNo");

				PreparedStatement past1 = conn
						.prepareStatement("select cGoodsTypeNo from T_GroupType_GoodsType where cGroupTypeNo=?");
				past1.setString(1, cGroupTypeNo);
				ResultSet rs = past1.executeQuery();
				past = conn.createStatement();
				while (rs.next()) {
					String cGoodsTypeNo = rs.getString("cGoodsTypeNo");

					String sql = "Update Posmanagement_main.dbo.t_cStoreGoods set bOnLine='1',bOnLine_Price=fCKPrice*('"
							+ (a) + "')+fCKPrice where  cStoreNo='" + cStoreNo + "' and  cGoodsTypeno='" + cGoodsTypeNo
							+ "'";
					past.addBatch(sql);
					System.out.println(sql);
				}
				past.executeBatch();
				DB.closePreparedStatement(past);
				DB.closeResultSet(rs);
				DB.closePreparedStatement(past1);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeConn(conn);
		}
		return false; // 异常
	}

	public static int wallet_recharge_strategy(Connection conn, String Pay_Money, String excess_Money) {
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement("insert into Wallet_recharge_strategy (Pay_Money,excess_Money) values(?,?)");
			past.setString(1, Pay_Money);
			past.setString(2, excess_Money);
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

	public static int wallet_updaterecharge_strategy(Connection conn, String Pay_Money, String excess_Money,
			String ID) {
		PreparedStatement past = null;
		try {
			past = conn
					.prepareStatement("update Wallet_recharge_strategy set Pay_Money =? ,excess_Money=?  where ID=? ");
			past.setString(1, Pay_Money);
			past.setString(2, excess_Money);
			past.setString(3, ID);
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

	public static int delwallet_recharge_strategy(Connection conn, String ID) {
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement("delete from Wallet_recharge_strategy where ID =? ");
			past.setString(1, ID);
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
}
