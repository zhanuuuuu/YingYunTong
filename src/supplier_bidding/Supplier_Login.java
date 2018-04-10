package supplier_bidding;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB_Supplier_Bidding;
import DB.GetConnection;


@WebServlet(description = "¹©Ó¦ÉÌµÇÂ¼", urlPatterns = { "/Supplier_Login" })
public class Supplier_Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Supplier_Login() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String cSupNo=request.getParameter("cSupNo");
			String cSupPassword=request.getParameter("cSupPassword");
			LoggerUtil.info(cSupNo);
			LoggerUtil.info(cSupPassword);
			String a=DB_Supplier_Bidding.Supplier_login(GetConnection.getStoreConn(), cSupNo, cSupPassword);
			if (a.equals("1")) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":\""
						+ cSupNo + "\"}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ cSupNo + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
						+ cSupNo + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":\""+ cSupNo + "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
