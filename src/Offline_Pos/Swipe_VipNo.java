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
import DB.DB;
import DB.GetConnection;
import ModelRas.MD5key;
import Tool.ResultSet_To_JSON;

@WebServlet(description = "扫会员卡", urlPatterns = { "/Swipe_VipNo" })
public class Swipe_VipNo extends HttpServlet {
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
		Connection conn = null;
		CallableStatement c = null;
		ResultSet rs = null;
		String vipNo = request.getParameter("vipNo");// 9009
		String sign = request.getParameter("sign");
		System.out.println(vipNo);
		System.out.println(sign);
		if (MD5key.getMD5Pass(vipNo + "ware").equals(sign)) {
			try {
				conn = GetConnection.getStoreConn();
				c = conn.prepareCall("{call p_GetVip_PFrate_byScore (?)}");
				c.setString(1, vipNo);
				rs = c.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
				if (array != null && array.length() > 0) {
					out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dData\":" + array.toString() + "}");
					System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dData\":" + array.toString() + "}");
				} else {
					out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dData\":" + array.toString() + "}");
					System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"dData\":" + array.toString() + "}");
				}
			} catch (Exception e) {
				out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dData\":\"" + "调用出错" + "\"}");
				e.printStackTrace();
			} finally {
				System.out.println("关闭连接");
				DB.closeRs_Con(rs, c, conn);
			}
		} else {
			out.print("{\"resultStatus\":\"" + 2 + "\"," + "\"dData\":\"" + "签名出错" + "\"}");
			System.out.println("{\"resultStatus\":\"" + 2 + "\"," + "\"dData\":\"" + "签名出错" + "\"}");
		}
		out.flush();
		out.close();
	}
}
