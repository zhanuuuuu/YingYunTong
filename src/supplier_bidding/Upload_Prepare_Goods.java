package supplier_bidding;
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

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;

@WebServlet(name = "Upload_Prepare_Goods", description = "备货", urlPatterns = { "/Upload_Prepare_Goods" })
public class Upload_Prepare_Goods extends HttpServlet {
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
		String cSheetNo = request.getParameter("cSheetNo");
		String iSsort = request.getParameter("iSsort");
		LoggerUtil.info(cSheetNo);
		LoggerUtil.info(iSsort);
		PreparedStatement past = null;
		ResultSet rs=null;
		try {
			past = conn.prepareStatement("update Head_affirm set Head_affirm_State ='1',iSsort=? where Head_affirm_cSheetno=? ");
			past.setString(1, iSsort);
			past.setString(2, cSheetNo);
			int a = past.executeUpdate();
			if (a == 1) {
				// 推送给营运通来收货
				// JPushClientExample.testSend("赶紧来收货","我把货备好了");
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + 1 + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + 0 + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + 0 + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + 0 + "}");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		out.flush();
		out.close();
	}
}
