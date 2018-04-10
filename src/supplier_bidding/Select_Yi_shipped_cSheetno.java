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


@WebServlet(description = "查询已发货", urlPatterns = { "/Select_Yi_shipped_cSheetno" })
public class Select_Yi_shipped_cSheetno extends HttpServlet {
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
		try {
			String cSupNo = request.getParameter("cSupNo");
			String dDate = request.getParameter("dDate");
			LoggerUtil.info(cSupNo);
			LoggerUtil.info(dDate);
			String str = "";
			JSONArray array = DB_Supplier_Bidding.select_Yishipped_cSheetno(GetConnection.getBiddingConn(), cSupNo, dDate);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + str + "\"}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + str + "}");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
