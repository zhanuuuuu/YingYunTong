package Online_Manager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;

@WebServlet(description = "运费管理", urlPatterns = { "/Freight_management" })
public class Freight_management extends HttpServlet {
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
		Connection conn = GetConnection.getStoreConn();
		PreparedStatement past = null;
		try {
			String data = request.getParameter("data");
			LoggerUtil.info(data);
			JSONObject obj = new JSONObject(data);
			past = conn.prepareStatement("insert into Oeder_freight_Table values(?,?) ");
			past.setString(1, obj.getString("Goods_amount"));
			past.setString(2, obj.getString("Freight"));
			int a = past.executeUpdate();
			if (a == 1) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + "1" + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + "0" + "}");
			}
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
