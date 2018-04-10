package Head_shop;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

@WebServlet(description = "查询所有生鲜供货商", urlPatterns = { "/Select_All_Fresh_Supper" })
public class Select_All_Fresh_Supper extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn= GetConnection.getStoreConn();
		
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select cSupNo,cSupName from t_Supplier  where ISNULL(bFresh,0)=1");
			rs=  past.executeQuery();
			JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			if(array.length()>0){
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString().replace(" ", "") + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString().replace(" ", "") + "}");
			}
		} catch (SQLException e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" +0 + "\"}");
			e.printStackTrace();
		}
		finally {
			DB.closeRs_Con(rs, past, conn);
		}
		
		
	}

}
