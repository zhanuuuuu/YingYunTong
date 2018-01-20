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


@WebServlet(description = "²éÑ¯µêÆÌµØÖ·", urlPatterns = { "/Select_Store_Address" })
public class Select_Store_Address extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String cStoreNo = request.getParameter("cStoreNo");
			System.out.println(cStoreNo);
			JSONArray  array = Select_Online_Manager.select_cStore_address(GetConnection.getStoreConn(), cStoreNo);
			if(array.length()>0){
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString().replace(" ", "")+ "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0+ "\"," + "\"dDate\":" + array.toString().replace(" ", "")+ "}");
			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString().replace(" ", "")+ "}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0+ "\"," + "\"dDate\":\"" + 0+ "\"}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
