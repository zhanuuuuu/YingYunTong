package Head_shop;
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
import org.json.JSONObject;
import DB.DB;
import DB.GetConnection;
import Tool.String_Tool;
@WebServlet(description = "ÅÌµãÐÞ¸Ä", urlPatterns = { "/t_CheckTast_update" })
public class t_CheckTast_update extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String cStoreNo=request.getParameter("cStoreNo");
	
		String data=request.getParameter("data");
		String cCheckTaskName=request.getParameter("cCheckTaskName");
		System.out.println(cStoreNo);
		System.out.println(data);
		Connection conn= GetConnection.getStoreConn();
		try {
			conn.setAutoCommit(false);
			JSONArray array=new JSONArray(data);
			PreparedStatement past=conn.prepareStatement("UPDATE t_CheckTast_GoodsDetail_log  set fQuantity_Check=?  where cCheckTaskNo=? and cStoreNo=? ");
			PreparedStatement past1=conn.prepareStatement("INSERT INTO t_CheckTast_GoodsDetail_CheckLog (cCheckTaskNo,cCheckTask,cGoodsNo,cGoodsName,cBarCode,cUnit,cSpec,fQuantity_Check,fInPrice_Avg,fQuantity_Diff,dCheckTask,cSupplier,cSupplierNo,fNormalPrice,fMoney_Sale_Diff,cWhNo,cStoreNo,cStoreName)values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ");

			for(int i=0;i<array.length();i++){
				JSONObject obj=array.getJSONObject(i);
				past.setString(1, obj.getString("fQuantity"));
				past.setString(2, obj.getString("cCheckTaskNo"));
				past.setString(3, cStoreNo);
				past.addBatch();
				if(i%100==0){
					past.executeBatch();
				}
				past1.setString(1, obj.getString("cCheckTaskNo"));
				past1.setString(2, cCheckTaskName);
				past1.setString(3, obj.getString("cGoodsNo"));
				past1.setString(4, obj.getString("cGoodsName"));
				past1.setString(5, obj.getString("cBarcode"));
				past1.setString(6, "");//obj.getString("cUnit")
				past1.setString(7, "");//obj.getString("cSpec")
				past1.setString(8, obj.getString("fQuantity"));
				past1.setString(9, "");
				past1.setString(10, obj.getString("fQuantity"));
				past1.setString(11, String_Tool.DataBaseTime());//obj.getString("dCheckTask")
				past1.setString(12, "");//obj.getString("cSupplier")
				past1.setString(13,"");//obj.getString("cSupplierNo")
				past1.setString(14, obj.getString("fNormalPrice"));
				past1.setString(15, "");
				past1.setString(16, "");
				past1.setString(17, cStoreNo);
				past1.setString(18, "");
				past1.addBatch();
				if(i%100==0){
					past1.executeBatch();
				}
			}
			past.executeBatch();
			past1.executeBatch();
			DB.closePreparedStatement(past);
			DB.closePreparedStatement(past1);
			conn.commit();
			conn.setAutoCommit(true);
            if(array.length()>0){
            	  out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString().replace(" ", "") + "}");
            }
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally{
			DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}
}
