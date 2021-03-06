package qianxi.zmy;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DB.DB;
import DB.GetConnection;
import Tool.String_Tool;

/**
 * Servlet implementation class driverSureGetRtGoods
 */
@WebServlet(description = "司机确认无误  得到单子  提交单子", urlPatterns = { "/driverSureGetRtGoods" })
public class driverSureGetRtGoods extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public driverSureGetRtGoods() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("content-type", "text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		
		String data=request.getParameter("data");
		Connection conn=null;
		PreparedStatement ps=null;
		PreparedStatement psat=null;
		
		PreparedStatement past_select=null;
		ResultSet past_rs=null;
		
		conn=GetConnection.getStoreConn();
		
		try {
			if(data==null){
				
				out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dDate\":" +null+ "}");
				return;
			}
			JSONArray array=new JSONArray(data);
			JSONObject obj=array.getJSONObject(0);
			conn.setAutoCommit(false);
			
			past_select= conn.prepareStatement(
				" SELECT cSheetno FROM  WH_cStoreReturnGoods "+
				"	WHERE cDriverState=1 and  "+
				"	 cSheetno=?");
			past_select.setString(1, obj.getString("cSheetno"));
			past_rs=past_select.executeQuery();
			if(past_rs.next()){
				DB.closePreparedStatement(past_select);
				conn.commit();
				conn.setAutoCommit(true);
				out.print("{\"resultStatus\":\"" + 2 + "\"," + "\"dDate\":" +null+ "}");  //单子已经被司机扫过了
				return;
			}
			
			DB.closePreparedStatement(past_select);
			
			
			ps= conn.prepareStatement(
					"UPDATE WH_cStoreReturnGoods "+
				"	SET cDriverState=1,cDriverOperaterno=?,cDriverdate=? "+
				"	WHERE cSheetno=?");
			ps.setString(1, obj.getString("cDriverOperaterno"));
			ps.setString(2, String_Tool.DataBaseTime());
			ps.setString(3, obj.getString("cSheetno"));
			ps.execute();
			DB.closePreparedStatement(ps);
			
			psat=conn.prepareStatement("UPDATE WH_cStoreReturnGoodsDetail "+
									"	SET cDriverSurefQuantity=?       "+
									"	WHERE cSheetno=? AND cGoodsNo=?");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				psat.setFloat(1, Float.valueOf(obj1.getString("cDriverSurefQuantity")));
				psat.setString(2, obj1.getString("cSheetno"));
				psat.setString(3, obj1.getString("cGoodsNo"));
				if(i%100==0){
					psat.addBatch();
				}
			}
			psat.executeBatch();
			DB.closePreparedStatement(psat);
			
			
			conn.commit();
			conn.setAutoCommit(true);
			
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + null + "}");
			
		} catch (SQLException e) {
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			out.print("{\"resultStatus\":\"" + -2 + "\"," + "\"dDate\":" + null + "}");
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			out.print("{\"resultStatus\":\"" + -3 + "\"," + "\"dDate\":" + null + "}"); //传过来的数据格式不对
			e.printStackTrace();
		}finally{
			
			DB.closeConn(conn);
			out.flush();
			out.close();
		}
		
		
		
	}

}
