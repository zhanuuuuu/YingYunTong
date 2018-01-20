package supplier_bidding;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import DB.DB_Supplier_Bidding;
import DB.GetConnection;

@WebServlet(description = "查询司机地址", urlPatterns = { "/Select_driver_the_adress" })
public class Select_driver_the_adress extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String cSheetNo = request.getParameter("cSheetNo");
			System.out.println("GET" + cSheetNo);
			JSONArray array = DB_Supplier_Bidding.Select_driver_float(GetConnection.getBiddingConn(), cSheetNo);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":\"" + array.toString() + "\"}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + "" + "}");
				System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + "" + "\"}");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String cSheetNo = request.getParameter("cSheetNo");
			JSONArray array = DB_Supplier_Bidding.Select_driver_the_adress(GetConnection.getBiddingConn(), cSheetNo);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":\"" + array.toString() + "\"}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + "" + "}");
				System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + "" + "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
