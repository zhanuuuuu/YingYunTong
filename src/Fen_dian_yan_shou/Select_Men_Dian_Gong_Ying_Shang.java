package Fen_dian_yan_shou;

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

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

/**
 * Servlet implementation class Select_Men_Dian_Gong_Ying_Shang
 */
@WebServlet(description = "查询门店供应商", urlPatterns = { "/Select_Men_Dian_Gong_Ying_Shang" })
public class Select_Men_Dian_Gong_Ying_Shang extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String cStoreNo = request.getParameter("cStoreNo");
		System.out.println(cStoreNo);
		Connection conn=GetConnection.getStoreConn();
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			c = conn.prepareCall("{call  p_GetStoreSuperList(?)}");
			c.setString(1, cStoreNo);
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			System.out.println(array);
			out.write(String_Tool.get_output_str(array));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, c, conn);
		}
	}
}
