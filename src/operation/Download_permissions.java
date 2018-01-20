package operation;
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

@WebServlet("/Download_permissions")
public class Download_permissions extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn=GetConnection.getStoreConn();
		String userNo=request.getParameter("userNo");
		String pass=request.getParameter("pass");
		ResultSet rs=null;
		PreparedStatement past=null;
		try {
			past=conn.prepareStatement("select a.*,c.cStoreNo,c.cStoreName from dbo.YingYunTongFunction a ,dbo.YingYunTongPermission  b,t_Pass c  where a.ID=b.ID and b.UserNo=c.[User] and c.[User]=?  and Pass=?  ");
		    past.setString(1, userNo);
		    past.setString(2, pass);
			rs=past.executeQuery();
		    JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
		    if(array!=null&&array.length()>0){
		    	 out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":"+ array.toString().replace(" ", "") + "}");
		    	 System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":"+ array.toString().replace(" ", "") + "}");
		    }
		    else{
		    	out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":"+ array.toString()+ "}");
		    	 System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":"+ array.toString().replace(" ", "") + "}");
		    }
		   
		} catch (SQLException e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":\""+ ""+ "\"}");
			e.printStackTrace();
		}
		finally{
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
	}
}
