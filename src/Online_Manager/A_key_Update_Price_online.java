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

@WebServlet(description = "Ò»¼üÉÏÏß", urlPatterns = { "/A_key_Update_Price_online" })
public class A_key_Update_Price_online extends HttpServlet {
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
		try {
			String data = request.getParameter("data");
			String percentage = request.getParameter("percentage");
			String cStoreNo = request.getParameter("cStoreNo");
			JSONArray array = new JSONArray(data);
			System.out.println(array);
			System.out.println(percentage);
			boolean str = Select_Online_Manager.updateA_key_online(GetConnection.getStoreConn(), array, percentage,cStoreNo);
			if (str) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + "0" + "}");
			}
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
