package Head_shop;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import Tool.ReadConfig;
import Tool.String_Tool;

@WebServlet(description = "–ﬁ∏ƒ≈Ãµ„≤Ó“Ï", urlPatterns = { "/Update_pan_dian_cha_yi" })
public class Update_pan_dian_cha_yi extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String data= request.getParameter("data");
		Connection conn=null;
	    PreparedStatement past=null;
		try {
			JSONArray array=new JSONArray(data);
			conn=GetConnection.getStoreConn();
			conn.setAutoCommit(false);
			past=conn.prepareStatement("UPDATE b SET b.fQuantity=? from wh_CheckWh a,wh_CheckWhDetail b  where a.cSheetno=b.cSheetno AND a.cSupplierNo = ? AND a.cSheetno=? AND b.cGoodsNo=? AND b.iLineNo=?");
			for(int i=0;i<array.length();i++){
				JSONObject obj=array.getJSONObject(i);
				past.setString(1, obj.getString("fQuantity_Now"));
				past.setString(2, obj.getString("cSupplierNo"));
				past.setString(3, obj.getString("cSheetno"));
				past.setString(4, obj.getString("cGoodsNo"));
				past.setString(5, obj.getString("iLineNo"));
				past.addBatch();
				if(i%200==0){
					past.executeBatch();
				}
			}
			past.executeBatch();
			if(array!=null&&array.length()>0){
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" +array.toString() + "}");
			}else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" +array.toString() + "}");
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
