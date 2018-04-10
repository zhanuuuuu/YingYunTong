package operation;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;
import DB.Operation_update;

@WebServlet(description = "查询会员卡或储值卡信息", urlPatterns = { "/Select_MoneyCar_OrVipCard_Message" })
public class Select_MoneyCar_OrVipCard_Message extends HttpServlet {
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
		String fage = request.getParameter("fage");
		String card = request.getParameter("card");
		LoggerUtil.info(fage);
		LoggerUtil.info(card);
		String sql = "";
		if (fage.equals("1")) { //是查询会员卡
			sql = "select * from dbo.t_Vip where cVipno=? ";
		} else {                 //2是查询储值卡
			sql = "select * from Supermarket.dbo.moneycard where  cardno =? ";
		}
		JSONArray array = Operation_update.Select_MoneyCar_Or_VipCard_Message(GetConnection.getStoreConn(), card, sql);
		if (array != null && array.length() > 0) {
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString().replace(" ", "") + "}");
			LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString().replace(" ", "") + "}");
		} else {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString().replace(" ", "") + "}");
			LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString().replace(" ", "") + "}");
		}
		out.flush();
		out.close();
	}

}
