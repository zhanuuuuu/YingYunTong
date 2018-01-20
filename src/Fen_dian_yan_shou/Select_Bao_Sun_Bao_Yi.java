package Fen_dian_yan_shou;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import DB.Fen_dian_Update;
import DB.GetConnection;

@WebServlet(description = "查询报损报溢", urlPatterns = { "/Select_Bao_Sun_Bao_Yi" })
public class Select_Bao_Sun_Bao_Yi extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String cStoreNo = request.getParameter("cStoreNo");
			String dDate1 = request.getParameter("dDate1");
			String dDate2 = request.getParameter("dDate2");
			String fage = request.getParameter("fage"); // "0是报损 1是褒义"
			System.out.println("0是报损 1是褒义");
			System.out.println(cStoreNo);
			System.out.println(dDate1);
			System.out.println(dDate2);
			System.out.println(fage);
			JSONArray array=new JSONArray();
			if (fage.equals("0")) {
				array = Fen_dian_Update.Select_Bao_Sun(GetConnection.getStoreConn(), cStoreNo,dDate1,dDate2);
			}
			else {
				array = Fen_dian_Update.Select_Bao_Yi(GetConnection.getStoreConn(), cStoreNo,dDate1,dDate2);
			}
			if (array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ array.toString() + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
