package Head_shop;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DB.DB;
import DB.GetConnection;

@WebServlet(description = "提交箱码关联", urlPatterns = { "/Upload_cGoodsNo_minPackage" })
public class Upload_cGoodsNo_minPackage extends HttpServlet {
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
		String cGoodsNo=request.getParameter("cGoodsNo");
		String cGoodsNo_minPackage=request.getParameter("cGoodsNo_minPackage");
		PreparedStatement past = null;
		try {
			System.out.println(cGoodsNo);
			System.out.println(cGoodsNo_minPackage);
			past = conn.prepareStatement("UPDATE t_Goods set cGoodsNo_minPackage =? where cGoodsNo=?");
			past.setString(1, cGoodsNo);
			past.setString(2, cGoodsNo_minPackage);
		    int a=past.executeUpdate();
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + a + "}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" +0 + "\"}");
			e.printStackTrace();
		}
		finally {
			DB.closeRs_Con(null, past, conn);
		}
	}
}
