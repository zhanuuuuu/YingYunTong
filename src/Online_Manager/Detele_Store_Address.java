package Online_Manager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;

@WebServlet(description = "É¾³ýµØÖ·", urlPatterns = { "/Detele_Store_Address" })
public class Detele_Store_Address extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Detele_Store_Address() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String cStoreNo = request.getParameter("cStoreNo");
		String cOperatorNo = request.getParameter("cOperatorNo");
		String id = request.getParameter("id");
		LoggerUtil.info(id);
		Connection conn=null;
		try {
		    conn = GetConnection.getStoreConn();
		    PreparedStatement past1 = conn.prepareStatement("select *  from Simple_online.dbo.Store_address_site where id= ? ");
			past1.setString(1, id);
			ResultSet rs=past1.executeQuery();
			if(rs.next()){
				String Provincial=rs.getString("province");
				String City=rs.getString("city");
				String District=rs.getString("district");
				String street=rs.getString("street");
				LoggerUtil.info(street);
				String sql="update  Simple_online.dbo.User_Address set Available='0' where Detailaddress like '%"+street+"%' ";
				PreparedStatement past2 = conn.prepareStatement(sql);
				past2.executeUpdate();
				DB.closePreparedStatement(past2);
				LoggerUtil.info("ÓÐµØÖ·");
			}
			PreparedStatement past = conn.prepareStatement("delete from Simple_online.dbo.Store_address_site where id= ? ");
			past.setString(1, id);
			int a = past.executeUpdate();
			out.print("{\"resultStatus\":\"" + a + "\"," + "\"dDate\":" + a + "}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + 0 + "}");
			e.printStackTrace();
		}
		finally {
			DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}
}
