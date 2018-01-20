package DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ModelRas.MD5key;
import Tool.GetLog;
import Tool.ReadConfig;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;
import bean.Order_Details;
import bean.Request_Order_Json;
import bean.User_Address;

public class DB {

	public static void main(String args[]) throws Exception {
		// Select_(GetConnection.getStoreConn());

//		String a = "1234567890123";
//		a = a.substring(1 - 1, 2);
//		System.out.println(a);
//		String fQuantity = "0344";
//
//		Double Kg = (double) (Integer.parseInt(fQuantity) / 1000);// 由g改成kg
//		System.out.println(Kg );
//		System.out.println(0344/1000);
		Select_(GetConnection.getPos_SaleConn()) ;

	}

	public static String Select_(Connection conn) { // 测试方法
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			PreparedStatement past = conn.prepareStatement(" select top 10 *  from dbo.t_SaleSheetDetail");
			rs = past.executeQuery();
			String str = ResultSet_To_JSON.resultSetTostr(rs);
			// JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			System.out.println(str);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(c, conn);
		}
		return null;
	}

	public static String Yi_Pei_Song_select_Order(Connection conn, String Pay_state, String cStoreNo, String start,
			String end) {
		// 付款状态，0是待付款，1是待发货，2是待收货，3是完成,4是所有
		PreparedStatement past = null;
		ResultSet rs = null;
		Gson gson = new GsonBuilder().create();
		List<Request_Order_Json> list = new ArrayList<Request_Order_Json>();
		try {
			String sql = "select a.*,b.cTel,b.cStorename ,c.Describe,d.cOperatorNo,d.cOperator,d.dDate  from Simple_online.dbo.Order_Table a,posmanagement_main.dbo.t_Store b,Simple_online.dbo.Pay_way_Table c, Simple_online.dbo.Store_Receive_Order_Log d where Pay_state=? and a.cStoreNo=b.cStoreNo and a.Order_State<> 0 and cast(dbo.[getDayStr](a.Date_time) as datetime) between ? and ?  and a.cStoreNo=? and a.Pay_wayId=c.Pay_wayId and a.cSheetno=d.cSheetno "; // Order_State订单状态
			System.out.println(sql);
			past = conn.prepareStatement(sql);
			past.setString(1, Pay_state);
			past.setString(2, start);
			past.setString(3, end);
			past.setString(4, cStoreNo);
			rs = past.executeQuery();
			while (rs.next()) {
				Request_Order_Json request_Order_Json = new Request_Order_Json();
				String cSheetno = rs.getString("cSheetno").replace(" ", "");
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

				PreparedStatement past1 = conn.prepareStatement(
						"select a.cGoodsImagePath,a.Description,b.cGoodsNo,b.cGoodsName,b.Num,b.Last_Price,b.Last_Money from  posmanagement_main.dbo.T_goods a, Simple_online.dbo.Order_Details  b where cSheetno= ? and a.cGoodsNo=b.cGoodsNo ");
				past1.setString(1, cSheetno);
				System.out.println(cSheetno);
				ResultSet rs1 = past1.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs1);

				DB.closeRs_Con(rs1, past1, null);

				List<Order_Details> details_list = gson.fromJson(array.toString(),
						new TypeToken<List<Order_Details>>() {
						}.getType());

				request_Order_Json.setDetails(details_list);
				request_Order_Json.setcSheetno(cSheetno);
				request_Order_Json.setDate_time(date_time);
				request_Order_Json.setAll_Money(all_Money);
				System.out.println(request_Order_Json.getAll_Money());
				request_Order_Json.setPay_wayId(pay_wayId);
				request_Order_Json.setSend_Money(send_Money);
				request_Order_Json.setTotal_money(total_money);
				request_Order_Json.setcTel(cTel);
				request_Order_Json.setcStoreName(cStoreName);
				request_Order_Json.setOrder_State(Order_State);
				request_Order_Json.setDate_time(date_time);
				request_Order_Json.setDescribe(Describe);
				request_Order_Json.setcOperator(cOperator);
				request_Order_Json.setcOperatorNo(cOperatorNo);
				request_Order_Json.setDate_time(dDate);
				PreparedStatement past2 = conn
						.prepareStatement("select * from Simple_online.dbo.User_Address where AddressID= ? ");
				past2.setString(1, AddressID);
				ResultSet rs2 = past2.executeQuery();

				JSONArray address_array = ResultSet_To_JSON.resultSetToJsonArray(rs2);
				DB.closeRs_Con(rs2, past2, null);
				JSONObject obj = address_array.getJSONObject(0);
				User_Address user_address = gson.fromJson(obj.toString(), User_Address.class);
				request_Order_Json.setUser_address(user_address);
				list.add(request_Order_Json);
			}
			String array = gson.toJson(list); // 订单详情
			String str = "";
			if (list.size() > 0) {
				str = "{\"resultStatus\":\"" + 1 + "\"," + "\"array\":" + array.toString().replace(" ", "") + "}";
			} else {
				str = "{\"resultStatus\":\"" + 0 + "\"," + "\"array\":" + array.toString() + "}";
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

	public static JSONArray select(Connection conn) { // 测试方法
		String sql = "select cGoodsNo,cUnitedNo,cGoodsName,cGoodsTypeno,cGoodsTypename,cBarcode,cUnit,cSpec,fNormalPrice,fVipPrice,fPreservationUp,fPreservationDown,fVipScore,bWeight,fCKPrice,cSupNo,cSupName  from dbo.t_Goods";
		try {
			PreparedStatement past = conn.prepareStatement(sql);
			ResultSet rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			System.out.println(array);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Map<String, DataSource> map = new HashMap<String, DataSource>();

	public static void init(String Ip, String DataSourceName) {
		if (String_Tool.isEmpty(Ip) || String_Tool.isEmpty(DataSourceName)) {
			System.out.println("配置文件没有数据");
			return;
		}
		Properties p = new Properties();
		p.setProperty("driverClassName", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		p.setProperty("url", "jdbc:sqlserver://" + Ip + ";databaseName=" + DataSourceName);
		p.setProperty("username", "sa");
		p.setProperty("password", new ReadConfig().getprop().getProperty("PassWord"));
		p.setProperty("maxActive", "30");
		p.setProperty("maxIdle", "100");
		p.setProperty("maxWait", "1000");
		p.setProperty("removeAbandoned", "true"); // 回收
		p.setProperty("removeAbandonedTimeout", "300"); // 回收超时时间
		p.setProperty("testOnBorrow", "true"); // 获取当前连接
		p.setProperty("logAbandoner", "true"); // 返回
		try {
			BasicDataSource dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(p);
			map.put(DataSourceName, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
			GetLog.getLogger(GetLog.class).error("获取连接池异常", e);
		}
	}

	public static void init(String Ip, String DataSourceName, String PassWord) {
		Properties p = new Properties();
		p.setProperty("driverClassName", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		p.setProperty("url", "jdbc:sqlserver://" + Ip + ";databaseName=" + DataSourceName);
		p.setProperty("username", "sa");
		p.setProperty("password", PassWord);
		p.setProperty("maxActive", "30");
		p.setProperty("maxIdle", "100");
		p.setProperty("maxWait", "1000");
		p.setProperty("removeAbandoned", "true"); // 回收
		p.setProperty("removeAbandonedTimeout", "300"); // 回收超时时间
		p.setProperty("testOnBorrow", "true"); // 获取当前连接
		p.setProperty("logAbandoner", "true"); // 返回
		try {
			BasicDataSource dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(p);
			map.put(DataSourceName, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
			GetLog.getLogger(GetLog.class).error("获取连接池异常", e);
		}
	}

	public static Connection getConnection(String Ip, String DataSourceName) throws SQLException {
		BasicDataSource dataSource = (BasicDataSource) map.get(DataSourceName);
		if (dataSource == null) {
			init(Ip, DataSourceName);
			System.out.println("新建连接池" + DataSourceName);
			dataSource = (BasicDataSource) map.get(DataSourceName);
		}
		return dataSource.getConnection();
	}

	public static Connection getConnection(String Ip, String DataSourceName, String PassWord) throws SQLException {
		BasicDataSource dataSource = (BasicDataSource) map.get(DataSourceName);
		if (dataSource == null) {
			init(Ip, DataSourceName, PassWord);
			System.out.println("新建连接池" + DataSourceName);
			dataSource = (BasicDataSource) map.get(DataSourceName);
		}
		return dataSource.getConnection();
	}

	public static void closeResultSet(ResultSet rs) { // 关闭结果集
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void closeCallState(CallableStatement c) { // 关闭存储过程
		try {
			if (c != null) {
				c.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConn(Connection conn) {// 关闭链接
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void past(Statement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	public static void closeRs_Con(CallableStatement c, Connection conn) {
		closeCallState(c);
		closeConn(conn);
	}

	public static void closeRs_Con(ResultSet rs, CallableStatement c, Connection conn) {
		closeResultSet(rs);
		closeRs_Con(c, conn);
	}

	public static void closeRs_Con(ResultSet rs, Statement pstmt, Connection conn) {
		closeResultSet(rs);
		closePreparedStatement(pstmt);
		closeConn(conn);
	}
	public static void closePreparedStatement(Statement pstmt){
		past(pstmt);
	}
	

	public static void closeAll(ResultSet rs, Statement pstmt, CallableStatement c, Connection conn) {
		closeResultSet(rs);
		closePreparedStatement(pstmt);
		closeRs_Con(c, conn);
	}
}
