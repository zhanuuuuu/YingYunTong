package Head_shop;
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
import org.json.JSONArray;
import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

@WebServlet(description = "查询已经盘点的商品", urlPatterns = { "/Select_Yi_pan_goods" })
public class Select_Yi_pan_goods extends HttpServlet {
	
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
		String cStoreNo=request.getParameter("cStoreNo");
		String cCheckTaskNo=request.getParameter("cCheckTaskNo");
		System.out.println(cStoreNo);
		System.out.println(cCheckTaskNo);
		Connection conn= GetConnection.getStoreConn();
		PreparedStatement past=null;
		ResultSet rs=null;
		try {
			past=conn.prepareStatement("select * from  t_CheckTast_GoodsDetail_log where cCheckTaskNo=? and cStoreNo=? ");
			past.setString(1,cCheckTaskNo);
			past.setString(2,cStoreNo);
			rs=past.executeQuery();
			JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
            if(array.length()>0){
            	  out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString().replace(" ", "") + "}");
            }
            else{
            	  out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString().replace(" ", "") + "}");
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}
}
