package Online_Manager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import DB.GetConnection;
import DB.Select_Online_Manager;

@WebServlet(description = "生鲜商品汇总", urlPatterns = { "/Select_Goods_summary" })
public class Select_Goods_summary extends HttpServlet {
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
		String start_time=request.getParameter("start_time");
		String end_time=request.getParameter("end_time");
		System.out.println(start_time);
		System.out.println(end_time);
		try {
			JSONArray array = Select_Online_Manager.Fresh_items_summary(GetConnection.getonlineConn(),start_time,end_time);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ "0" + "}");
			}
			System.out.println(array.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
