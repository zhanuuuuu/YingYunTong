package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import Tool.ReadConfig;

public class GetConnection {
	static Logger logger = Logger.getLogger(GetConnection.class);
//
	static {
		DB.closeConn(getStoreConn());
		DB.closeConn(getonlineConn());
		DB.closeConn(getSupermarketConn());
		DB.closeConn(getWalletConn());
	}

	// 得到总部的数据库的链接
	public static Connection getStoreConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("IP"),new ReadConfig().getprop().getProperty("DataSource")); // 初始化加盟商户的数据库链接
			return conn;
		} catch (Exception e) {
			logger.error("Posmanagement_main连接异常");
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getPosstationConn(String ip) {
		try {
			Connection conn = DB.getConnection(ip, "posstation"); //
			return conn;
		} catch (Exception e) {
			logger.error("posstation连接异常");
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getonlineConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("Simple_online_IP"),
					new ReadConfig().getprop().getProperty("Simple_onlineDataSource")); // 初始化加盟商户的数据库链接
			return conn;
		} catch (Exception e) {
			logger.error("Simple_online连接异常");
			e.printStackTrace();
		}
		return null;
	}

	// 得到商家数据库的链接 (store) 得到商家下的分店的数据库的链接
	public static Connection getStoreConn(String storeno) {
		Connection conn1 = getStoreConn();
		ResultSet rs=null;
		PreparedStatement past=null;
		try {
		    past = conn1.prepareStatement("select cStoreIP ,cDataBase,cDataBasePassword from t_Store where cStoreNo=?");
			past.setString(1, storeno);
			rs = past.executeQuery();
			if (rs.next()) {
				String cStoreIP = rs.getString("cStoreIP");
				String cDataBase = rs.getString("cDataBase");
				String cDataBasePassword=rs.getString("cDataBasePassword");
				Connection conn = DB.getConnection(cStoreIP+":1433",cDataBase,cDataBasePassword); // 初始化加盟商户的数据库链接
				return conn;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("或得门店"+storeno+"数据库异常连接异常");
			e.printStackTrace();
		}
		finally {
			DB.closeRs_Con(rs, past, conn1);
		}
		return null;
	}

	// 得到supermarket数据库的链接
	public static Connection getSupermarketConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("Supermarket_IP"),
					new ReadConfig().getprop().getProperty("SupermarketDataSource")); // 初始化加盟商户的数据库链接
			return conn;
		} catch (Exception e) {
			logger.error("Supermarket异常");
			e.printStackTrace();
		}
		return null;
	}

	// 得到PS_OSS数据库的链接
	public static Connection getPS_OSSConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("PS_OSS_IP"),
					new ReadConfig().getprop().getProperty("PS_OSSDataSource")); // 初始化加盟商户的数据库链接
			return conn;
		} catch (Exception e) {
			logger.error("或得商家下的分店的数据库Connection异常");
			e.printStackTrace();
		}
		return null;
	}

	
	public static Connection getBiddingConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("Bidding_IP"),
					new ReadConfig().getprop().getProperty("BiddingDataSource")); // 初始化加盟商户的数据库链接
			return conn;
		} catch (Exception e) {
			logger.error("或得商家下的分店的数据库Connection异常");
			e.printStackTrace();
		}
		return null;
	}
	
	public static Connection getPos_SaleConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("Pos_Sale_IP"),
					new ReadConfig().getprop().getProperty("Pos_SaleDataSource")); // 初始化加盟商户的数据库链接
			return conn;
		} catch (Exception e) {
			logger.error("或得商家下的分店的数据库Connection异常");
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getWalletConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("PS_Wallet_IP"),
					new ReadConfig().getprop().getProperty("PS_WalletDataSource")); // 初始化加盟商户的数据库链接
			return conn;
		} catch (Exception e) {
			logger.error("PS_Wallet连接异常");
			e.printStackTrace();
		}
		return null;
	}

}
