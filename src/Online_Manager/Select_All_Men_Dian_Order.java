package Online_Manager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;
import DB.Select_Online_Manager;

/**
 * Servlet implementation class Select_All_Men_Dian_Order
 */
@WebServlet(description = "查询所有的门店订单", urlPatterns = { "/Select_All_Men_Dian_Order" })
public class Select_All_Men_Dian_Order extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String cStoreNo = request.getParameter("cStoreNo");
			String start=request.getParameter("starttime");
			String end=request.getParameter("endtime");
			String fage ="1";
			LoggerUtil.info(cStoreNo);
			LoggerUtil.info(start);
			LoggerUtil.info(end);
			String str = Select_Online_Manager.select_Order(GetConnection.getStoreConn(), fage, cStoreNo,start,end);
			out.print(str);
		} catch (Exception e) {

			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
