package supplier_bidding_head_shop;

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

@WebServlet(description = "总部查询自己的订单", urlPatterns = { "/Head_Select_affirm_Order" })
public class Head_Select_affirm_Order extends HttpServlet {
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
			String dDate = request.getParameter("dDate");
			String Head_cStoreNo = request.getParameter("Head_cStoreNo");
			JSONArray array = DB_Supplier_Bidding.head_Select_affirm_Order(GetConnection.getBiddingConn(),Head_cStoreNo,dDate);
			if (array!=null&&array.length()>0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString()+ "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\","+ "\"dDate\":" + array.toString() + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ 0 + "}");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
