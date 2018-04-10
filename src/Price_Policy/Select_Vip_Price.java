package Price_Policy;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

public class Select_Vip_Price extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String cGoodsNo = request.getParameter("cGoodsNo");
		String dDateBgn = request.getParameter("dDateBgn");
		String dDateEnd = request.getParameter("dDateEnd");
		String cStoreNo=request.getParameter("cStoreNo");
		Connection conn = GetConnection.getStoreConn();
		ResultSet rs = null;
		PreparedStatement past = null;
		LoggerUtil.info(cGoodsNo);
		LoggerUtil.info(dDateBgn);
		LoggerUtil.info(dDateEnd);
		try {
			past = conn.prepareStatement("select a.dDateBgn,a.dDateEnd,a.cSheetno from  dbo.t_VipcGoodsPrice a,   t_VipcGoodsPriceDetail b where a.cSheetno=b.cSheetno  and b.cGoodsNo=?  and cStoreNo=?  and (? between a.dDateBgn and a.dDateEnd or   ?   between a.dDateBgn and a.dDateEnd )");
			past.setString(1, cGoodsNo);
			past.setString(2, cStoreNo);
			past.setString(3, dDateBgn);
			past.setString(4, dDateEnd);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
						+ array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
						+ array.toString() + "}");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}

}
