package operation;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jpush.example.JPushClientExample;

/**
 * Servlet implementation class push
 */
@WebServlet(description = "อฦหอ", urlPatterns = { "/push" })
public class push extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String content=request.getParameter("content");
		String title=request.getParameter("title");
		JPushClientExample.testSend(title,content);
		out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dData\":\"" +"OK" + "\"}");
	}

}
