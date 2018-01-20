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

	// �õ��ܲ������ݿ������
	public static Connection getStoreConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("IP"),new ReadConfig().getprop().getProperty("DataSource")); // ��ʼ�������̻������ݿ�����
			return conn;
		} catch (Exception e) {
			logger.error("Posmanagement_main�����쳣");
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getPosstationConn(String ip) {
		try {
			Connection conn = DB.getConnection(ip, "posstation"); //
			return conn;
		} catch (Exception e) {
			logger.error("posstation�����쳣");
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getonlineConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("Simple_online_IP"),
					new ReadConfig().getprop().getProperty("Simple_onlineDataSource")); // ��ʼ�������̻������ݿ�����
			return conn;
		} catch (Exception e) {
			logger.error("Simple_online�����쳣");
			e.printStackTrace();
		}
		return null;
	}

	// �õ��̼����ݿ������ (store) �õ��̼��µķֵ�����ݿ������
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
				Connection conn = DB.getConnection(cStoreIP+":1433",cDataBase,cDataBasePassword); // ��ʼ�������̻������ݿ�����
				return conn;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("����ŵ�"+storeno+"���ݿ��쳣�����쳣");
			e.printStackTrace();
		}
		finally {
			DB.closeRs_Con(rs, past, conn1);
		}
		return null;
	}

	// �õ�supermarket���ݿ������
	public static Connection getSupermarketConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("Supermarket_IP"),
					new ReadConfig().getprop().getProperty("SupermarketDataSource")); // ��ʼ�������̻������ݿ�����
			return conn;
		} catch (Exception e) {
			logger.error("Supermarket�쳣");
			e.printStackTrace();
		}
		return null;
	}

	// �õ�PS_OSS���ݿ������
	public static Connection getPS_OSSConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("PS_OSS_IP"),
					new ReadConfig().getprop().getProperty("PS_OSSDataSource")); // ��ʼ�������̻������ݿ�����
			return conn;
		} catch (Exception e) {
			logger.error("����̼��µķֵ�����ݿ�Connection�쳣");
			e.printStackTrace();
		}
		return null;
	}

	
	public static Connection getBiddingConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("Bidding_IP"),
					new ReadConfig().getprop().getProperty("BiddingDataSource")); // ��ʼ�������̻������ݿ�����
			return conn;
		} catch (Exception e) {
			logger.error("����̼��µķֵ�����ݿ�Connection�쳣");
			e.printStackTrace();
		}
		return null;
	}
	
	public static Connection getPos_SaleConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("Pos_Sale_IP"),
					new ReadConfig().getprop().getProperty("Pos_SaleDataSource")); // ��ʼ�������̻������ݿ�����
			return conn;
		} catch (Exception e) {
			logger.error("����̼��µķֵ�����ݿ�Connection�쳣");
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getWalletConn() {
		try {
			Connection conn = DB.getConnection(new ReadConfig().getprop().getProperty("PS_Wallet_IP"),
					new ReadConfig().getprop().getProperty("PS_WalletDataSource")); // ��ʼ�������̻������ݿ�����
			return conn;
		} catch (Exception e) {
			logger.error("PS_Wallet�����쳣");
			e.printStackTrace();
		}
		return null;
	}

}
