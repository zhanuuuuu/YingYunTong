package Offline_Pos;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import ModelRas.MD5key;
import Tool.ResultSet_To_JSON;

/**
 * Servlet implementation class p_Qry_card_activity
 */
@WebServlet(description = "积分活动返现查询", urlPatterns = { "/p_Qry_card_activity" })
public class p_Qry_card_activity extends HttpServlet {
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
		Connection conn =null;
		CallableStatement c = null;
		ResultSet rs = null;
		String sign = request.getParameter("sign");
		JSONArray array=new JSONArray();
		if (MD5key.getMD5Pass("ware13391810430").equals(sign)) {
			try {
				conn=GetConnection.getStoreConn();
				c = conn.prepareCall("{call p_Qry_card_activity ()}");
				rs = c.executeQuery();
				array = ResultSet_To_JSON.resultSetToJsonArray(rs);
				
				if (array != null && array.length() > 0) {
					out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dData\":" + array.toString() + "}");
					LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dData\":" + array.toString() + "}");
				} else {
					out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dData\":" + array.toString() + "}");
					LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"," + "\"dData\":" + array.toString() + "}");
				}
			} catch (Exception e) {
				out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dData\":" + array.toString() + "}");
				e.printStackTrace();
			} finally {
				LoggerUtil.info("关闭连接");
				DB.closeRs_Con(rs, c, conn);
			}
		} else {
			out.print("{\"resultStatus\":\"" + 2 + "\"," + "\"dData\":\"" + "签名出错" + "\"}");
		}
		out.flush();
		out.close();
	}
}
