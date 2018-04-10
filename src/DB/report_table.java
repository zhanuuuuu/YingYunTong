package DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import Tool.ResultSet_To_JSON;

public class report_table {
	
	//������������
	public static JSONArray Select_Horizontal_report_table(Connection conn,String date1,String date2,String storeno) { // ��ѯ���ͳ��ⵥ����Ʒ
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call  p_Account_Receiver(?,?,?,?) }");
			c.setString(1, "");
			c.setString(2, date1);
			c.setString(3, date2);
			c.setString(4, storeno);
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			LoggerUtil.info( array.toString());
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closeRs_Con(c, conn);
		}
		return null;
	}
	
	//��ѯ��Ʒ��������
	public static JSONArray Select_goods_Sale_Details(Connection conn,String CGoodsno,String date1,String date2,String storeno) { // ��ѯ���ͳ��ⵥ����Ʒ
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call  goods_sale_details(?,?,?,?) }");
			c.setString(1, CGoodsno);
			c.setString(2, date1);
			c.setString(3, date2);
			c.setString(4, storeno);
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			LoggerUtil.info( array.toString());
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closeRs_Con(c, conn);
		}
		return null;
	}
	
	public static JSONArray Select_Type_Group_Sales(Connection conn,String  storeno,String date1,String date2,String cGoodsTypeno) { // ��ѯ���ͳ��ⵥ����Ʒ
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call  p_x_salesABC_byGoods_log_TermID_Goods_App(?,?,?,?,?) }");
			c.setString(1,  storeno);
			c.setString(2, date1);
			c.setString(3, date2);
			c.setString(4, "");
			c.setString(5, cGoodsTypeno);
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
	
	//��ѯë������
	public static JSONArray Select_mao_li_report(Connection conn,String cStoreNo, String d1, String d2,String GroupNo,String NoType,String GroupNoMax) { // ��ѯ���ͳ��ⵥ����Ʒ
		CallableStatement c = null;
		ResultSet rs = null;
		try {
//			@cStoreNo varchar(32),
//			@dDate1 datetime,
//			@dDate2 datetime,
//			@cWHno varchar(32),
//			@cTermID varchar(32),
//			@GroupNo varchar(32),
//			@NoType varchar(8),  -- @NoType=1 ��ʾ��һ������ �����ŵ꣬@NoType=2��Χ���飬@NoType=3 ���ز���
//			@GroupMax varchar(8)
			c = conn.prepareCall("{call  p_FIFO_SalesProfit_MultGoods_log_TermID_App_03(?,?,?,?,?,?,?,?) }");
			c.setString(1, cStoreNo);
			c.setString(2, d1);
			c.setString(3, d2);
			c.setString(4, "");
			c.setString(5, "");
			c.setString(6, GroupNo);
			c.setString(7, NoType);
			c.setString(8, GroupNoMax);
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
	//�ŵ���������
	public static JSONArray Select_mendianshouyin_report(Connection conn, String d1, String d2,String cStoreNo) { // ��ѯ���ͳ��ⵥ����Ʒ
		CallableStatement c = null;
		ResultSet rs = null;
		CallableStatement c1 = null;
		try {
			c = conn.prepareCall("{call  p_Account_Receiver_App(?,?,?,?) }");
			c.setString(1, "");
			c.setString(2, d1);
			c.setString(3, d2);
			c.setString(4, cStoreNo);
			c.execute();
			
			c1 = conn.prepareCall("{call  p_getTestExec(?) }");
			c1.setString(1, "");
			
			rs =c1.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closeCallState(c1);
			DB.closeRs_Con(c, conn);
		}
		return null;
	}
	
	//���ؼ�ͳ��
	public static JSONArray Select_Special_price(Connection conn,String cStoreNo, String d1, String d2) { // ��ѯ���ͳ��ⵥ����Ʒ
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call  p_x_salesABC_byGoods_log_TermID_App (?,?,?,?,?) }");
			c.setString(1, cStoreNo);
			c.setString(2, d1);
			c.setString(3, d2);
			c.setString(4, "");
			c.setString(5, "");
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
}
