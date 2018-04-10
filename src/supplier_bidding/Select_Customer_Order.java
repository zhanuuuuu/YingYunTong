package supplier_bidding;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB_Supplier_Bidding;
import DB.GetConnection;
import Tool.String_Tool;

@WebServlet(description = "查询客户订单", urlPatterns = { "/Select_Customer_Order" })
public class Select_Customer_Order extends HttpServlet {
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
		try {
			String cSupNo = request.getParameter("cSupNo");
			String dDate = request.getParameter("dDate");
			String Head_cStoreNo = request.getParameter("Head_cStoreNo");
			LoggerUtil.info(cSupNo);
			LoggerUtil.info(dDate);
			LoggerUtil.info(Head_cStoreNo);
			String str = "";
			String resultStatus = "0";
			if (String_Tool.isEmpty(cSupNo)) { // 司机查看 Head_cStoreNo, dDate
				JSONArray array = DB_Supplier_Bidding.Driver_Select_Order(GetConnection.getBiddingConn(), dDate,Head_cStoreNo);
				if (array != null && array.length() > 0) {
					str = array.toString();
					resultStatus = "1";
				}
			} else { // 供应商查看需要,为备货
				JSONArray array = DB_Supplier_Bidding.select_Customer_Order(GetConnection.getBiddingConn(), cSupNo,
						dDate);
				if (array != null && array.length() > 0) {
					str = array.toString();
					resultStatus = "1";
				}
			}
			if (resultStatus.equals("1")) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + str + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + str + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + str + "\"}");
				LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + str + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
