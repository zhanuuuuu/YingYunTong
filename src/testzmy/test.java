package testzmy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

public class test {
	public static void main(String[] args) throws SQLException {
		Connection conn =null;
		
		PreparedStatement past = null;
		ResultSet rs = null;
		JSONArray array = null;
		try {	
			conn = GetConnection.getStoreConn();
			conn.setAutoCommit(false);
			
			String sql = "";
			
				sql = "  select top 10 * from t_Goods ";
				past = conn.prepareStatement(sql);
				//past.setString(1, "0001"); //
				rs = past.executeQuery();
				array = ResultSet_To_JSON.resultSetToJsonArray(rs);
				conn.commit();
				conn.setAutoCommit(true);
				System.out.println(array.toString());
				
			
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		
	}

}
