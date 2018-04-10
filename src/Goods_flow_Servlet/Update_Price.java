package Goods_flow_Servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DB.DB;
import DB.GetConnection;

@WebServlet(description = "修改商品价格", urlPatterns = { "/Update_Price" })
public class Update_Price extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String cStoreNo = request.getParameter("cStoreNo");
		String cGoodsNo = request.getParameter("cGoodsNo");
		String fPrice = request.getParameter("fPrice");
		String fQtyUp = request.getParameter("fQtyUp");
		String fQtyDown = request.getParameter("fQtyDown");
		String fQtySoft = request.getParameter("fQtySoft");
		String UserNo = request.getParameter("UserNo");
		String UserName = request.getParameter("UserName");
		String type = request.getParameter("type");
		Connection conn = null;
		CallableStatement c = null;
		try {
			conn = GetConnection.getStoreConn();
			c = conn.prepareCall("{call Update_KG_price(?,?,?,?,?,?,?,?,?)}");
			c.setString(1, cStoreNo);
			c.setString(2, cGoodsNo);
			c.setString(3, fPrice);
			c.setString(4, fQtyUp);
			c.setString(5, fQtyDown);
			c.setString(6, fQtySoft);
			c.setString(7, UserNo);
			c.setString(8, UserName);
			c.setString(9, type);
			c.execute();
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":\"" + "" + "\"}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":\"" + "" + "\"}");
			e.printStackTrace();
		}finally {
			DB.closeRs_Con(null, c, conn);
		}
		out.flush();
		out.close();
	}
}
