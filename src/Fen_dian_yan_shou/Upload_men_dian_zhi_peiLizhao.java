package Fen_dian_yan_shou;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import DB.DBYan_Huo_update;
import DB.GetConnection;


@WebServlet(description = "门店直配lizhao", urlPatterns = { "/Upload_men_dian_zhi_peiLizhao" })
public class Upload_men_dian_zhi_peiLizhao extends HttpServlet {
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
		String data = request.getParameter("data");
		System.out.println(data);
		try {
			JSONArray array = new JSONArray(data);
			boolean a = DBYan_Huo_update.insert_into_men_dian_zhi_peiLizhao(GetConnection.getStoreConn(), array);
			if (a) {
				System.out.println("{\"resultStatus\":\"" + 1 + "\""+ "}");
				out.print("{\"resultStatus\":\"" + 1 + "\""+ "}");
			} 
			else {
				System.out.println("{\"resultStatus\":\"" + 0 + "\""+ "}");
				out.print("{\"resultStatus\":\"" + 0 + "\""+ "}");
			}
		} catch (Exception e) {
			System.out.println("{\"resultStatus\":\"" + "服务器异常" + "\""+ "}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
