package Online_Manager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;

/**
 * Servlet implementation class testServlet
 */
@WebServlet(description = "≤‚ ‘servlet", urlPatterns = { "/testServlet" })
public class testServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String a = request.getParameter("a");
		if (a.equals("1")) {
			return;
		} else {
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn = null;
		try {
			conn = GetConnection.getonlineConn();
			{
				out.print("Œ“ «ƒ„");
				LoggerUtil.info("∫«∫«");
			
				return ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LoggerUtil.info("¡¨Ω”");
			DB.closeConn(conn);
		}
		LoggerUtil.info("¿≤¿≤");
		out.flush();
		out.close();
		
	}
}
