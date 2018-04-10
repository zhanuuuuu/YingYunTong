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
@WebServlet(description = "门店地址管理", urlPatterns = { "/Upload_Store_Address" })
public class Upload_Store_Address extends HttpServlet {
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
		String cStoreName= request.getParameter("cStoreName");
		String cStoreNo=request.getParameter("cStoreNo");
		String Address = request.getParameter("address");
		String Tel = request.getParameter("tel");
		String fLont = request.getParameter("fLont");
		String fLant = request.getParameter("fLant");
		LoggerUtil.info(fLont);
		LoggerUtil.info(fLant);
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String distrinct = request.getParameter("distrinct");
		LoggerUtil.info(distrinct);
		String street = request.getParameter("street");
		String beizhu1 = request.getParameter("beizhu1");
		String beizhu2 = request.getParameter("beizhu2");
		String cOperatorNo=request.getParameter("cOperatorNo");
		String cOperator = request.getParameter("cOperator");
		
		int a= Select_Online_Manager.cStore_Address(GetConnection.getStoreConn(),cStoreNo, cStoreName, Address,Tel, fLont, fLant, province, city, distrinct,street,beizhu1, beizhu2, cOperatorNo,cOperator);
	    LoggerUtil.info("{\"resultStatus\":\"" + a + "\"," + "\"dDate\":"+ a + "}");
		out.print("{\"resultStatus\":\"" + a + "\"," + "\"dDate\":"+ a + "}");
		out.flush();
		out.close();
	} 
}
