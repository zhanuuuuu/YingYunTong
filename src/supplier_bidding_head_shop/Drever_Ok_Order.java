package supplier_bidding_head_shop;

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

import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;

@WebServlet(description = "司机确认收货", urlPatterns = { "/Drever_Ok_Order" })
public class Drever_Ok_Order extends HttpServlet {
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
		Connection conn = GetConnection.getBiddingConn();
		String data = request.getParameter("data");
		LoggerUtil.info(data);
		PreparedStatement past = null;
		try {
			conn.setAutoCommit(false);
			JSONObject obj = new JSONObject(data);
			String DriverNo = obj.getString("DriverNo");
			String Head_affirm_cSheetno = obj.getString("Head_affirm_cSheetno");

			PreparedStatement past1 = conn
					.prepareStatement("update Head_affirm set Head_affirm_State ='3' where Head_affirm_cSheetno=? ");
			past1.setString(1, Head_affirm_cSheetno);
			int a = past1.executeUpdate();
			if (a == 1) {
				// 推送竞价发通知//发给供货商
				// JPushClientExample.testSend("司机接单","准备迎接我");
				// 推送到服务器
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":\"" + 1 + "\"}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + 0 + "}");
				conn.commit();
				conn.setAutoCommit(true);
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + 0 + "\"}");
				LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + 0 + "}");
			}
			// } else {
			// out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + 0
			// + "\"}");
			// }
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DB.DB.closeAll(null, past, null, conn);
		}
		out.flush();
		out.close();
	}

}
