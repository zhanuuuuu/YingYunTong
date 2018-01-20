package Head_shop;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;
@WebServlet(description = "²éÑ¯ÅÌµã²îÒì", urlPatterns = { "/Query_pan_dian_cha_yi" })
public class Query_pan_dian_cha_yi extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String cCheckTaskNo= request.getParameter("cCheckTaskNo");
		String cZoneNo= request.getParameter("cZoneNo");
		Connection conn=null;
		ResultSet rs=null;
		CallableStatement c=null;
		System.out.println(cCheckTaskNo);
		System.out.println(cZoneNo);
		try {
			conn=GetConnection.getStoreConn();
			c=conn.prepareCall("{call Query_pan_dian_cha_yi (?,?)}");
			c.setString(1, cCheckTaskNo);
			c.setString(2, cZoneNo);
			rs=c.executeQuery();
			JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);

			System.out.println(array.toString());
			if(array!=null&&array.length()>0){
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" +array.toString() + "}");
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" +array.toString() + "}");
			}else{
				System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" +array.toString() + "}");
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" +array.toString() + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DB.DB.closeResultSet(rs);
			DB.DB.closeCallState(c);
			DB.DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}
}
