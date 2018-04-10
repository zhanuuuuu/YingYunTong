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

@WebServlet(description = "批量淘汰商品", urlPatterns = {"/Batch_Weed_out"})

public class Batch_Weed_out extends HttpServlet {
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
		Connection conn= GetConnection.getStoreConn();
		String data=request.getParameter("data");
		String cStoreNo=request.getParameter("cStoreNo");
		PreparedStatement past = null;
		try {
			conn.setAutoCommit(false);
			JSONArray array=new JSONArray(data);
			past = conn.prepareStatement("update t_cStoreGoods set bDeled ='1' where cGoodsNo=? and cStoreNo=? ");
			for(int i=0;i<array.length();i++){
				JSONObject obj=array.getJSONObject(i);
				past.setString(1, obj.getString("cGoodsNo"));
				past.setString(2, cStoreNo);
				past.addBatch();
			}
			past.executeBatch();
		    conn.commit();
		    conn.setAutoCommit(true);
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + 1 + "}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" +0 + "\"}");
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally {
			DB.closeRs_Con(null, past, conn);
		}
	}
}
