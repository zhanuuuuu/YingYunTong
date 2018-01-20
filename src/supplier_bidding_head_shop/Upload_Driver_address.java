package supplier_bidding_head_shop;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DB.DB_Supplier_Bidding;
import DB.GetConnection;

@WebServlet(description = "�ϴ�˾��λ��", urlPatterns = { "/Upload_Driver_address" })
public class Upload_Driver_address extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String longitude = request.getParameter("longitude");
			String latitude = request.getParameter("latitude");
			String DriverNo = request.getParameter("DriverNo");
			System.out.println(longitude);
			System.out.println(latitude);
			System.out.println(DriverNo);
			int a = DB_Supplier_Bidding.update_Driver_address(GetConnection.getBiddingConn(), longitude, latitude,DriverNo);
			if (a == 1) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + 1 + "}");
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + 0 + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + 0 + "}");
				System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + 0 + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + e.getMessage() + "}");
		}
		out.flush();
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		doGet(request, response);
	}
}
