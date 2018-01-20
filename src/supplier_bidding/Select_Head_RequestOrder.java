package supplier_bidding;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import DB.DB_Supplier_Bidding;
import DB.GetConnection;
import bean.Head_Request_Order;

@WebServlet(description = "查询总部需求单", urlPatterns = { "/Select_Head_RequestOrder" })
public class Select_Head_RequestOrder extends HttpServlet {
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
			System.out.println(cSupNo);
			System.out.println(dDate);
			List<Head_Request_Order> list = DB_Supplier_Bidding.select_head_request_order(GetConnection.getStoreConn(),cSupNo, dDate);
			if (list != null && list.size() > 0) {
				Gson gson = new Gson();
				String str = gson.toJson(list);
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + str + "}");
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + str + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + "" + "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
