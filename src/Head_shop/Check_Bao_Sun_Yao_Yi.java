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
import org.json.JSONArray;
import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

@WebServlet(description = "审核报损褒义", urlPatterns = { "/Check_Bao_Sun_Yao_Yi" })
public class Check_Bao_Sun_Yao_Yi extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		String cSheetno = request.getParameter("cSheetno");
		String action = request.getParameter("action");
		String get_through = request.getParameter("get_through");
		System.out.println(cSheetno);
		System.out.println(action);
		System.out.println(get_through);
		Connection conn = GetConnection.getStoreConn();
		PreparedStatement past = null;
		try {
			String sql = "";
			if (action.equals("0")) { // 更新报损
				sql = "Update wh_LossWarehouse  set bExamin = ? where  cSheetno= ? ";
				past = conn.prepareStatement(sql);
				past.setString(1, get_through);
			} else if (action.equals("1")) { // 更新报溢
				sql = "Update dbo.wh_EffusionWh  set bExamin = ?  where cSheetno=? ";
				past = conn.prepareStatement(sql);
				past.setString(1, get_through);
			}
			past.setString(2, cSheetno);
			int a = past.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}

}
